package com.philemonworks.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class MEntry implements Comparable<MEntry> {
    public String field;
    public Object value;
    public MEntry(String f,Object v) { field = f; value = v; }
    @Override
    public int compareTo(MEntry e) {
        return this.field.compareTo(e.field);
    }
    public String toString() { 
        StringBuffer b = new StringBuffer(64);
        b.append('"').append(field).append("\" : ");
        if (value instanceof String) {
            b.append('"').append(value).append('"');
        } else if (value instanceof ObjectId) {
            b.append("ObjectId(").append(value).append(')');
        } else {
            b.append(value);
        }
        return b.toString();
    }

    /**
     * If both values are documents, then perform document merge
     * If both values are arrays, then perform list merge
     * Else overwrite value
     * @param other
     * @return
     */
    public MEntry merge(MEntry other) {
        if (value instanceof MDBObject && other.value instanceof MDBObject) {
            ((MDBObject) value).merge((MDBObject)other.value);
        } else if (value instanceof List<?> && other.value instanceof List<?>) {
            this.value = this.merge((List)value, (List)(other.value));
        } else {
            this.value = other.value;
        }
        return this;
    }
    private List merge(List myList, List otherList) {
        ArrayList newList = new ArrayList(otherList.size());
        for (int j=0;j<myList.size();j++) {
            Object each = myList.get(j);
            Object other = otherList.get(j);
            Object newValue = null;
            if (each instanceof MDBObject && other instanceof MDBObject) {
                ((MDBObject) each).merge((MDBObject)other);
                newValue = each;
            } else if (each instanceof List<?> && other instanceof List<?>) {
                newValue = this.merge((List)each, (List)(other));
            } else {
                newValue = other;
            }
            newList.add(newValue);
        }
        // remainder
        for (int k=myList.size(); k < otherList.size(); k++) {
            newList.add(otherList.get(k));
        }
        return newList;
    }
}
