package com.github.magdanadratowska.web;

import com.github.magdanadratowska.dao.BookDAO;
import com.github.magdanadratowska.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/books-edit"}, name = "BookEditServlet")
public class BookEditServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        updateBook(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long idBook;
        try {
            idBook = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("/books");
            return;
        }
        Optional<Book> bookOptional = bookDAO.findBookById(idBook);
        bookOptional.ifPresent(book -> request.setAttribute("book", book));
        request.getRequestDispatcher("book-form.jsp").forward(request, response);
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long bookId = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String authorName = request.getParameter("author_name");
        String authorSurname = request.getParameter("author_surname");
        bookDAO.updateBook(bookId, title, authorName, authorSurname);
        response.sendRedirect(request.getHeader("referer"));
    }

}
