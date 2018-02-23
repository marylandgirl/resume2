package com.example.resume2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private String summary;

}
