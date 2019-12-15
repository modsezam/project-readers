package com.github.magdanadratowska.web;


import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.dao.UserBookDAO;
import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.UserType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user-delete")
public class UserDeleteServlet extends HttpServlet {
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDao = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        boolean isAdmin = UserType.ADMIN.equals(session.getAttribute("userType"));
        long userId = Long.parseLong(req.getParameter("userId"));
        if (isAdmin) {
            try {
                accountDao.deleteUserbooksOfUser(userId);
                userDAO.deleteUserById(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        resp.sendRedirect("/user-list");
    }
}
