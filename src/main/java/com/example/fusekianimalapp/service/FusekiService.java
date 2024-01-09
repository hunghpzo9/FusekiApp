package com.example.fusekianimalapp.service;

import com.example.fusekianimalapp.common.RestService;
import com.example.fusekianimalapp.model.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FusekiService {
    @Autowired
    private RestService restService;
    public List<Triple> getAllTripleBySub(String searchText){
        List<Triple> triples =  restService.getDataByAllFieldContains(searchText);
        triples.forEach(triple -> {
            String desc = triple.getObject();
            String[] words = desc.split("\\W+");
            for (String word : words) {
               Triple tr= restService.getTripleDistinctBySubject(word);
               if(tr!=null){
                   desc = desc.replace(word,"<a href=\"#\">"+word+"</a>");
               }
            }
            triple.setObject(desc);
        });
        return triples;
    }
    public List<Triple> getAllTripleInDescription(List<Triple> triples){
        triples.forEach(triple -> {
            String[] words = triple.getObject().split("\\W+");
            for (String word : words) {
                restService.getTripleDistinctBySubject(word);
            }
        });
      return null;
    }
}
