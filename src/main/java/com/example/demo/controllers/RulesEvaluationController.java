package com.example.demo.controllers;

import com.example.demo.dtos.EmailMessageDto;
import com.example.demo.dtos.SystemAttributeDto;
import com.example.demo.services.RulesEvaluationService;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules-evaluation")
public class RulesEvaluationController {

    private RulesEvaluationService rulesEvaluationService;

    @Autowired
    RulesEvaluationController(RulesEvaluationService rulesEvaluationService) {
        this.rulesEvaluationService = rulesEvaluationService;
    }

    public String getEmailBodyFromFile() {
        String basePath = "D:/LinkDev Work Materials/Avaya Dev Project Materials/Research Spike Materials";
        String filePath = basePath + "/src/main/resources/emails/20MB_email.txt";
//        Scanner scanner = new Scanner(new File(filePath));
//        String body = "";
//        while (scanner.hasNext()) {
//            body += " " + scanner.next();
//        }
//        return body;
        String emailText = null;
        try {
            emailText = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emailText;
    }

    @PostMapping
    public ResponseEntity<List<SystemAttributeDto>> rulesEvaluation(@RequestBody EmailMessageDto emailMessageDto) {
        String txt = getEmailBodyFromFile();
        emailMessageDto.setSubject(txt);
        emailMessageDto.setBody(txt);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<SystemAttributeDto> systemAttributeDtoList = rulesEvaluationService.evaluateSavedRules(emailMessageDto);
        stopWatch.stop();
        System.out.println("------- Rule Evaluation Time Elapsed: " + stopWatch.getTime(TimeUnit.MILLISECONDS) + " ms");
        System.out.println("------- Rule Evaluation Time Elapsed: " + stopWatch.getTime(TimeUnit.SECONDS) + " secs");
        return ResponseEntity.ok(systemAttributeDtoList);
    }
}
