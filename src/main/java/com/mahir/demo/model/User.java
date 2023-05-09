package com.mahir.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.view.CompanyView;
import com.mahir.demo.view.UserView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// We use Entity annotation for insertion to table in DB
@Entity
//@Table(name = "user") // Lets us name our table differently than our class' name
// Lombok permits the creation of getters, setters, equals methods with the help of annotations
@Getter
@Setter
public class  User {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENT
    @JsonView({UserView.class, CompanyView.class})
    private Integer id;
    @Column(name = "first_name") // needs to correspond to the column name in DB
//    @Column(length = 80, nullable = false)
    @JsonView({UserView.class, CompanyView.class})
    private String firstName;

    @Column(length = 50, nullable = true) // overrides the instructions in DB
    @JsonView({UserView.class, CompanyView.class}) //
    private String lastName;

    @JsonView({UserView.class, CompanyView.class})
    private String email;

    private String password;

    @JsonView(UserView.class)
    private String profileImageName;

    @ManyToOne
    @JsonView({UserView.class, CompanyView.class})
    private Role role;

    @ManyToOne
    @JsonView(UserView.class)
    private Country country;

    @ManyToOne
    @JsonView(UserView.class)
    private Company company;

    @CreationTimestamp
    @JsonView(UserView.class)
    private LocalDate createdOn;

    @UpdateTimestamp
    @JsonView(UserView.class)
    private LocalDateTime updatedOn;

    // Where we specify the type of relationship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "search_job_user",
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @JsonView(UserView.class)
    private Set<Job> jobSearched = new HashSet<>();
}
