package com.eplugger.core.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        SpringBeanUtils.appContext = appContext;
    }

    /**
     * 通过name获取bean
     *
     * @param beanName bean注册名
     * @param cls      bean类型
     * @return bean
     */
    public static <T> T getBeanByName(String beanName, Class<T> cls) {
        return appContext.getBean(beanName, cls);
    }

    /**
     * 通过name获取bean
     *
     * @param beanName bean注册名
     * @return bean
     */
    public static Object getBeanByName(String beanName) {
        return appContext.getBean(beanName);
    }

    /**
     * 获得继承某个注解的类集合
     *
     * @param annotationType 注解
     * @return
     */
    public static Collection<Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return appContext.getBeansWithAnnotation(annotationType).values();
    }

    /**
     * 获取某个类型的唯一bean
     *
     * @param cls
     * @return
     */
    public static <T> T getBeanOfType(Class<T> cls) {
        return appContext.getBean(cls);
    }

    /**
     * 获取某个类型的bean
     *
     * @param cls
     * @return
     */
    public static <T> Collection<T> getBeansOfType(Class<T> cls) {
        Map<String, T> map = appContext.getBeansOfType(cls);
        if (map != null) {
            return appContext.getBeansOfType(cls).values();
        } else {
            return null;
        }
    }

    /**
     * 通过类型获得bena的注册名
     */
    public static String[] getBeanNamesForType(Class<?> cls) {
        return appContext.getBeanNamesForType(cls);
    }
}
