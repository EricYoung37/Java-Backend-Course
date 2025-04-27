package com.company.backend.didemo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MyProto {
    public MyProto() {
        System.out.println("com.example.demo.MyProto: @Component | Prototype");
    }
}
