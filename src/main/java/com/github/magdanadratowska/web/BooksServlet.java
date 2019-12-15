package com.github.magdanadratowska.web;


import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.model.User;
import com.github.magdanadratowska.model.UserBook;

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

@WebServlet(urlPatterns = {"/books"}, name = "BooksServlet")
public class BooksServlet extends HttpServlet {

    private AccountDAO accountDAO;

    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            getBooks(request, response);
        } catch (SQLException e) {
            System.out.println("error");
        }

    }

    private void getBooks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);
        User user = new User(userId);
        long numberOfBooks = accountDAO.countAllBook();
        Optional<String> pageStringOptional = Optional.ofNullable(request.getParameter("page"));
        long page = Long.parseLong(pageStringOptional.orElse("1"));
        long noOfPages = (long) Math.ceil(numberOfBooks / 5.0);
        List<UserBook> userBookList = accountDAO.getAllBookListForCurrentUserWithDeletedBooks(user, (page * 5 - 5), 5);
        request.setAttribute("userBookList", userBookList);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("booklist.jsp").forward(request, response);
    }
}
