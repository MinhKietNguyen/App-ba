package org.example.batodolist.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Helper class to access Spring beans from non-Spring managed classes
 * (like JPA entity callbacks @PrePersist, @PreUpdate)
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Get a Spring bean by class type
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * Get a Spring bean by name
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * Get the ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }
}