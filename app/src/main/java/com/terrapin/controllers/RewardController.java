package com.terrapin.controllers;

import com.terrapin.model.Reward;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardController {

    @RequestMapping("/createReward")
    public Reward createReward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Reward();
    }

    @RequestMapping("/updateReward")
    public Reward updateReward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Reward();
    }

    @RequestMapping("/deleteReward")
    public Reward deleteReward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Reward();
    }

    @RequestMapping("/getReward")
    public Reward getReward(@RequestParam(value="name", defaultValue="World") String name) {
        return new Reward();
    }
}
