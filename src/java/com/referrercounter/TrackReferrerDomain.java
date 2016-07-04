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
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * <code>TrackReferrerDomain</code> is a servlet that records the domain of a 
 * referring URL passed to it. It will also send back a JSON response
 * containing a message about any errors that occurred or an "OK" status.
 */
@WebServlet(name = "TrackReferrerDomain", urlPatterns = {"/TrackReferrerDomain"})
public class TrackReferrerDomain extends HttpServlet {

    private PrintWriter out;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
   
        out = response.getWriter();

        JsonObject obj = handleRequest(request,response);
        out.write(obj.toString());
        
    }
    
    private JsonObject handleRequest(HttpServletRequest request, HttpServletResponse response)         
    {
        JsonObjectBuilder b = Json.createObjectBuilder();
       
        // check if the "url" parameter is missing
        String urlString = request.getParameter("url");
        if (urlString == null) {     
            response.setStatus(400); // indicate bad request
            b.add( "response", "Missing parameter: url");                
            return b.build();
        }
        
        // remove any script tags to avoid cross-site scripting attacks
        urlString = HtmlHelper.removeScriptTags(urlString);
        String domain;
  
        // extract the domain from the URL
        try {
            URL url = new URL(urlString);
            domain = url.getHost();      
        }
        catch (MalformedURLException e) {
            response.setStatus(400); // indicate bad request
            b.add( "response", "Malformed parameter: url");                
            return b.build();
        }
        
        // record the referring domain
        try {
            ReferrerList referrerList = new ReferrerList();
            referrerList.readFromFile();
            referrerList.addDomainHit(domain);
            referrerList.saveToFile();
        } catch (IOException | ClassNotFoundException e) {
            return Json.createObjectBuilder().add("response",e.getLocalizedMessage()).build();
        } 
        b.add( "response", "OK");      
        return b.build();
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
