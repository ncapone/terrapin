package com.terrapin.controllers;

import com.terrapin.model.Award;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwardController {

    @RequestMapping("/createAward")
    public Award createAward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Award();
    }

    @RequestMapping("/deleteAward")
    public Award deleteAward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Award();
    }

    @RequestMapping("/updateAward")
    public Award updateAward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Award();
    }

    @RequestMapping("/getAward")
    public Award getAward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Award();
    }
}
