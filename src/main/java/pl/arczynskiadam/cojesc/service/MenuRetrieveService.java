package pl.arczynskiadam.cojesc.service;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.client.google.ocr.GoogleOcrClient;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MenuRetrieveService {

    private static final String IMG = "img";
    private static final String A = "a";
    private static final String HREF = "href";
    private static final String SRC = "src";

    private final GoogleOcrClient ocrClient;

    public MenuRetrieveService(GoogleOcrClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    public Optional<String> findLunchMenu(Restaurant restaurant) {
        var webDriver = getNewPhantomJSDriver();

        webDriver.get(restaurant.getMenuUr().toString());
        waitForPageLoaded(webDriver);

        return Jsoup.parse(webDriver.getPageSource())
                .body()
                .getElementsByClass("userContentWrapper")
                .stream()
                .flatMap(this::filterImages)
                .flatMap(this::removeLinks)
                .filter(this::isMenuPost)
                .findFirst()
                .map(Element::toString);

    }

    private Stream<Element> filterImages(Element node) {
        var newNode = node.clone();
        newNode.getElementsByTag(IMG).remove();
        newNode.getElementsByClass("uiScaledImageContainer").remove();

        WebDriver driver = getNewPhantomJSDriver();
        getSpotlightImageUrl(node).ifPresent(url -> {
            driver.get(url);
            waitForPageLoaded(driver);
            new WebDriverWait(driver, 100).until(showSpotlightImagePopup(driver));
        });

        Jsoup.parse(driver.getPageSource())
                .body()
                .getElementsByTag(IMG)
                .stream()
                .filter(e -> e.hasClass("spotlight"))
                .collect(Collectors.toList())
                .forEach(newNode::appendChild);

        return Stream.of(newNode);
    }

    private Optional<String> getSpotlightImageUrl(Element node) {
        Elements spotlightImages = node.getElementsByClass("_1ktf");
        if (spotlightImages.size() == 0) {
            return Optional.empty();
        }

        var url = "https://facebook.com" + spotlightImages.get(0).getElementsByTag(IMG).get(0).attr(SRC);
        url = url.substring(0, url.indexOf('?')) + "?type=3&theater";
        return Optional.of(url);
    }

    private ExpectedCondition<Boolean> showSpotlightImagePopup(WebDriver driver) {
        return js -> {
            String script = "Array.from(document.getElementsByClassName('_1ktf')).forEach(function(e) {e.getElementsByTagName(\"a\")[0].click()})";
            ((JavascriptExecutor) js).executeScript(script);
            return driver.getPageSource().contains("spotlight");
        };
    }

    private Stream<Element> removeLinks(Element node) {
        var newNode = node.clone();
        newNode.getElementsByTag(A).removeAttr(HREF);
        return Stream.of(newNode);
    }

    private void waitForPageLoaded(WebDriver webDriver) {
        new WebDriverWait(webDriver, 10).until(
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    private WebDriver getNewPhantomJSDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/usr/local/bin/phantomjs"
        );
        return new PhantomJSDriver(capabilities);
    }

    //    @SneakyThrows(MalformedURLException.class)
    private boolean isMenuPost(Element e) {
        var keywords = new String[]{"lunch", "lunchowe", "lunchowy", "lunchowa"};
        var days = new String[]{"poniedziałek", "wtorek", "środa", "czwartek", "piątek"};

//        var url = new URL("https://" + e.getElementsByTag(IMG).get(0).attr(HREF));
//        var lunchScore = ocrClient.imageContainsKeywordsScore(url,days);
//        var dayScore = ocrClient.imageContainsKeywordsScore(url, days);

//        if (lunchScore > 0.0 || dayScore > 1.0/5.0) {
//            return true;
//        }

        return StringUtils.containsAny(e.html(), days) && StringUtils.containsAny(e.html(), keywords);
    }

//    private Predicate<Post> after(ZonedDateTime time) {
//        return post -> post.getCreatedTime().isAfter(time);
//    }
//
//    private Predicate<Post> withKeyWords(FacebookFeedRestaurant restaurant) {
//        return p -> containsAll(p.getMessage(), restaurant.getMenuKeyWords());
//    }
//
//    private ZonedDateTime expectedMenuPublishDate(Restaurant restaurant) {
//        var menuDuration = Duration.ofDays(restaurant.getMenuDuration());
//        return ZonedDateTime.now().truncatedTo(DAYS).plus(18, HOURS).minus(menuDuration);
//    }
}
