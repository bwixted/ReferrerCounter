/*
 * Copyright (c) Bill Wixted.  All rights reserved. 
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>AllDomains</code> displays a web page showing all of the referring
 * domains so far tracked by the service.
 */
@WebServlet(name = "AllDomains", urlPatterns = {"/AllDomains"})
public class AllDomains extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        HtmlHelper htmlHelper = new HtmlHelper(out);
                
        htmlHelper.printHeaders("All Domains");
               
        try {
  
            ReferrerList referrerList = new ReferrerList();
            referrerList.readFromFile();     
            
            Map<String, Integer> map = referrerList.getMap();
                
            HtmlTable htmlTable = new HtmlTable("Domain","Count");
            for (Map.Entry pair : map.entrySet()) {
                htmlTable.addRow(pair.getKey().toString(), pair.getValue().toString());
            }
            
            if (map.isEmpty()) {
                htmlTable.addRow("No domains have been recorded.","");
            }
            
            htmlTable.render(out);
            
        }
        catch(ClassNotFoundException e) {
            htmlHelper.printMessage("Error reading from file");
        }
        
        htmlHelper.printFooters();
        
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
