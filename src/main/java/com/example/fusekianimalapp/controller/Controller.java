package com.example.fusekianimalapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/search")
    public String search(){

        return "search";
    }
    @GetMapping("/searchResult")
    public String searchResult(){
        return "search-result";
    }
    @GetMapping("/resultDetail")
    public String resultDetail(){
        return "result-detail";
    }
}
