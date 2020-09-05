package com.example.demo.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Getter
@Setter
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String ruleName;

    @Enumerated(EnumType.STRING)
    RuleType ruleType;

    @OneToMany(mappedBy = "rule")
    @Cascade(CascadeType.ALL)
    List<RuleKeywordGroup> ruleKeywordGroupList;

    @ManyToMany
    @JoinTable(name = "rule_attributes", joinColumns = @JoinColumn(name = "rule_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    @Cascade(CascadeType.ALL)
    List<SystemAttribute> systemAttributeList;
}
