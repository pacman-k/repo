package by.epam.training.dao;

import by.epam.training.core.Bean;
import by.epam.training.core.BeanInterceptor;
import by.epam.training.core.Interceptor;
import lombok.extern.log4j.Log4j;

import java.lang.reflect.Method;
import java.util.Arrays;

@Bean
@Log4j
@Interceptor(clazz = TransactionSupport.class)
public class TransactionInterceptor implements BeanInterceptor {
    private TransactionManager transactionManager;

    public TransactionInterceptor(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void before(Object proxy, Object service, Method method, Object[] args) {

        if (isMethodHasTransaction(service, method)) {
            try {
                transactionManager.beginTransaction();
            } catch (TransactionManagerException e) {
                throw new IllegalStateException("Failed to begin transaction", e);
            }
        }
    }

    @Override
    public void success(Object proxy, Object service, Method method, Object[] args) {
        if (isMethodHasTransaction(service, method)) {
            try {
                transactionManager.commitTransaction();
            } catch (TransactionManagerException e) {
                throw new IllegalStateException("Failed to commit transaction", e);
            }
        }
    }

    @Override
    public void fail(Object proxy, Object service, Method method, Object[] args) {

        if (isMethodHasTransaction(service, method)) {
            try {
                transactionManager.rollbackTransaction();
            } catch (TransactionManagerException e) {
                throw new IllegalStateException("Failed to rollback transaction", e);
            }
        }
    }

    private boolean isMethodHasTransaction(Object service, Method method) {
        boolean methodHasTransaction = method.getAnnotation(Transactional.class) != null;
        if (!methodHasTransaction) {
            methodHasTransaction = Arrays.stream(service.getClass().getDeclaredMethods())
                    .filter(serviceMethod -> serviceMethod.getName().equals(method.getName()))
                    .anyMatch(serviceMethod -> serviceMethod.getAnnotation(Transactional.class) != null);
        }
        return methodHasTransaction;
    }
}
