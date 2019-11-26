package io.temco.guhada.blockchain.config;

import io.temco.guhada.framework.config.CustomAccessTokenConverter;
import io.temco.guhada.framework.config.OAuth2ResourceServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Created by Youngil.Park
 * Since 2019-04-29
 */
@Configuration
public class ResourceServerConfig extends OAuth2ResourceServer {
    public ResourceServerConfig(CustomAccessTokenConverter customAccessTokenConverter) {
        super(customAccessTokenConverter);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }
}
