package com.example.demo.controllers;

import com.example.demo.dtos.KeywordGroupDto;
import com.example.demo.services.KeywordGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keyword-groups")
public class KeywordGroupController {

    private KeywordGroupService keywordGroupService;

    @Autowired
    KeywordGroupController(KeywordGroupService keywordGroupService) {
        this.keywordGroupService = keywordGroupService;
    }

    @PostMapping
    public void addKeywordGroup(@RequestBody KeywordGroupDto keywordGroupDto) {
        keywordGroupService.addNewKeywordGroup(keywordGroupDto);
    }

}
