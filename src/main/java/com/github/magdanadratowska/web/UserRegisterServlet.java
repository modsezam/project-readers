package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.Md5Encrypter;
import com.github.magdanadratowska.model.User;
import com.github.magdanadratowska.model.UserType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet ("/user-register")
public class UserRegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    Md5Encrypter md5Encrypter = new Md5Encrypter();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password-repeat");
        HttpSession session = req.getSession();
        session.setAttribute("registerError", null);

        if (password.equals(passwordRepeat)) {

            try {
                String hash = md5Encrypter.encrypt(password);

                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(hash);
                user.setRegisterDate(LocalDateTime.now());
                user.setUserType(UserType.USER);

                userDAO.addUser(user);

                User userFromDB = userDAO.getUserByEmail(email).get();
                session.setAttribute("userId", userFromDB.getId());
                session.setAttribute("userName", userFromDB.getUsername());
                session.setAttribute("userType", userFromDB.getUserType());
                resp.sendRedirect("/account");

            } catch (SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                resp.sendRedirect("/login.jsp");
            }

        } else {
            session.setAttribute("registerError", "repeatPasswordError");
            resp.sendRedirect("/login.jsp");
        }
    }
}
