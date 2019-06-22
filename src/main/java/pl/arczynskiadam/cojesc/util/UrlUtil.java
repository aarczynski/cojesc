package pl.arczynskiadam.cojesc.util;

import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

    @SneakyThrows(MalformedURLException.class)
    public static URL toUrl(String url) {
        return new URL(url);
    }
}
