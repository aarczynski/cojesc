package pl.arczynskiadam.cojesc.http.client.facebook.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "pl.arczynskiadam.cojesc")
public class FeignConfig {

}
