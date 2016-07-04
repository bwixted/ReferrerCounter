/*
 * Copyright (c) Bill Wixted.  All rights reserved. 
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <code>HtmlTable</code> Generates a formatted HTML table with two columns
 * for each row. The rows alternate color between gray and white. You can provide 
 * any two strings to populate the cells of each row.
 */
public class HtmlTable {
    
    private final LinkedHashMap<String,String> map = new LinkedHashMap();
    private final String column1;
    private final String column2;
    
    public HtmlTable(String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
    }
    
    public void render(PrintWriter out)
    {
        out.println("<div>");
        out.println("<table class=outer cellpadding=2 cellspacing=0>");
        out.println("<table class=inner cellpadding=3 cellspacing=0>");
        out.println("<tr><th>" + column1 + "</th><th>" + column2 + "</th></tr>");
        Iterator it = map.entrySet().iterator();
        int cntr=0;
        while (it.hasNext()) {
            String style = ((cntr%2)==0)? "wt" : "gy";
   
            Map.Entry pair = (Map.Entry)it.next();
            out.println("<tr class=" + style + "><td>" + pair.getKey() + "</td><td>" + pair.getValue() + "</td></tr>");
            cntr++;
        }
        out.println("</table></table></div>");

    }
    
    public void addRow(String item,String value) {
        map.put(item, value);
    }
}
