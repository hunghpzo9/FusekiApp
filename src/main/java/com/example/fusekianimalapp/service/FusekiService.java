package com.example.fusekianimalapp.service;

import com.example.fusekianimalapp.common.RestService;
import com.example.fusekianimalapp.model.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FusekiService {
    @Autowired
    private RestService restService;
    public List<Triple> getAllTripleBySub(String searchText){
        return restService.getBySubject(searchText);
    }
}
