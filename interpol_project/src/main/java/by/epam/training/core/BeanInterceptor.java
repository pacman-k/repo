package by.epam.training.core;

import java.lang.reflect.Method;

public interface BeanInterceptor {


    void before(Object proxy, Object service, Method method, Object[] args);

    void success(Object proxy, Object service, Method method, Object[] args);

    void fail(Object proxy, Object service, Method method, Object[] args);

}
