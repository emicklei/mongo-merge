package com.philemonworks.mongo;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MTest {
    @Test
    public void wr() throws Exception {
        Mongo mongoDB = new Mongo("localhost", 27017);
        DBCollection collection = mongoDB.getDB("mergeables").getCollection("entries");
        
        BasicDBObject w = new BasicDBObject();
        BasicDBObject w2 = new BasicDBObject();
        w2.put("b", "b");
        w2.put("x", new int[]{64});
        w.put("a", "a");
        w.put("b", w2);
        collection.save(w);
        
        DBObject r = collection.findOne(w);
        System.out.println(r);
        
        collection.setObjectClass(MDBObject.class);
        r = collection.findOne(w);
        System.out.println(r);        
    }
    
}
