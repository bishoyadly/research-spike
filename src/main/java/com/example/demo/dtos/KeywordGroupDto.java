package com.example.demo.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordGroupDto {

    private String groupName;

    private List<String> keywordList;
}
