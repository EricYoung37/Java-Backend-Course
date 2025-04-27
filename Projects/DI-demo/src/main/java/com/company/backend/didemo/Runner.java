package com.company.backend.didemo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Runner {

    private final MyComp myComp;

    private MyBean myBean;

    @Autowired
    private MyProto myProto1; // field injects a prototype bean by type

    @Autowired
    private MyProto myProto2; // field injects a prototype bean by type

    @Autowired // constructor injects a singleton bean by name
    public Runner(@Qualifier("myComp") MyComp myComp) {
        this.myComp = myComp;
    }

    @Autowired // setter injects a singleton bean by type
    public void setMyBean(MyBean myBean) {
        this.myBean = myBean;
    }

    @PostConstruct
    public void run() {
        System.out.println("Are MyProto beans the same instance? " + (myProto1 == myProto2));
    }
}