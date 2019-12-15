package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/account/books-delete"}, name = "AccountBookDeleteServlet")
public class AccountBookDeleteServlet extends HttpServlet {

    private AccountDAO accountDAO;
    public void init() {
        accountDAO = new AccountDAO();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        deleteBookFromUserList(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void deleteBookFromUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);
        User user = new User(userId);

        long id = Long.parseLong(request.getParameter("id"));
        accountDAO.deleteBookFromUserList(user, id);
//        request.setAttribute("deleted", true);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("../books");
//        dispatcher.forward(request, response);
        response.sendRedirect(request.getHeader("referer"));
    }
}
