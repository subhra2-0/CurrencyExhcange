package com.maveric.citi;


import com.maveric.citi.repository.ICustomerRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;




@SpringBootApplication
@Configuration
public class CurrencyExchangeApplication {


	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ICustomerRepository customerRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;







	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}




	private SecurityScheme createAPIKeyScheme() {

		return new SecurityScheme().type(SecurityScheme.Type.HTTP)

				.bearerFormat("JWT")

				.scheme("bearer");

	}


	@Bean

	public OpenAPI usersMicroserviceOpenAPI() {

		return new OpenAPI().addSecurityItem(new SecurityRequirement().

						addList("Bearer Authentication"))

				.components(new Components().addSecuritySchemes

						("Bearer Authentication", createAPIKeyScheme()))

				.info(new Info().title("Currency Exchange Portal")

						.description("CITI Currency Exchange Portal")

						.version("1.0"));

	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}







}

