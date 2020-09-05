package com.example.demo.services;

import com.example.demo.dtos.KeywordGroupDto;
import com.example.demo.models.Keyword;
import com.example.demo.models.KeywordGroup;
import com.example.demo.repositories.KeywordGroupRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordGroupService {

    private KeywordGroupRepository keywordGroupRepository;

    @Autowired
    KeywordGroupService(KeywordGroupRepository keywordGroupRepository) {
        this.keywordGroupRepository = keywordGroupRepository;
    }

    public void addNewKeywordGroup(KeywordGroupDto keywordGroupDto) {
        KeywordGroup keywordGroup = new KeywordGroup();
        keywordGroup.setGroupName(keywordGroupDto.getGroupName());
        List<Keyword> keywordList = new ArrayList<>();
        StringBuilder regexPattern = new StringBuilder();
        int lastWordIndex = keywordGroupDto.getKeywordList().size() - 1;

        for (int i = 0; i < keywordGroupDto.getKeywordList().size(); i++) {
            String word = keywordGroupDto.getKeywordList().get(i).toLowerCase();
            keywordGroupDto.getKeywordList().set(i, word);
        }

        for (int i = 0; i < keywordGroupDto.getKeywordList().size(); i++) {
            Keyword keyword = new Keyword();
            String word = keywordGroupDto.getKeywordList().get(i);
            keyword.setWord(word);
            keyword.setKeywordGroup(keywordGroup);
            keywordList.add(keyword);
            if (i != lastWordIndex) {
                regexPattern.append(String.format("\\b%s\\b|", word));
            }
        }
        regexPattern.append(String.format("\\b%s\\b", keywordGroupDto.getKeywordList().get(lastWordIndex)));
        keywordGroup.setKeywordList(keywordList);
        keywordGroup.setGroupRegexPattern(regexPattern.toString());
        keywordGroupRepository.save(keywordGroup);
    }
}
