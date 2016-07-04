/*
 * Copyright (c) Bill Wixted.  All rights reserved. 
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>HtmlHelper</code> Class that provides reusable HTML generation
 * used for the web pages in the service.
 * 
 */
public class HtmlHelper {
    
    private final PrintWriter out;
    
    public HtmlHelper(PrintWriter out) {
        this.out = out;
    }
    
    public void printHeaders(String title) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<style media=screen type=text/css>");
        out.println("div { margin-top:1px; margin-bottom:1px; margin-left:10px; margin-right:1px; text-align:left; width:800px; }");
        out.println("h2 { margin: 10px }");
        out.println("h3 { margin-top:30px; margin-bottom:30px; }");
        out.println("body { font-family: tahoma,arial; font-size: 14px; color: #000000; background-color: #FFFFFF; }");
        out.println("table { border: 2px solid}");
        out.println("th { background-color: silver }");
        out.println(".url { width: 400px }");
        out.println(".outer { color: #FFFFFF; width: 100% }");
        out.println(".inner {  color: #000000; width: 100% }");
        out.println(".wt { color: #666600; font-size: 100%; font-weight: normal; background-color: #FFFFFF; text-decoration: none; }");
        out.println(".gy { color: #666600; font-size: 100%; font-weight: normal; background-color:#EEEEEE; text-decoration: none; }");
        out.println(".code { font-family: courier; white-space: nowrap; }");
        out.println("</style>");
        out.println("<title>ReferrerCounter</title>");            
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>" + title + "</h2>");
    }
    
    public void printFooters() {
        out.println("</body>");
        out.println("</html>");
    }
    
    public void printMessage(String message) {
        out.println("<div>" + message + "</div>");
    }
    
    public static String removeScriptTags(String message) {
      String scriptRegex = "<(/)?[ ]*script[^>]*>";
      Pattern pattern2 = Pattern.compile(scriptRegex);

      if(message != null) {
            Matcher matcher2 = pattern2.matcher(message);
            StringBuffer str = new StringBuffer(message.length());
            while(matcher2.find()) {
              matcher2.appendReplacement(str, Matcher.quoteReplacement(" "));
            }
            matcher2.appendTail(str);
            message = str.toString();
      }
     return message;
}
    
}
