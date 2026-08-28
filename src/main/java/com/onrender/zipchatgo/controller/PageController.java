package com.onrender.zipchatgo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Thymeleaf 페이지 이동을 담당하는 Controller.
 * templates 폴더의 HTML과 브라우저 URL을 연결한다.
 *
 * /support/* 는 support.SupportPageController 로 이관하여 여기서는 제거함(중복 매핑 방지).
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/signup")
    public String signup() {
        return "member/signup";
    }

    @GetMapping("/favorite")
    public String favorite() {
        return "favorite/favorite";
    }

    // /property/map 은 map.MapPageController 로 이관됨(중복 정의 방지)

    @GetMapping("/property/register")
    public String propertyRegister() {
        return "property/register";
    }

    @GetMapping("/market/trend")
    public String marketTrend() {
        return "market/trend";
    }

    @GetMapping("/market/sale")
    public String marketSale() {
        return "market/sale";
    }

    @GetMapping("/market/jeonse")
    public String marketJeonse() {
        return "market/jeonse";
    }

    @GetMapping("/market/volume")
    public String marketVolume() {
        return "market/volume";
    }

    @GetMapping("/market/region")
    public String marketRegion() {
        return "market/region";
    }

    @GetMapping("/market/region-flow")
    public String marketRegionFlow() {
        return "market/region-flow";
    }

    @GetMapping("/market/rate")
    public String marketRate() {
        return "market/rate";
    }

    @GetMapping("/market/school")
    public String marketSchool() {
        return "market/school";
    }

    @GetMapping("/market/traffic")
    public String marketTraffic() {
        return "market/traffic";
    }

    @GetMapping("/market/cost")
    public String marketCost() {
        return "market/cost";
    }

    @GetMapping("/market/ai-report")
    public String marketAiReport() {
        return "market/ai-report";
    }
}
