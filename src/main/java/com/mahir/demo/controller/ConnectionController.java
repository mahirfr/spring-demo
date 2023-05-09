package com.mahir.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.dao.UserDao;
import com.mahir.demo.model.User;
import com.mahir.demo.security.JwtUtils;
import com.mahir.demo.security.MyUserDetails;
import com.mahir.demo.security.MyUserDetailsService;
import com.mahir.demo.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ConnectionController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @PostMapping("/login")
    @JsonView(UserView.class)
    public ResponseEntity<String> connection(@RequestBody User user) {

        MyUserDetails userDetails;

        try {
            userDetails = (MyUserDetails) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            ).getPrincipal();
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(jwtUtils.generateJwt(userDetails), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @JsonView(UserView.class)
    public ResponseEntity<User> signup(@RequestBody User user) {

        if (user.getId() != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (user.getPassword().length() <= 3)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(user.getEmail()).matches())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> optionalUser = userDao.findByEmail(user.getEmail());

        if (optionalUser.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userDao.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }
}
