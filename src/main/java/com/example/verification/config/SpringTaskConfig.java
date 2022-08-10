package com.example.verification.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
//@EnableScheduling
@ComponentScan({ "com.example.verification.task" })
public class SpringTaskConfig {
}
