package es.licitaciones.estado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import es.licitaciones.estado.configuration.SystemPropertyConfiguration;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableAutoConfiguration
@EntityScan(basePackages = { "es.licitaciones.estado.model.entity" })
@EnableJpaRepositories(basePackages = { "es.licitaciones.estado.repository" })
@ComponentScan(basePackages = { "es.licitaciones.estado" })
public class LicitacionesEstadoApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(LicitacionesEstadoApplication.class);
		application.addListeners(new SystemPropertyConfiguration());
		application.run(args);
	}
}
