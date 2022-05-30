package com.cellulam.spring.datasource.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.cellulam.spring.datasource")
@EnableAutoConfiguration
@SpringBootConfiguration
public class TestConf {
}
