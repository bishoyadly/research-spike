package com.example.demo.dtos;

import com.example.demo.models.LogicalOperation;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RuleKeywordGroupDto {

    Integer keywordGroupId;
    LogicalOperation logicalOperation;
    Integer operationOrder;
}
