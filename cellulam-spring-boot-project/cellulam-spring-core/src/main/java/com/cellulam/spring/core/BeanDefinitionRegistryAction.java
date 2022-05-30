package com.cellulam.spring.core;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 *
 */
public interface BeanDefinitionRegistryAction {

    void apply(BeanDefinitionRegistry registry);

}
