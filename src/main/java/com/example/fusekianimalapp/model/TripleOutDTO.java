package com.example.fusekianimalapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripleOutDTO {
    private String subject;
    private String predicate;
    private String object;
}
