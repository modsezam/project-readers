package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.BookDAO;
import com.github.magdanadratowska.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/books-add"}, name = "BookAddServlet")
public class BookAddServlet extends HttpServlet {

    BookDAO bookDAO;
    @Override
    public void init() {
        bookDAO = new BookDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setAuthorName(request.getParameter("author_name"));
        book.setAuthorSurname(request.getParameter("author_surname"));
        bookDAO.addBook(book);
        response.sendRedirect("/books");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idBook = null;
        Book book = new Book(idBook);
        request.setAttribute("book", book);
        request.getRequestDispatcher("book-form.jsp").forward(request, response);
    }
}
