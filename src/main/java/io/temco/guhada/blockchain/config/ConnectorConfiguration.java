package io.temco.guhada.blockchain.config;

import io.temco.guhada.blockchain.util.SelfCertificateManager;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

@Configuration
@PropertySource("classpath:application.yml")
public class ConnectorConfiguration {

    @Value("${server.ssl.useSelfCertificate}")
    private boolean useSelfCertificate;

    @Bean
    @Primary
    public ServerProperties sslProperties() {
        ServerProperties properties = new ServerProperties();
        Ssl ssl = useSelfCertificate ? createLocal() : createTrusted();    new Ssl();
        properties.setSsl(ssl);
        return properties;
    }


    @Value("${key-store-password}") private String keyStorePassword;
    private Ssl createTrusted() {
        Ssl ssl = new Ssl();
        ssl.setKeyPassword(keyStorePassword);
        System.setProperty("server.ssl.key-store-password", keyStorePassword);
        return ssl;
    }

    private Ssl createLocal() {
        SelfCertificateManager selfCertificateManager = new SelfCertificateManager();
        File keyStore = selfCertificateManager.createCaCert(keyStorePassword);
        Ssl ssl = new Ssl();
        ssl.setKeyPassword(keyStorePassword);
        System.setProperty("server.ssl.key-store", keyStore.getAbsolutePath());
        System.setProperty("server.ssl.key-store-password", keyStorePassword);
        return ssl;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);

            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }


    @Value("${server.nonSecure.port}")
    private int serverPort;

    @Value("${server.port}")
    private int serverSecurePort;

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(serverPort);
        connector.setSecure(false);
        connector.setRedirectPort(serverSecurePort);
        return connector;
    }
}
