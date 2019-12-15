package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = String.valueOf(req.getSession().getAttribute("userType"));
        boolean isAdmin = userType.equals("ADMIN");
        if (!isAdmin) {
            resp.sendRedirect("/login");
        } else {
            try {
                getUsers(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUsers(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Long numberOfUsers = userDAO.getNummberOfUsers();
        Optional<String> pageOptional = Optional.ofNullable(req.getParameter("page"));
        Long currentPage = Long.parseLong(pageOptional.orElse("1"));
        Long noOfPages = (long) Math.ceil(numberOfUsers / 5.0);
        List<User> allUsersWithPages = userDAO.getUsersWithPages((currentPage * 5) - 5, 5L);
        req.setAttribute("users", allUsersWithPages);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("user-list.jsp").forward(req, resp);
    }
}
