package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.AccountDAO;
import com.github.magdanadratowska.model.User;
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

@WebServlet(urlPatterns = {"/userlist/list", "/userlist/delete"}, name = "UsersListServlet")

public class UsersListServlet extends HttpServlet {

    private AccountDAO accountDAO;

    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String action = request.getServletPath();
        try {
            switch (action) {
                case "/userlist/list":
                    userlist(request, response);
                    break;
                case "/userlist/delete":
                    deleteBookFromUserList(request, response);
                    break;
                case "/userlist/todelete":
                    wantToDeleteBook(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }


    }

    private void wantToDeleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        request.setAttribute("title", title);
        request.setAttribute("id", id);
        request.setAttribute("todelete", true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../userlist/list");
        dispatcher.forward(request, response);
    }

    private void deleteBookFromUserList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = new User();
        user.setId(1L);
        long id = Long.parseLong(request.getParameter("id"));
        accountDAO.deleteBookFromUserList(user, id);
        request.setAttribute("deleted", true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../userlist/list");
        dispatcher.forward(request, response);
    }

    private void userlist(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        HttpSession session = request.getSession();
        Optional<Object> objectUserId = Optional.ofNullable(session.getAttribute("userId"));
        long userId = objectUserId.map(o -> Long.parseLong(o.toString())).orElse(0L);

        List<UserBook> usersBookList = accountDAO.getUsersBookList(userId);
        request.setAttribute("usersBookList", usersBookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../userlist.jsp");
        dispatcher.forward(request, response);
    }
}
