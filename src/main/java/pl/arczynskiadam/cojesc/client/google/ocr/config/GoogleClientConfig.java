package pl.arczynskiadam.cojesc.client.google.ocr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/google-credentials.yaml")
public class GoogleClientConfig {

}
