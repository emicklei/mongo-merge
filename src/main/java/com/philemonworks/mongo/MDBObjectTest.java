package com.philemonworks.mongo;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class MDBObjectTest {
    @Test
    public void ace() {
        MDBObject ec = new MDBObject();
        ec.put("a", "a");
        ec.put("c", "c");
        ec.put("e", "e");
        System.out.println(ec);
        Assert.assertEquals("a",ec.get("a"));
        Assert.assertEquals("c",ec.get("c"));
        Assert.assertEquals("e",ec.get("e"));
        Assert.assertNull(ec.get("b"));
        Assert.assertNull(ec.get("f"));
    }
    @Test
    public void cba() {
        MDBObject ec = new MDBObject();
        ec.put("c", "c");
        ec.put("b", "b");
        ec.put("a", "a");
        System.out.println(ec);
    } 
    @Test
    public void merge1() {
        MDBObject one = new MDBObject();
        one.put("c", "c");
        one.put("e", "e");
        MDBObject two = new MDBObject();
        two.put("a", "a");
        two.put("b", "b");
        two.put("f", "f");
        one.merge(two);
        System.out.println(one);
    }   
    @Test
    public void merge2() {
        MDBObject one = new MDBObject();
        MDBObject two = new MDBObject();
        two.put("b", "b");
        two.put("c", 4);
        one.put("b", two);
        MDBObject three = new MDBObject();
        MDBObject four = new MDBObject();
        four.put("a", "a");
        four.put("d", 3.14);
        three.put("b", four);
        
        one.merge(three);
        System.out.println(one);
    }     
    @Test
    public void random() {
        Random rand = new Random();
        MDBObject ec = new MDBObject();
        for (int i=0;i<20;i++) {
            String v = String.valueOf((char)(97 + rand.nextInt(25)));
            ec.put(v, v);
        }
        System.out.println(ec);
    }
}
