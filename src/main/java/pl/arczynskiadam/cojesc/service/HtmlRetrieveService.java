package pl.arczynskiadam.cojesc.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class HtmlRetrieveService {

    public List<String> fetchByCssClass(URL url, String cssClass) throws RuntimeException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url.toString()).get();
            uncomment(doc);
            sanitize(doc);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to http GET: " + url);
        }

        return doc
                .body()
                .getElementsByClass(cssClass)
                .stream()
                .map(Node::toString)
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
                .toString());
    }

    private Document sanitize(Document doc) {
        doc.select("script").remove();
        doc.getAllElements().removeAttr("href");
        return doc;
    }

    private Document uncomment(Document doc) {
        List<Comment> comments = findAllComments(doc);
        for (Comment comment : comments) {
            String data = comment.getData();
            comment.after(data);
            comment.remove();
        }
        return doc;
    }

    private List<Comment> findAllComments(Document doc) {
        List<Comment> comments = new ArrayList<>();
        for (Element element : doc.getAllElements()) {
            for (Node n : element.childNodes()) {
                if (n.nodeName().equals("#comment")) {
                    var comment = ((Comment) n).getData().replaceAll("\\s", "");
                    if (!comment.contains("[if") && !comment.contains("[endif")) {
                        comments.add((Comment) n);
                    }
                }
            }
        }
        return Collections.unmodifiableList(comments);
    }
}
