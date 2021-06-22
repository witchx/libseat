package com.libseat.admin;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

@SpringBootTest
class AdminApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test1() {
        Object o = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(o,referenceQueue);
        System.out.println(o);
        System.out.println(referenceQueue.poll());
        System.gc();
        System.out.println(o);
        System.out.println(referenceQueue.poll());
    }

}

