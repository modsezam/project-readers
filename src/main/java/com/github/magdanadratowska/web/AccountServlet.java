package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.dao.UserDAO;
import com.github.magdanadratowska.model.Book;
import com.github.magdanadratowska.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(urlPatterns = "/account", name = "AccountServlet")

public class AccountServlet extends HttpServlet {
    private AccountDAO accountDAO;
    private UserDAO userDAO;

    public void init() {
        accountDAO = new AccountDAO();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            showAccount(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        Book lastBook = accountDAO.getLastReadBook(userId);
        long sumOfReadBooks = accountDAO.sumOfReadUserBooks(userId);
        Optional<User> user = userDAO.getUserById(userId);
        request.setAttribute("lastReadBook", lastBook);
        request.setAttribute("sumOfReadBooks", sumOfReadBooks);
        request.setAttribute("type", user.get().getUserType());
        request.setAttribute("userName", user.get().getUsername());
        request.setAttribute("registerDate", user.get().getRegisterDate());
        request.setAttribute("email", user.get().getEmail());
        RequestDispatcher dispatcher = request.getRequestDispatcher("account.jsp");
        dispatcher.forward(request, response);
    }
}
