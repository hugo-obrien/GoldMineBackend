package com.blackcubes.skarger.goldmine.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/test")
    public void test() {
        System.out.println("Surely common test works");
    }

    @RequestMapping("/response-test")
    public String responseTest() {
        System.out.println("Response test works too");
        return "Hello, world!";
    }
}
