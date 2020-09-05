package com.example.demo.controllers;


import com.example.demo.dtos.RuleDto;
import com.example.demo.services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules")
public class RuleController {

    private RuleService ruleService;

    @Autowired
    RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public void addRule(@RequestBody RuleDto ruleDto) {
        this.ruleService.addNewRule(ruleDto);
    }
}
