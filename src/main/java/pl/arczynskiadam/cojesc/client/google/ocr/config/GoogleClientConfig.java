package pl.arczynskiadam.cojesc.client.google.ocr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:/google-credentials.yaml", ignoreResourceNotFound = true)
public class GoogleClientConfig {

}
