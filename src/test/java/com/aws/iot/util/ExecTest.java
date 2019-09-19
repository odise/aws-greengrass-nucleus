/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0 */


package com.aws.iot.util;

import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;

import static org.junit.Assert.*;


public class ExecTest {
    @Test
    public void test() {
        if (Exec.isWindows)
            return;
        String s = Exec.cmd("pwd");
//        System.out.println("pwd: "+s);
        assertFalse(s.contains("\n"));
        assertTrue(s.startsWith("/"));
        assertEquals(s,Exec.sh("pwd"));
        String s2 = Exec.sh("ifconfig -a;echo Hello");
//        System.out.println(s2);
        assertTrue(s2.contains("Hello"));
    }
    
    @Test
    public void test2() {
//        System.out.println(Exec.sh("printenv;java --version"));
//        assertFalse(Exec.successful("java --version|egrep -i -q '(jdk|jre) *17\\.'"));
//        assertTrue(Exec.successful("java --version|egrep -i -q '(jdk|jre) *11\\.'"));
        assertFalse(Exec.successful(false, "echo openjdk 11.0|egrep -i -q '(jdk|jre) *18\\.'"));
        assertTrue(new Exec().withShell("echo openjdk 11.0|egrep -i -q '(jdk|jre) *11\\.'")
                .withDumpOut().successful(false));
    }
    
    @Test
    public void test3() {
        CountDownLatch done = new CountDownLatch(1);
        List<String> o = new ArrayList<>();
        List<String> e = new ArrayList<>();

        new Exec().withShell("pwd")
                .withOut(str->o.add(str.toString()))
                .withErr(str->e.add(str.toString()))
                .background(exc -> done.countDown());
        try {
            done.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
            fail();
        }
//        System.out.println("O: "+deepToString(o));
//        System.out.println("E: "+deepToString(e));
        assertEquals(0, e.size());
        assertEquals(1, o.size());
        assertTrue(o.get(0).startsWith("/"));
    }

}