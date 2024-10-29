package com.example.demoSpringBoot.controller;

import com.example.demoSpringBoot.service.MyReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyReportController {
    MyReportService myReportService;

    @GetMapping("/gen")
    public ResponseEntity generateReport(@RequestParam("lang") String lang) throws JRException {
        Locale locale = StringUtils.parseLocaleString(lang);
        ByteArrayOutputStream outputStream = myReportService.generateReport(locale);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/design")
    public ResponseEntity designReport() throws JRException {
        ByteArrayOutputStream outputStream = myReportService.designReportByLib();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}
