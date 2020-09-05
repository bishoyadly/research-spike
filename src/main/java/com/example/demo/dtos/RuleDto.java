package com.example.demo.dtos;

import com.example.demo.models.RuleType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RuleDto {

    String ruleName;
    RuleType ruleType;
    List<RuleKeywordGroupDto> ruleKeywordGroupDtoList;
    List<SystemAttributeDto> systemAttributeDtoList;
}
