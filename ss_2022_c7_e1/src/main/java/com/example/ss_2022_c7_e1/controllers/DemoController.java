package com.example.ss_2022_c7_e1.controllers;

import com.example.ss_2022_c7_e1.security.Demo4ConditionEvaluator;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class DemoController {

    // PreAuthorize

    @GetMapping("/demo1")
    @PreAuthorize("hasAuthority('read')")
    public String demo1() {
        return "Demo 1";
    }

    @GetMapping("/demo2")
    @PreAuthorize("hasAnyAuthority('write', 'read')")
    public String demo2() {
        return "Demo 2";
    }

    @GetMapping("/demo3/{smth}")
    @PreAuthorize("#something == authentication.name") // authentication object from the SecurityContext
    public String demo3(@PathVariable("smth") String something) {
        return "Demo 3";
    }

    @GetMapping("/demo4")
    @PreAuthorize("@demo4ConditionEvaluator.condition()")
    public String demo4() {
        return "Demo 4";
    }

    // PostAuthorize

    @GetMapping("/demo5/{something}")
    @PostAuthorize("returnObject != 'test'") // is mainly used when we want to restrict the access to some returned value
    public String demo5(@PathVariable String something) {
        System.out.println("Executed!"); // Never uses @PostAuthorize with methods that change data!
        return something;
    }

    // PreFilter => works with either array or Collection

    @GetMapping("/demo6")
    @PreFilter("filterObject.contains('a')")
    public List<String> demo6(@RequestBody List<String> values) {
        System.out.println("Values: " + values);
        return values;
    }

    // PostFilter => the return type must be an array or Collection

    @GetMapping("/demo7")
    @PostFilter("filterObject.contains('a')")
    public List<String> demo7(@RequestBody List<String> values) {
        System.out.println("Values: " + values);
        return values;
    }
}
