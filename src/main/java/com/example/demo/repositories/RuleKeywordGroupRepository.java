package com.example.demo.repositories;

import com.example.demo.models.Rule;
import com.example.demo.models.RuleKeywordGroup;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleKeywordGroupRepository extends CrudRepository<RuleKeywordGroup, Integer> {

    public List<RuleKeywordGroup> findByRuleOrderByOperationOrder(Rule rule);
}
