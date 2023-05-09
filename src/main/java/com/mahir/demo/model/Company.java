package com.mahir.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.view.CompanyView;
import com.mahir.demo.view.UserView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserView.class, CompanyView.class})
    private Integer id; // Integer because id can be nullable

    @JsonView({UserView.class, CompanyView.class})
    private String name;

    @OneToMany(mappedBy = "company")
    @JsonView(CompanyView.class)
    private Set<User> employeeList = new HashSet<>();
}
