package io.viren.demo.resource;

import io.viren.demo.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestResource {

    @Autowired
    private Service service;


    @GetMapping("/")
    public String sayHello() {
        log.info("sample log.....!!!");
        return "Hello";
    }

    @GetMapping("/sample")
    public ResponseEntity<Boolean> sample() {
        return  new ResponseEntity<>(service.callOtherService(), HttpStatus.OK);
    }
}
