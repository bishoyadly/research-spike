package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matcher")
public class RegexController {

    @Getter
    @Setter
    private static class RegexRequest {

        String regexPattern;
        String inputText;
    }

    private enum LogicalOperation {
        AND, AND_NOT, OR, OR_NOT
    }

    public List<String> buildKeywordGroup1() {
        List<String> keywordGroup1 = new ArrayList<>();
        keywordGroup1.add("sport");
        keywordGroup1.add("football");
        keywordGroup1.add("champions");
        return keywordGroup1;
    }

    public List<String> buildKeywordGroup2() {
        List<String> keywordGroup2 = new ArrayList<>();
        keywordGroup2.add("technology");
        keywordGroup2.add("virtual reality");
        keywordGroup2.add("games");
        return keywordGroup2;
    }

    public List<String> buildKeywordGroup3() {
        List<String> keywordGroup2 = new ArrayList<>();
        keywordGroup2.add("programming");
        keywordGroup2.add("java");
        keywordGroup2.add("regex");
        return keywordGroup2;
    }

    public String buildGroupRegexPattern(List<String> keywordGroup) {
        String regexPattern = "";
        for (int i = 0; i < keywordGroup.size() - 1; i++) {
            regexPattern += String.format("\\b%s\\b|", keywordGroup.get(i));
        }
        regexPattern += String.format("\\b%s\\b", keywordGroup.get(keywordGroup.size() - 1));
        return regexPattern;
    }

    public Integer keywordGroupMatchingResult(String keywordGroupRegexPattern, String inputText) {
        Pattern pattern = Pattern.compile(keywordGroupRegexPattern);
        Matcher matcher = pattern.matcher(inputText);
        Integer matchingCounter = 0;
        while (matcher.find()) {
            matchingCounter++;
        }
        return matchingCounter;
    }

    public boolean ruleValidator(List<Integer> keywordGroupSizeList, List<Integer> groupMatchingResultList, List<LogicalOperation> logicalOperationList) {

        if (logicalOperationList.isEmpty()) {
            return groupMatchingResultList.get(0) >= keywordGroupSizeList.get(0);
        }
        if (groupMatchingResultList.get(0) < keywordGroupSizeList.get(0)) {
            return false;
        }
        int index = 1;
        for (LogicalOperation logicalOperation : logicalOperationList) {
            if ((logicalOperation.equals(LogicalOperation.AND) && !groupMatchingResultList.get(index).equals(keywordGroupSizeList.get(index))) ||
                (logicalOperation.equals(LogicalOperation.AND_NOT) && groupMatchingResultList.get(index).equals(keywordGroupSizeList.get(index)))
            ) {
                return false;
            }
            index++;
        }
        return true;
    }

    @PostMapping
    public boolean doesMatch(@RequestBody RegexRequest regexRequest) {
        String group1Pattern = buildGroupRegexPattern(buildKeywordGroup1());
        String group2Pattern = buildGroupRegexPattern(buildKeywordGroup2());
        String group3Pattern = buildGroupRegexPattern(buildKeywordGroup3());
        Integer result1 = keywordGroupMatchingResult(group1Pattern, regexRequest.getInputText());
        Integer result2 = keywordGroupMatchingResult(group2Pattern, regexRequest.getInputText());
        Integer result3 = keywordGroupMatchingResult(group3Pattern, regexRequest.getInputText());
        return ruleValidator(Arrays.asList(buildKeywordGroup1().size(), buildKeywordGroup1().size(), buildKeywordGroup1().size()),
            Arrays.asList(result1),
            new ArrayList<>());
    }

    @PutMapping
    public List<String> getMatches(@RequestBody RegexRequest regexRequest) {
        Pattern pattern = Pattern.compile(regexRequest.getRegexPattern());
        Matcher matcher = pattern.matcher(regexRequest.getInputText());
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

}
