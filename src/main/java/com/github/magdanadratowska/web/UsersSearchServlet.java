package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.User;
import com.github.magdanadratowska.model.UserBook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/user-search"}, name = "UsersSearchServlet")
public class UsersSearchServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Optional<String> pageStringOptional = Optional.ofNullable(request.getParameter("page"));
        long page = Long.parseLong(pageStringOptional.orElse("1"));
        Optional<String> queryStringOptional = Optional.ofNullable(request.getParameter("query"));
        String query = queryStringOptional.orElse("");
        long numberOfUsers = userDAO.countAllUsersWithQuery(query);
        long noOfPages = (long) Math.ceil(numberOfUsers / 5.0);
        List<User> users = userDAO.getAllUsersWithQuery(query, (page * 5 - 5), 5);
        request.setAttribute("users", users);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("query", query);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("user-list.jsp").forward(request, response);
    }
}
