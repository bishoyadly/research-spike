package com.example.demo.services;

import com.example.demo.dtos.EmailMessageDto;
import com.example.demo.dtos.SystemAttributeDto;
import com.example.demo.models.KeywordGroup;
import com.example.demo.models.LogicalOperation;
import com.example.demo.models.Rule;
import com.example.demo.models.RuleKeywordGroup;
import com.example.demo.models.RuleType;
import com.example.demo.models.SystemAttribute;
import com.example.demo.repositories.RuleKeywordGroupRepository;
import com.example.demo.repositories.RuleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RulesEvaluationService {

    private RuleRepository ruleRepository;
    private RuleKeywordGroupRepository ruleKeywordGroupRepository;
    private List<SystemAttributeDto> systemAttributeDtoList;

    @Setter
    @Getter
    static class RuleComponents {

        List<Integer> keywordGroupSizeList;
        List<Integer> groupMatchingResultList;
        List<LogicalOperation> logicalOperationList;

        public RuleComponents(List<Integer> keywordGroupSizeList, List<Integer> groupMatchingResultList, List<LogicalOperation> logicalOperationList) {
            this.keywordGroupSizeList = keywordGroupSizeList;
            this.groupMatchingResultList = groupMatchingResultList;
            this.logicalOperationList = logicalOperationList;
        }
    }

    @Autowired
    RulesEvaluationService(RuleRepository ruleRepository, RuleKeywordGroupRepository ruleKeywordGroupRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleKeywordGroupRepository = ruleKeywordGroupRepository;
        this.systemAttributeDtoList = new ArrayList<>();
    }

    private List<Rule> getAllSavedRules() {
        List<Rule> rulesList = new ArrayList<>();
        for (Rule rule : ruleRepository.findAll()) {
            rulesList.add(rule);
        }
        return rulesList;
    }

    private Integer keywordGroupMatchingResult(String keywordGroupRegexPattern, String inputText) {
        Pattern pattern = Pattern.compile(keywordGroupRegexPattern);
        Matcher matcher = pattern.matcher(inputText.toLowerCase());
        Integer matchingCounter = 0;
        while (matcher.find()) {
            matchingCounter++;
            log.info(matcher.group());
        }
        return matchingCounter;
    }

    private String getEmailTextBasedOnRuleType(RuleType ruleType, EmailMessageDto emailMessageDto) {
        String inputText = "";
        if (ruleType.equals(RuleType.SUBJECT)) {
            inputText = emailMessageDto.getSubject();
        } else if (ruleType.equals(RuleType.BODY)) {
            inputText = emailMessageDto.getBody();
        } else {
            inputText = emailMessageDto.getSubject() + " " + emailMessageDto.getBody();
        }
        return inputText;
    }

    private RuleComponents getRuleComponents(List<RuleKeywordGroup> sortedRuleKeywordGroupList, String inputText) {
        List<Integer> keywordGroupSizeList = new ArrayList<>();
        List<Integer> groupMatchingResultList = new ArrayList<>();
        List<LogicalOperation> logicalOperationList = new ArrayList<>();
        for (int i = 0; i < sortedRuleKeywordGroupList.size(); i++) {
            RuleKeywordGroup ruleKeywordGroup = sortedRuleKeywordGroupList.get(i);
            KeywordGroup keywordGroup = ruleKeywordGroup.getKeywordGroup();
            if (i > 0) {
                logicalOperationList.add(ruleKeywordGroup.getLogicalOperation());
            }
            keywordGroupSizeList.add(keywordGroup.getKeywordList().size());
            Integer matchingResult = keywordGroupMatchingResult(keywordGroup.getGroupRegexPattern(), inputText);
            groupMatchingResultList.add(matchingResult);
        }
        return new RuleComponents(keywordGroupSizeList, groupMatchingResultList, logicalOperationList);
    }

    private boolean ruleValidator(RuleComponents ruleComponents) {
        List<Integer> groupMatchingResultList = ruleComponents.getGroupMatchingResultList();
        List<LogicalOperation> logicalOperationList = ruleComponents.getLogicalOperationList();
        if (logicalOperationList.isEmpty()) {
            return groupMatchingResultList.get(0) > 0;
        }
        if (groupMatchingResultList.get(0) == 0) {
            return false;
        }
        int index = 1;
        for (LogicalOperation logicalOperation : logicalOperationList) {
            if ((logicalOperation.equals(LogicalOperation.AND) && groupMatchingResultList.get(index) == 0) ||
                (logicalOperation.equals(LogicalOperation.AND_NOT) && groupMatchingResultList.get(index) >= 1)
            ) {
                return false;
            }
            index++;
        }
        return true;
    }

    Integer getSystemAttributeIndex(String searchName) {
        for (int i = 0; i < systemAttributeDtoList.size(); i++) {
            String foundName = systemAttributeDtoList.get(i).getName();
            if (foundName.equals(searchName)) {
                return i;
            }
        }
        return -1;
    }

    private void setFinalSystemAttributes(List<SystemAttribute> ruleSystemAttributeList) {
        for (SystemAttribute systemAttribute : ruleSystemAttributeList) {
            Integer attributeIndex = getSystemAttributeIndex(systemAttribute.getName());
            if (attributeIndex == -1) {
                SystemAttributeDto systemAttributeDto = new SystemAttributeDto();
                systemAttributeDto.setName(systemAttribute.getName());
                systemAttributeDto.setValue(systemAttribute.getValue());
                systemAttributeDtoList.add(systemAttributeDto);
            } else {
                systemAttributeDtoList.get(attributeIndex).setValue(systemAttribute.getValue());
            }
        }
    }

    public List<SystemAttributeDto> evaluateSavedRules(EmailMessageDto emailMessageDto) {
        systemAttributeDtoList.clear();
        List<Rule> ruleList = getAllSavedRules();
        for (Rule rule : ruleList) {
            List<RuleKeywordGroup> sortedRuleKeywordGroupList = ruleKeywordGroupRepository.findByRuleOrderByOperationOrder(rule);
            String emailText = getEmailTextBasedOnRuleType(rule.getRuleType(), emailMessageDto);
            RuleComponents ruleComponents = getRuleComponents(sortedRuleKeywordGroupList, emailText);
            boolean isValid = ruleValidator(ruleComponents);
            if (isValid) {
                setFinalSystemAttributes(rule.getSystemAttributeList());
            }
        }
        return systemAttributeDtoList;
    }

}
