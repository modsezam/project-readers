package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet ("/setuser")
public class UserSetTypeToUserServlet extends HttpServlet {
    UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = Long.parseLong(req.getParameter("userId"));
        try {
            userDAO.setUserToUserById(userId);
            resp.sendRedirect("/user-list");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/user-list");
        }
    }
}
