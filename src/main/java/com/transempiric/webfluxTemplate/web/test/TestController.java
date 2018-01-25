package com.transempiric.webfluxTemplate.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/test")
public class TestController {

    @GetMapping("/ws")
    public String websocket() {
        return "websocket";
    }
}
