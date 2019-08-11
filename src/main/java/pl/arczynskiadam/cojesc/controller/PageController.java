package pl.arczynskiadam.cojesc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        log.info("Homepage visited");
        return "index";
    }

    @GetMapping("/cojesc")
    public String cojesc() {
        return "forward:/";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }
}
