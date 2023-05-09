package com.mahir.demo.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.view.UserView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// We use Entity annotation for insertion to table in DB ?
@Entity
// Lombok permits the creation of getters, setters, equals methods with the help of annotations
@Getter
@Setter
public class Country {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENT
    @JsonView(UserView.class)
    private Integer id;

    @JsonView(UserView.class)
    private String name;

}
