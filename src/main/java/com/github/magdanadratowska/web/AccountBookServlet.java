package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.model.UserBook;

import javax.servlet.RequestDispatcher;
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


@WebServlet(urlPatterns = "/account/books", name = "AccountBookServlet")
public class AccountBookServlet extends HttpServlet {

    private AccountDAO accountDAO;
    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            readUserBooksList(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


    private void readUserBooksList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        List<UserBook> usersBookList = accountDAO.getUsersBookList(userId);
        request.setAttribute("usersBookList", usersBookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../userlist.jsp");
        dispatcher.forward(request, response);
    }

   /* @Deprecated
    private void addBookToUserList(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        String idBookString = request.getParameter("id");
        long bookId;
        try {
            bookId = Long.parseLong(idBookString);
            accountDAO.addBookToUserList(bookId, userId);
            //przejście do strony książki
            RequestDispatcher dispatcher = request.getRequestDispatcher("../list");
            dispatcher.forward(request, response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            //obsługa błędu
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }*/

   /* @Deprecated
    private void removeBookFromUserList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        String idBookString = request.getParameter("id");
        long bookId;
        try {
            bookId = Long.parseLong(idBookString);
            accountDAO.removeBookFromUserList(bookId, userId);
            //przejście do strony książki
            System.out.println("xx>" + request.getContextPath().toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("../list");
            dispatcher.forward(request, response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            //obsługa błędu
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }*/

}
