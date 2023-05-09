package com.mahir.demo.security;

import com.mahir.demo.dao.UserDao;
import com.mahir.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optional = userDao.findByEmail(email);

        if(optional.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        return new MyUserDetails(optional.get());
    }
}
