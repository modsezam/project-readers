package com.github.magdanadratowska.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@WebFilter (urlPatterns = {"/*"}, filterName = "UserLoggedFilter", initParams = {})
public class UserLoggedFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("", "/login", "/books/list", "/user-register")));

    private static final Set<String> ALLOWED_PATHS_CSS_JS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/css/materialize.css", "/css/materialize.min.css",
                    "/js/materialize.js" , "/css/materialize.min.js")));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");

        boolean loggedIn = (session.getAttribute("userName")!=null);
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        boolean allowedPathCssJs = ALLOWED_PATHS_CSS_JS.contains(path);

        if (loggedIn || allowedPath || allowedPathCssJs) {
            filterChain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {

    }
}
