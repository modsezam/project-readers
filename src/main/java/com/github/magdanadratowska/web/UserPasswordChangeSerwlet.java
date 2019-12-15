package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.Md5Encrypter;
import com.github.magdanadratowska.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet ("/passwordChange")
public class UserPasswordChangeSerwlet extends HttpServlet {
    UserDAO userDAO = new UserDAO();
    Md5Encrypter md5Encrypter = new Md5Encrypter();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = Long.parseLong(req.getParameter("userId"));
        try {
            Optional<User> optionalUser = userDAO.getUserById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                req.setAttribute("user", user);
                req.getRequestDispatcher("password-change.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("/user-list");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/user-list");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("userId"));
        String newPassword = req.getParameter("newPassword");
        String repeatNewPassword = req.getParameter("newPasswordRepeat");
        HttpSession session = req.getSession();
        session.setAttribute("changePasswordError", null);


        try {
            Optional<User> optionalUser = userDAO.getUserById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (newPassword.equals("") || repeatNewPassword.equals("")) {
                    session.setAttribute("changePasswordError", "emptyFields");
                    resp.sendRedirect("/passwordChange?userId=" + user.getId());
                } else if (!newPassword.equals(repeatNewPassword)) {
                    session.setAttribute("changePasswordError", "repeatPasswordError");
                    resp.sendRedirect("/passwordChange?userId=" + user.getId());
                } else {
                    String hash = md5Encrypter.encrypt(newPassword);
                    user.setPassword(hash);
                    userDAO.updateUser(user);
                    resp.sendRedirect("/user-list");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/user-list");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            resp.sendRedirect("/user-list");
        }


    }
}
