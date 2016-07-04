/*
 * Copyright (c) Bill Wixted.  All rights reserved.  
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;


/**
 * <code>ReferrerList</code> Provides for tracking of referrer domains. A map of domains
 * and their counts is maintained and is serialized to and from a data file during 
 * each invocation.
 */
public class ReferrerList implements Serializable {
    
    Map<String,Integer> map = new HashMap();
    final String path = "referrers.lst";
    
    public void addDomainHit(String domain) {
        Integer value = map.get(domain);
        if (value == null) {
            map.put(domain, 1);
        }
        else {
            map.put(domain, value+1);
        }
    }

    public Map<String, Integer> getMap() {

        // return a descending sorted map so the most active domains appear
        // at the top
        
	// convert map to list
	List<Map.Entry<String, Integer>> list = 
		new LinkedList<>(map.entrySet());

        // sort list with comparator, to compare the map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                        return (o2.getValue()).compareTo(o1.getValue());
                }
        });

        // convert sorted list back to a map
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
   
    
    public JsonArray getJsonArray(Integer N) 
    {
        
        // sort the map of domain/value pairs
        Map<String, Integer> sortedMap = getMap();
                
        Iterator it = sortedMap.entrySet().iterator();
        
        JsonArrayBuilder arr = Json.createArrayBuilder();
  
        // return only the top N domains
        int cnt=0;
        while (it.hasNext() && (cnt<N)) {
            cnt++;
            Map.Entry pair = (Map.Entry)it.next();
            String domain = pair.getKey().toString();
            String value = pair.getValue().toString();    
            
            arr.add(Json.createObjectBuilder().
                add("domain",domain).
                add("count",value));
               
        } 
  
        return arr.build();
        
    }
    
    public String findDomainCount(String domain)
    {

        Integer value = map.get(domain);
        if (value == null) {
            return "0";
        }
        return value.toString();
 
    }
    
     void saveToFile() throws IOException {
        File f = new File(path);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(                          
                    new FileOutputStream(f))) {
            oos.writeObject( map );
        }
  
     }
     
     void readFromFile() throws ClassNotFoundException, IOException {
     
        File f = new File(path);
        if (f.exists()){
            String s = f.getAbsolutePath();
            try (ObjectInputStream ois = new ObjectInputStream(                      
                    new FileInputStream(new File(path)))) {
                map = (Map<String,Integer>)ois.readObject();
            }
        }
             
     }
}
