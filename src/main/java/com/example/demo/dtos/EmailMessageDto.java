package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMessageDto {

    String subject;
    String body;
    String bodyFormat;
}
