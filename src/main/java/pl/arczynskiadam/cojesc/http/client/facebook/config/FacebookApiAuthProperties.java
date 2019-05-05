package pl.arczynskiadam.cojesc.http.client.facebook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cojesc.http.client.facebook")
public class FacebookApiAuthProperties {
    private String appId;
    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
