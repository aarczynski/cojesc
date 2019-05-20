package pl.arczynskiadam.cojesc.client.facebook.graphapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "pl.arczynskiadam.cojesc")
public class FeignConfig {

}
