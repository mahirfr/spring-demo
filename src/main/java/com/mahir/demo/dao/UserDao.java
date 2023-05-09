package com.mahir.demo.dao;

import com.mahir.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository // Same as @Component but clearer it also means that the class is gonna be used for storage or CRUD operations
public interface UserDao extends JpaRepository<User, Integer> {
    User findByFirstName(String firstName);
    Optional<User> findByEmail (String email);
}
