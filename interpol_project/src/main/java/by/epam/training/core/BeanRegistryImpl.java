package by.epam.training.core;

import lombok.*;
import lombok.extern.log4j.Log4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
@Log4j
public class BeanRegistryImpl implements BeanRegistry {

    private FactoryBean factoryBean = new FactoryBean();
    private Set<RegistryInfo> beanRegistry = new HashSet<>();

    @Override
    public <T> void registerBean(T bean) {

        RegistryInfo info = calculateRegistryInfo(bean.getClass());
        info.setConcreteBean(bean);
        addRegistryInfo(info);
    }

    @Override
    public <T> void registerBean(Class<T> beanClass) {
        RegistryInfo info = calculateRegistryInfo(beanClass);
        final Supplier<Object> factory = createFactory(info);
        info.setFactory(factory);
        addRegistryInfo(info);
    }

    private void addRegistryInfo(RegistryInfo info) {
        beanRegistry.stream()
                .filter(registryInfo -> registryInfo.getName().equals(info.getName()))
                .findFirst()
                .ifPresent(registryInfo -> {
                    log.error("Bean with name " + registryInfo.getName() + " already registered");
                    throw new NotUniqueBeanException("Bean with name " + registryInfo.getName() + " already registered");
                });
        beanRegistry.add(info);
    }

    private Supplier<Object> createFactory(RegistryInfo info) {

        Class<?> clazz = info.getClazz();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length > 1) {
            log.error("More than 1 constructor is present for class " + clazz.getSimpleName());
            throw new BeanInstantiationException("More than 1 constructor is present for class " + clazz.getSimpleName());
        }

        return () -> {
            Constructor<?> constructor = constructors[0];
            if (constructor.getParameterCount() > 0) {
                Parameter[] parameters = constructor.getParameters();
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {

                    Class<?> type = parameters[i].getType();
                        args[i] = getBean(type);
                }
                try {
                    return constructor.newInstance(args);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    log.error("Failed to instantiate bean", e);
                    throw new BeanInstantiationException("Failed to instantiate bean", e);
                }
            } else {
                try {
                    return clazz.newInstance();
                } catch ( InstantiationException | IllegalAccessException e) {
                    log.error("Failed to instantiate bean", e);
                    throw new BeanInstantiationException("Failed to instantiate bean", e);
                }
            }
        };
    }

    private RegistryInfo calculateRegistryInfo(Class<?> beanClass) {

        Bean bean = beanClass.getAnnotation(Bean.class);
        if (bean == null) {
            throw new MissedAnnotationException(beanClass.getName() + " doesn't have @Bean annotation");
        }

        RegistryInfo info = new RegistryInfo();
        info.setClazz(beanClass);

        Class<?>[] interfaces = beanClass.getInterfaces();
        info.setInterfaces(Arrays.stream(interfaces).collect(Collectors.toSet()));

        Annotation[] annotations = beanClass.getAnnotations();
        info.setAnnotations(Arrays.stream(annotations).collect(Collectors.toSet()));

        Interceptor interceptor = beanClass.getAnnotation(Interceptor.class);
        if (interceptor != null) {
            info.setInterceptor(interceptor);
        }

        String beanName = bean.name();
        if (beanName.trim().length() > 0) {
            info.setName(beanName);
        } else if (beanClass.getInterfaces().length == 1) {
            info.setName(beanClass.getInterfaces()[0].getSimpleName());
        } else {
            info.setName(beanClass.getSimpleName());
        }

        return info;
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {

        Bean bean = beanClass.getAnnotation(Bean.class);
        String beanName = (bean != null && bean.name().trim().length() > 0) ? bean.name().trim() : null;
        Predicate<RegistryInfo> searchBean = info -> info.getName().equals(beanName) || info.getClazz().equals(beanClass) || info.getInterfaces().contains(beanClass);
        return getBean(searchBean);
    }

    @Override
    public <T> T getBean(String beanName) {

        Predicate<RegistryInfo> searchBean = info -> info.getName().equals(beanName) || info.getClazz().getSimpleName().equals(beanName);
        return getBean(searchBean);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBean(Predicate<RegistryInfo> searchBean) {
        List<RegistryInfo> registryInfoList = beanRegistry
                .stream()
                .filter(searchBean)
                .collect(Collectors.toList());

        if (registryInfoList.size() > 1) {
            String multipleNames = registryInfoList.stream().map(RegistryInfo::getName).collect(Collectors.joining(", "));
            log.error("Multiple implementations found: " + multipleNames);
            throw new NotUniqueBeanException("Multiple implementations found: " + multipleNames);
        } else {
            return (T) registryInfoList.stream().map(this::mapToBean).findFirst().orElse(null);
        }
    }

    @Override
    public <T> boolean removeBean(T bean) {

        RegistryInfo registryInfo = calculateRegistryInfo(bean.getClass());
        return beanRegistry.remove(registryInfo);
    }

    @Override
    public void destroy() {
        factoryBean.destroy();
        beanRegistry.clear();
    }

    @SuppressWarnings("unchecked")
    private <T> T mapToBean(RegistryInfo registryInfo) {
        T service = (T) factoryBean.getBean(registryInfo);
        Set<RegistryInfo> availableInterceptors = beanRegistry.stream()
                .filter(RegistryInfo::isInterceptor)
                .filter(interceptorInfo -> registryInfo.getAnnotations()
                        .stream()
                        .anyMatch(a -> a.annotationType().equals(interceptorInfo.getInterceptor().clazz())))
                .collect(Collectors.toSet());

        if (availableInterceptors.isEmpty()) {
            return service;
        } else {
            List<BeanInterceptor> interceptors = availableInterceptors.stream()
                    .map(interceptorInfo -> (BeanInterceptor) factoryBean.getBean(interceptorInfo))
                    .collect(Collectors.toList());
            return getServiceProxy(service, registryInfo, interceptors);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getServiceProxy(T service, RegistryInfo info, List<BeanInterceptor> interceptors) {
        Class<?>[] toProxy = new Class[info.getInterfaces().size()];
        Class<?>[] interfaces = info.getInterfaces().toArray(toProxy);
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces,
                (proxy, method, args) -> {

                    try {
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.before(proxy, service, method, args);
                        }
                        Object invoked = method.invoke(service, args);
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.success(proxy, service, method, args);
                        }
                        return invoked;
                    } catch (Exception e) {
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.fail(proxy, service, method, args);
                        }
                        log.error("Exception during proxy invocation. method");
                       throw new IllegalStateException("Exception during proxy invocation" , e);
                    }
                });
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RegistryInfo {

        private String name;
        private Class clazz;
        private Set<Class<?>> interfaces;
        private Set<Annotation> annotations;
        private Interceptor interceptor;
        private Supplier<?> factory;
        private Object concreteBean;

        boolean isInterceptor() {
            return this.interceptor != null;
        }
    }

    private static class FactoryBean {

        private Map<RegistryInfo, Object> beans = new ConcurrentHashMap<>();

        Object getBean(RegistryInfo info) {

            if (info.getConcreteBean() != null) {
                beans.put(info, info.getConcreteBean());
            } else if (!beans.containsKey(info)) {
                final Object bean = info.getFactory().get();
                beans.put(info, bean);
            }
            return beans.get(info);
        }


        void destroy() {
            beans.clear();
        }
    }
}