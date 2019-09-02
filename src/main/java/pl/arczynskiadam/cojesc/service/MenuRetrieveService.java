package pl.arczynskiadam.cojesc.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Service
public class MenuRetrieveService {

    public Optional<String> findLunchMenu(Restaurant restaurant) {
        var webDriver = getNewPhantomJSDriver();

        webDriver.get(restaurant.getMenuUr().toString());
        waitForPageLoaded(webDriver);
        expandFacebookPosts(webDriver);

        return Jsoup.parse(webDriver.getPageSource())
                .body()
                .getElementsByClass("userContentWrapper")
                .stream()
                .map(Node::toString)
                .filter(html -> containsIgnoreCase(html, "lunch") || containsIgnoreCase(html, "zapraszamy") || containsIgnoreCase(html, "czekamy"))
                .findFirst();
    }

    private void expandFacebookPosts(WebDriver webDriver) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        String script = "Array.from(document.getElementsByClassName('see_more_link')).forEach(function(e) {e.click()})";
        js.executeScript(script);
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
