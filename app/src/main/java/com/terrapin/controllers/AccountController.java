package com.terrapin.controllers;

import com.terrapin.model.Account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @RequestMapping("/createAccount")
    public Account createAccount(@RequestParam(value="name", defaultValue="World") String name) {
        return new Account();
    }

    @RequestMapping("/deleteAccount")
    public Account deleteAccount(@RequestParam(value="name", defaultValue="World") String name) {
        return new Account();
    }

    @RequestMapping("/updateAccount")
    public Account updateAccount(@RequestParam(value="name", defaultValue="World") String name) {
        return new Account();
    }

    @RequestMapping("/getAccount")
    public Account getAccount(@RequestParam(value="name", defaultValue="World") String name) {
        return new Account();
    }

}
