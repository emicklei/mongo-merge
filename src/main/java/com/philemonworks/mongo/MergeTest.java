package com.philemonworks.mongo;

import java.util.Arrays;

import org.junit.Test;

public class MergeTest {
    @Test
    public void doit() {
        MDBObject one = new MDBObject();
        for (int i=0;i<26;i=i+2) {
            String k = String.valueOf((char)(97+i));
            if (i<13)
                one.put(k, k);
            else {
                one.put(k, Arrays.asList(k));
            }            
        }
        System.out.println(one);
        MDBObject two = new MDBObject();
        for (int i=1;i<26;i=i+2) {
            String k = String.valueOf((char)(97+i));
            if (i<13)
                two.put(k, k);
            else {
                two.put(k, Arrays.asList(k));
            }            
        }        
        System.out.println(two);
        one.merge(two);
        System.out.println(one);
        one.merge(two);
        System.out.println(one);
    }
}
