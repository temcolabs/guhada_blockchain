package io.temco.guhada.blockchain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-${spring.profiles.active:dev}.yml", factory = YamlPropertySourceFactory.class)
public class JasyptConfiguration {
}
