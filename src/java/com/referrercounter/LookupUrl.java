/*
 * Copyright (c) Bill Wixted.  All rights reserved.  
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>LookupUrl</code> Displays a form that allows the user to enter a URL
 * to retrieve the current count of how many times the domain in the URL has
 * been recorded by this service.
 */
@WebServlet(name = "LookupUrl", urlPatterns = {"/LookupUrl"})
public class LookupUrl extends HttpServlet {

    private PrintWriter out = null;
    private HtmlHelper htmlHelper = null;
     
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        
        htmlHelper = new HtmlHelper(out);
        htmlHelper.printHeaders("Lookup Domain Count for URL");
        
        out.println("<div><form name=lookupurl method=post>");
        out.println("<table cellpadding=2 cellspacing=2><tr><td>URL: <input class=url name=url type=text/></td></tr>");
        out.println("<tr><td>Enter a well-formed URL (e.g., \"http://www.acme.com/home.jsp\")</td></tr>");
        out.println("<tr><td align=center><input value=\"Lookup Domain Count\" type='submit' /></td></tr></table>");
        out.println("</form></div>");
        out.println("<div>&nbsp;</div>");
            
        if (request.getMethod().equalsIgnoreCase("post")) {
            handleRequest(request);
        }
         
        htmlHelper.printFooters();
        
    }

    private void handleRequest(HttpServletRequest request) 
    {
            
        String urlString = request.getParameter("url");
        if (urlString == null) {
            return;
        }
        // remove any script tags to avoid cross-site scripting attacks
        urlString = HtmlHelper.removeScriptTags(urlString);
        String domain;
  
        try {
            URL url = new URL(urlString);
            domain = url.getHost();
        }
        catch (MalformedURLException e) {
            htmlHelper.printMessage("The URL is malformed. Please try again!");
            return;
        }
        
        // retrieve the count for the domain
        String domainCount;
        try {
            ReferrerList referrerList = new ReferrerList();
            referrerList.readFromFile();
            domainCount = referrerList.findDomainCount(domain);
        } 
        catch (IOException | ClassNotFoundException e) {
            htmlHelper.printMessage("Error reading from file.");
            return;
        }
            
        // display the results
        HtmlTable htmlTable = new HtmlTable("Item","Value");
        htmlTable.addRow("URL",urlString);
        htmlTable.addRow("Domain",domain);
        htmlTable.addRow("Count",domainCount);
        htmlTable.render(out);
  
    }
 
            
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
