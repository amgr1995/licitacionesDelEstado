package es.licitaciones.estado.configuration;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemPropertyConfiguration implements ApplicationListener, Ordered {

    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
            
            String profile = environment.getProperty("spring.profiles.active");
            System.setProperty("spring.profiles.active", profile);
            String service = environment.getProperty("spring.application.name");
            System.setProperty("spring.application.name", service);
            String hostname = "unknown";
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                log.warn("UnknownHostException", e);
            }
            System.setProperty("hostname", hostname);
        }
    }

}
