package com.mahir.demo.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.view.UserView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

// Lombok permits the creation of getters, setters, equals methods with the help of annotations
@Entity
@Getter
@Setter
public class Role {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENT
    @JsonView(UserView.class)
    private Integer id;

    @JsonView(UserView.class)
    private String name;

}
