package com.company.backend.didemo;

import org.springframework.stereotype.Component;

@Component
public class MyComp {
    public MyComp() {
        System.out.println("com.example.demo.MyComp: @Component | Singleton");
    }
}