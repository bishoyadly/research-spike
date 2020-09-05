package com.example.demo.repositories;

import com.example.demo.models.KeywordGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordGroupRepository extends CrudRepository<KeywordGroup, Integer> {

}
