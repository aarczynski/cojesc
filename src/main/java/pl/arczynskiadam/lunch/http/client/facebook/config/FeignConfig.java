package pl.arczynskiadam.lunch.http.client.facebook.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "pl.arczynskiadam.lunch")
public class FeignConfig {

}
