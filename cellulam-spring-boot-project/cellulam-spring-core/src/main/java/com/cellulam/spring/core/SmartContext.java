package com.cellulam.spring.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spring-context, 在 <code>ApplicationContextInitializedEvent</code> 事件 之后, 设置 applicationContext
 */
public final class SmartContext implements ListableBeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(SmartContext.class);

    private static ConfigurableApplicationContext context;

    private SmartContext() {
    }

    /**
     * 初始化 spring-context. 若已存在, 则重置
     *
     * @param context
     */
    static void initOrResetContext(ConfigurableApplicationContext context) {
        if (SmartContext.context == null) {
            logger.info("init set spring-context#{}, hash#{}. env-hash#{}", context.hashCode(),
                    System.identityHashCode(context), System.identityHashCode(context.getEnvironment()));
        } else {
            if (SmartContext.context != context) {
                logger.info("override set spring-context#{}, hash#{}, env-hash#{}", context.hashCode(),
                        System.identityHashCode(context), System.identityHashCode(context.getEnvironment()));
            }
        }

        SmartContext.context = context;
    }

    // below is BeanFactory
    @Override
    public Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return context.getBean(name, requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return context.getBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return context.getBean(requiredType, args);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return context.getBeanProvider(requiredType);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return context.getBeanProvider(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return context.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return context.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return context.isPrototype(name);
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return context.isTypeMatch(name, typeToMatch);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return context.isTypeMatch(name, typeToMatch);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return context.getType(name);
    }

    @Override
    public Class<?> getType(String s, boolean b) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public String[] getAliases(String name) {
        return context.getAliases(name);
    }

    /**
     * 获取一个类型的所有的已定义的bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getBeans(Class<T> clazz) {
        Map<String, T> map = context.getBeansOfType(clazz);
        return map.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 根据bean-定义，注册一个bean
     *
     * @param beanName
     * @param beanDefinition
     */
    public void register(String beanName, AbstractBeanDefinition beanDefinition) {
        DefaultListableBeanFactory beanFactory = getDefaultListableBeanFactory();
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * @param action
     */
    public void applyBeanDefinitionRegistry(BeanDefinitionRegistryAction action) {
        DefaultListableBeanFactory beanFactory = getDefaultListableBeanFactory();
        action.apply(beanFactory);
    }

    private DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) context.getBeanFactory();
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    // below is ListableBeanFactory
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return context.containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return context.getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass, boolean b) {
        return null;
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType, boolean b) {
        return null;
    }

    @Override
    public String[] getBeanNamesForType(ResolvableType type) {
        return context.getBeanNamesForType(type);
    }

    @Override
    public String[] getBeanNamesForType(ResolvableType resolvableType, boolean b, boolean b1) {
        return new String[0];
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return context.getBeanNamesForType(type);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return context.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return context.getBeansOfType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        return context.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }

    @Override
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return context.getBeanNamesForAnnotation(annotationType);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        return context.getBeansWithAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
        return context.findAnnotationOnBean(beanName, annotationType);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        return context.findAnnotationOnBean(beanName, annotationType, allowFactoryBeanInit);
    }

}
