package com.example.resttempl.controller;


import com.example.resttempl.dto.User;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/vic")
public class MainRestController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL = "http://91.241.64.178:7081/api/users";

    private static String cookie;

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        HttpHeaders headers = response.getHeaders();
        cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        ResponseEntity<String> str1 = createUser();
        ResponseEntity<String> str2 = update();
        ResponseEntity<String> str3 = delete(3);
        System.out.println(str1.getBody()+str2.getBody()+str3.getBody());

        return response.getBody();
    }


    public ResponseEntity<String> createUser() {
        User user = new User();
        long id = 3;
        user.setId(id);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte)100);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL,HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> update() {
        User user = new User();
        long id = 3;
        user.setId(id);
        user.setName("Thomas");
        user.setLastName("Shelby");
        byte age = 100;
        user.setAge(age);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<String> delete(long id) {
        String url = URL + "/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url,HttpMethod.DELETE, entity, String.class);
    }
}
