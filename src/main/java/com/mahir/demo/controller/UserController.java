package com.mahir.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.dao.UserDao;
import com.mahir.demo.model.Country;
import com.mahir.demo.model.Role;
import com.mahir.demo.model.User;
import com.mahir.demo.security.JwtUtils;
import com.mahir.demo.service.FileService;
import com.mahir.demo.view.UserView;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileService fileService;

    @GetMapping("/users")
    @JsonView(UserView.class)
    public List<User> getUsers() {

        return userDao.findAll();
    }

    /*
 Unsafe method
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id) {
        return userDao.findById(id).orElse(null);
    }
*/

    @GetMapping("/user/{id}")
    @JsonView(UserView.class)
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> optionalUser = userDao.findById(id);

        return optionalUser.map(user ->
                new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));

//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String bearer) {
        String jwt = bearer.substring(7);
        Claims data = jwtUtils.getData(jwt);
        Optional<User> optionalUser = userDao.findByEmail(data.getSubject());

        return optionalUser
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        /*
         replaced by above line
         if (optionalUser.isPresent())
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);

         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        */
    }


/*
    First method
    @PostMapping("/user")
    public boolean addUser() {
        User user = new User();
        user.setId(4);
        user.setFirstName("coco");
        user.setLastName("kiki");
        userDao.save(user);
        return true;
    }
*/
    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int userId) {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isPresent()) {
            String imageName  = optionalUser.get().getProfileImageName();

            try {
                byte[] image = fileService.getImageByName(imageName);

                HttpHeaders header = fileService.getHeaderImageType(imageName);

                return new ResponseEntity<>(image, header, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                System.out.println("je suis là");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (IOException e) {
                System.out.println("mimeType test failed!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        System.out.println("yoooooo");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //     Second method
    @CrossOrigin
    @PostMapping("/admin/user")
    public ResponseEntity<User> addUser(
            @RequestPart("user") User newUser,
            @Nullable @RequestParam("file") MultipartFile file
    ) {

        if (newUser.getId() != null) {

            Optional<User> optionalUser = userDao.findById(newUser.getId());
            // If it's an update
            if (optionalUser.isPresent()) {

                User userToUpdate = optionalUser.get();
                userToUpdate.setLastName (newUser.getLastName ());
                userToUpdate.setFirstName(newUser.getFirstName());
                userToUpdate.setEmail    (newUser.getEmail    ());
                userToUpdate.setCountry  (newUser.getCountry  ());

                if (file != null) {
                    try {
                        String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        newUser.setProfileImageName(imageName);
                        fileService.uploadToLocalFileSystem(file, imageName);
                    } catch (IOException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                userDao.save(userToUpdate);

                return new ResponseEntity<>(newUser, HttpStatus.OK);
            }

            return new ResponseEntity<>(newUser, HttpStatus.BAD_REQUEST);
        }
        Role role = new Role();
        role.setId(2);
        newUser.setRole(role);

        String hashedPassword = passwordEncoder.encode("root");
        newUser.setPassword(hashedPassword);

        if (file != null) {
            try {
                String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                newUser.setProfileImageName(imageName);
                fileService.uploadToLocalFileSystem(file, imageName);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        userDao.save(newUser);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/user/{id}")
    @JsonView(UserView.class)
    public ResponseEntity<User> deleteUser(@PathVariable int id) {

        Optional<User> userToBeDeleted = userDao.findById(id);

        if (userToBeDeleted.isPresent()) {
            userDao.deleteById(id);
            return new ResponseEntity<>(userToBeDeleted.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
