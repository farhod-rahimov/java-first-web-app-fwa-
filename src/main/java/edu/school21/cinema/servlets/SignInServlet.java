package edu.school21.cinema.servlets;

import edu.school21.cinema.repositories.AuthenticationsRepository;
import edu.school21.cinema.repositories.UsersRepository;
import edu.school21.cinema.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/signIn")
public class SignInServlet extends HttpServlet {

    private ApplicationContext myAppContext;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        myAppContext = (ApplicationContext)servletContext.getAttribute("myAppContext");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher reqRequestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/secure/SignIn.jsp");
        reqRequestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersService usersService = myAppContext.getBean(UsersService.class);
        UsersRepository usersRepository = myAppContext.getBean(UsersRepository.class);
        AuthenticationsRepository authRepository = myAppContext.getBean(AuthenticationsRepository.class);
        PasswordEncoder passwordEncoder = myAppContext.getBean(PasswordEncoder.class);

        usersService.signIn(req, resp, usersRepository, authRepository, passwordEncoder);
    }
}
