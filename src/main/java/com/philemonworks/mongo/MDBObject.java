package com.philemonworks.mongo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

public class MDBObject implements DBObject {
    private boolean _isPartialObject = false;

    ArrayList<MEntry> sorted = new ArrayList<MEntry>(64);

    public void merge(MDBObject other) {
        int mySize = sorted.size();
        if (mySize == 0) {
            sorted = other.sorted;
            return;
        }
        int hisSize = other.sorted.size();
        if (hisSize == 0) {
            return;
        }
        ArrayList<MEntry> merged = new ArrayList<MEntry>(mySize + hisSize);
        int m = 0;
        int h = 0;
        while (m < mySize && h < hisSize) {
            MEntry me = sorted.get(m);
            MEntry he = other.sorted.get(h);
            int sign = me.compareTo(he);
            if (sign < 0) {
                merged.add(me);
                m++;
            } else if (sign == 0) {
                merged.add(me.merge(he));
                m++;
                h++;
            } else { // sign > 0
                merged.add(he);
                h++;
            }
        }
        while (m < mySize) {
            merged.add(sorted.get(m++));
        }
        while (h < hisSize) {
            merged.add(other.sorted.get(h++));
        }
        this.sorted = merged;
    }

    public Object get(String f) {
        int low = 0;
        int high = sorted.size();
        while (true) {
            int mid = (high + low) / 2;
            MEntry v = sorted.get(mid);
            int sign = f.compareTo(v.field);
            if (sign < 0) {
                if (high == mid) {
                    break;
                } else {
                    high = mid;
                }
            } else if (sign == 0) {
                return v.value;
            } else {
                if (low == mid) {
                    break;
                } else {
                    low = mid;
                }
            }
        }
        return null;
    }

    public Object put(String f, Object v) {
        MEntry insert = new MEntry(f, v);
        boolean swapping = false;
        for (int i = 0; i < sorted.size(); i++) {
            MEntry each = sorted.get(i);
            if (swapping) {
                sorted.set(i, insert);
                insert = each;
            } else {
                int sign = insert.compareTo(each);
                if (sign < 0) { // before each
                    swapping = true;
                    sorted.set(i, insert);
                    insert = each;
                } else if (sign == 0) { // replace
                    sorted.set(i, insert);
                    return v;
                }
            }
        }
        sorted.add(insert);
        return v;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("{ ");
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0)
                b.append(" , ");
            MEntry each = sorted.get(i);
            b.append(each);
        }
        b.append('}');
        return b.toString();
    }

    @Override
    public void putAll(BSONObject o) {
        for (String each : o.keySet()) {
            this.put(each, o.get(each));
        }
    }

    @Override
    public void putAll(Map m) {
        for (Object each : m.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) each;
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map toMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(this.sorted.size());
        for (int i = 0; i < sorted.size(); i++) {
            MEntry each = sorted.get(i);
            map.put(each.field, each.value);
        }
        return map;
    }

    @Override
    public Object removeField(String key) {
        for (int i = 0; i < sorted.size(); i++) {
            MEntry each = sorted.get(i);
            if (each.field.equals(key)) {
                sorted.remove(i);
                return each.value;
            }
        }
        return null;
    }

    @Override
    @Deprecated
    public boolean containsKey(String s) {
        return this.containsField(s);
    }

    @Override
    public boolean containsField(String s) {
        for (int i = 0; i < sorted.size(); i++) {
            MEntry each = sorted.get(i);
            if (each.field.equals(s))
                return true;
        }
        return false;
    }

    @Override
    public Set<String> keySet() {
        HashSet<String> keys = new HashSet<String>(this.sorted.size());
        for (int i = 0; i < sorted.size(); i++) {
            keys.add(sorted.get(i).field);
        }
        return keys;
    }

    @Override
    public void markAsPartialObject() {
        _isPartialObject = true;
    }

    @Override
    public boolean isPartialObject() {
        return _isPartialObject;
    }
}
