package com.example.fusekianimalapp.controller;

import com.example.fusekianimalapp.common.LinkGenerator;
import com.example.fusekianimalapp.common.RestService;
import com.example.fusekianimalapp.model.Triple;
import com.example.fusekianimalapp.service.FusekiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    FusekiService fusekiService;
    @GetMapping("/search")
    public String search(){

        return "search";
    }
    @GetMapping("/searchResult")
    public String searchResult(Model model,@RequestParam("searchText") String searchText){
        List<Triple> tripleList = fusekiService.getAllTripleBySub(searchText);
        model.addAttribute("triples",tripleList);

        return "search-result";
    }
    @GetMapping("/resultDetail")
    public String resultDetail(){
        return "result-detail";
    }

    @PostMapping("/searchText")
    public String searchText(Model model,@RequestParam("searchText") String searchText) {
        model.addAttribute("triples",fusekiService.getAllTripleBySub(searchText));
        return "redirect:/searchResult";
    }
}
