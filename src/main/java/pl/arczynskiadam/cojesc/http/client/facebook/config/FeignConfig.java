package pl.arczynskiadam.cojesc.http.client.facebook.config;

import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableFeignClients(basePackages = "pl.arczynskiadam.cojesc")
@PropertySource("classpath:/credentials.yaml")
@EnableConfigurationProperties(FacebookApiAuthProperties.class)
public class FeignConfig {

    private static final String ACCESS_TOKEN = "access_token";

    @Bean
    public RequestInterceptor requestInterceptor(FacebookApiAuthProperties auth) {
        return requestTemplate -> requestTemplate.query(ACCESS_TOKEN, String.join("|", auth.getAppId(), auth.getSecret()));
    }
}
