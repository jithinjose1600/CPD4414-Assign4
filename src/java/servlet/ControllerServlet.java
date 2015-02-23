/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import db.DBClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author c0648991
 */
@WebServlet("/servlet")
public class ControllerServlet extends HttpServlet{
    
    @Override
    protected  void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        res.setHeader("Content-Type", "text/plain-text");
        
       StringBuilder sb = new StringBuilder();
       String query=null;
        
        try (Connection conn = DBClass.getConnection()) {
            PrintWriter out = res.getWriter();
            PreparedStatement pstmt=null;
            if (!req.getParameterNames().hasMoreElements()) {
                query="SELECT * FROM PRODUCTS";
                pstmt = conn.prepareStatement(query);
            } else {
                int id = Integer.parseInt(req.getParameter("id"));
                query="SELECT * FROM PRODUCTS WHERE ProductId=?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, String.valueOf(id));
            }
            
            ResultSet rs = pstmt.executeQuery();
            if(!rs.wasNull())
            {
              sb.append("[");
            while (rs.next()) {
                sb.append(String.format("{ \"productId\" : "+ rs.getInt("ProductID")+", \"name\" : \""+rs.getString("Name")+"\", \"description\" : \""+rs.getString("Description")+"\", \"quantity\" : "+rs.getInt("Quantity")+" },"));   
            }
            sb.append("]");
            out.println(sb.toString());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
  
}
