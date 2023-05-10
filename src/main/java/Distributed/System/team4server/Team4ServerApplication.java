package Distributed.System.team4server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@SpringBootApplication
public class Team4ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team4ServerApplication.class, args);
	}

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customizer() {
		return p -> {
			p.setOneIndexedParameters(true);
			p.setMaxPageSize(10);
		};
	}

}
