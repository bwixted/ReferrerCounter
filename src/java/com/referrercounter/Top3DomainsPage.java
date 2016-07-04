/*
 * Copyright (c) Bill Wixted.  All rights reserved.  
 * The software in this package is published under the terms of the MIT
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.referrercounter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>Top3DomainsPage</code> is a servlet that displays a web page containing
 * a table that lists the top 3 referrering domains recorded by the service.
 */
@WebServlet(name = "Top3DomainsPage", urlPatterns = {"/Top3DomainsPage"})
public class Top3DomainsPage extends HttpServlet {
    
    private static final int NUM_ITEMS = 3;
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
                
        HtmlHelper htmlHelper = new HtmlHelper(out);
        htmlHelper.printHeaders("Top 3 Domains");
  
        try {
            ReferrerList referrerList = new ReferrerList();
            referrerList.readFromFile();
            Map<String, Integer> sortedMap = referrerList.getMap();
                
            HtmlTable htmlTable = new HtmlTable("Domain","Count");
            Iterator it = sortedMap.entrySet().iterator();

            int cnt=0;
            while (it.hasNext() && (cnt<NUM_ITEMS)) {
                cnt++;
                Map.Entry pair = (Map.Entry)it.next();
                htmlTable.addRow(pair.getKey().toString(), pair.getValue().toString());
            }
            
            if (cnt == 0) {
                htmlTable.addRow("No domains have been recorded.","");
            }

            htmlTable.render(out);
            
        } catch( ClassNotFoundException e) {
            htmlHelper.printMessage("Error reading from file");
        }
        
        htmlHelper.printFooters();
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
