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
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/account/books-add"}, name = "AccountBookAddServlet")
public class AccountBookAddServlet extends HttpServlet {

    AccountDAO accountDAO;
    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addBookToUserList(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void addBookToUserList(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        User user = new User(userId);

        String idBookString = request.getParameter("id");
        Optional<Object> objectIsOwned = Optional.ofNullable(request.getParameter("isOwned"));
        long bookId;
        try {
            bookId = Long.parseLong(idBookString);
            if (objectIsOwned.isPresent()){
                accountDAO.restoreBookToUserList(user, bookId);
            } else {
                accountDAO.addBookToUserList(bookId, userId);
            }
//            request.getRequestDispatcher("../books").forward(request, response);

            response.sendRedirect(request.getHeader("referer"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            //obsługa błędu
        }
    }
}
