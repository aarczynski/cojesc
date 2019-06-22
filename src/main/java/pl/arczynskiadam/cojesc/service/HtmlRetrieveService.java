package pl.arczynskiadam.cojesc.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class HtmlRetrieveService {

    public List<String> fetchByCssClass(URL url, String cssClass) throws RuntimeException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to http GET: " + url);
        }

        return doc
                .body()
                .getElementsByClass(cssClass)
                .stream()
                .map(e -> e.toString())
                .collect(toList());
    }

    public Optional<String> fetchByHtmlId(URL url, String htmlId) throws RuntimeException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to http GET: " + url);
        }

        return Optional.ofNullable(doc
                .body()
                .getElementById(htmlId)
                .html());
    }
}
