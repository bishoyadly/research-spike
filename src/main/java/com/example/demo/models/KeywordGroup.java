package com.example.demo.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Getter
@Setter
public class KeywordGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String groupName;

    private String groupRegexPattern;

    @OneToMany(mappedBy = "keywordGroup")
    @Cascade(value = CascadeType.ALL)
    private List<Keyword> keywordList;

    @OneToMany(mappedBy = "keywordGroup")
    private List<RuleKeywordGroup> ruleKeywordGroupList;
}
