package com.mahir.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.demo.dao.CompanyDao;
import com.mahir.demo.model.Company;
import com.mahir.demo.view.CompanyView;
import com.mahir.demo.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CompanyController {
    @Autowired
    public CompanyDao companyDao;

    @GetMapping("/companies")
    @JsonView(CompanyView.class)
    public List<Company> getListCompanies() {
        return companyDao.findAll();
    }

    @GetMapping("/companies/{id}")
    @JsonView(CompanyView.class)
    public Company getCompany(@PathVariable int id) {
        return companyDao
                .findById(id)
                .orElse(null);
    }


    @DeleteMapping("/admin/companies/{id}")
    public boolean deleteCompany(@PathVariable int id) {
        companyDao.deleteById(id);
        return true;
    }

    @PostMapping("/admin/companies")
    public Company addCompany(@RequestBody Company companyToAdd) {
        companyDao.save(companyToAdd);
        return companyToAdd;
    }

}
