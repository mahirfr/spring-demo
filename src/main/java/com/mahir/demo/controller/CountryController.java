package com.mahir.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.dao.CountryDao;
import com.mahir.demo.model.Country;
import com.mahir.demo.view.CountryView;
import com.mahir.demo.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class CountryController {

    @Autowired
    private CountryDao countryDao;

    @GetMapping("/country-list")
    public List<Country> getCountries() {

        List<Country> countries = countryDao.findAll();

        return countries;
    }

    // Unsafe method
//    @GetMapping("/country/{id}")
//    public Country getCountry(@PathVariable int id) {
//        return countryDao.findById(id).orElse(null);
//    }

    @GetMapping("/country/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable int id) {
        Optional<Country> optionalCountry = countryDao.findById(id);

        if (optionalCountry.isPresent())
            return new ResponseEntity<>(optionalCountry.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//    First method

//    @PostMapping("/country")
//    public boolean addCountry() {
//        Country country = new Country();
//        country.setId(4);
//        country.setName("pays");
//        countryDao.save(country);
//        return true;
//    }

//     Second method

    @PostMapping("/admin/country")
    public ResponseEntity<Country> addCountry(@RequestBody Country newCountry) {

        if (newCountry.getId() != null) {

            Optional<Country> optionalCountry = countryDao.findById(newCountry.getId());

            if (optionalCountry.isPresent()) {
                countryDao.save(newCountry);
                return new ResponseEntity<>(newCountry, HttpStatus.OK);
            }

            return new ResponseEntity<>(newCountry, HttpStatus.BAD_REQUEST);
        }

        countryDao.save(newCountry);
        return new ResponseEntity<>(newCountry, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/country/{id}")
    public ResponseEntity<Country> deleteCountry(@PathVariable int id) {

        Optional<Country> countryToBeDeleted = countryDao.findById(id);

        if (countryToBeDeleted.isPresent()) {
            countryDao.deleteById(id);
            return new ResponseEntity<>(countryToBeDeleted.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
