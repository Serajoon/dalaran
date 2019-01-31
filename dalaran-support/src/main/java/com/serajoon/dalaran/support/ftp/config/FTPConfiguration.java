package com.serajoon.dalaran.support.ftp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FTPProperties.class)
@Slf4j
public class FTPConfiguration {
}
