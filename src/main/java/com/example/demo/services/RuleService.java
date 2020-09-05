package com.example.demo.services;

import com.example.demo.dtos.RuleDto;
import com.example.demo.dtos.RuleKeywordGroupDto;
import com.example.demo.dtos.SystemAttributeDto;
import com.example.demo.models.KeywordGroup;
import com.example.demo.models.Rule;
import com.example.demo.models.RuleKeywordGroup;
import com.example.demo.models.SystemAttribute;
import com.example.demo.repositories.KeywordGroupRepository;
import com.example.demo.repositories.RuleRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private RuleRepository ruleRepository;
    private KeywordGroupRepository keywordGroupRepository;

    @Autowired
    RuleService(RuleRepository ruleRepository, KeywordGroupRepository keywordGroupRepository) {
        this.ruleRepository = ruleRepository;
        this.keywordGroupRepository = keywordGroupRepository;
    }

    public void addNewRule(RuleDto ruleDto) {
        Rule rule = new Rule();
        rule.setRuleName(ruleDto.getRuleName());
        rule.setRuleType(ruleDto.getRuleType());
        List<RuleKeywordGroup> ruleKeywordGroupList = new ArrayList<>();
        List<SystemAttribute> systemAttributeList = new ArrayList<>();

        for (RuleKeywordGroupDto ruleKeywordGroupDto : ruleDto.getRuleKeywordGroupDtoList()) {
            KeywordGroup keywordGroup = keywordGroupRepository.findById(ruleKeywordGroupDto.getKeywordGroupId()).orElseThrow();
            RuleKeywordGroup ruleKeywordGroup = new RuleKeywordGroup();
            ruleKeywordGroup.setKeywordGroup(keywordGroup);
            ruleKeywordGroup.setLogicalOperation(ruleKeywordGroupDto.getLogicalOperation());
            ruleKeywordGroup.setOperationOrder(ruleKeywordGroupDto.getOperationOrder());
            ruleKeywordGroup.setRule(rule);
            ruleKeywordGroupList.add(ruleKeywordGroup);
        }
        rule.setRuleKeywordGroupList(ruleKeywordGroupList);

        for (SystemAttributeDto systemAttributeDto : ruleDto.getSystemAttributeDtoList()) {
            SystemAttribute systemAttribute = new SystemAttribute();
            systemAttribute.setName(systemAttributeDto.getName());
            systemAttribute.setValue(systemAttributeDto.getValue());
            systemAttributeList.add(systemAttribute);
        }
        rule.setSystemAttributeList(systemAttributeList);
        ruleRepository.save(rule);
    }

}
