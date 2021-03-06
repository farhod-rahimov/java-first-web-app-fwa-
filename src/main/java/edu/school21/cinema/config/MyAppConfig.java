package edu.school21.cinema.config;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.cinema.repositories.*;
import edu.school21.cinema.services.UsersService;
import edu.school21.cinema.services.UsersServiceException;
import edu.school21.cinema.services.UsersServiceExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"edu.school21.cinema.repositories", "edu.school21.cinema.services"})
@PropertySource("file:${webapp.root}/WEB-INF/application.properties")
public class MyAppConfig {

    private Environment env;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.login}")
    private String dbLogin;

    @Value("${db.password}")
    private String dbPassword;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean
    @Scope("singleton")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        try {
            Class.forName("org.postgresql.Driver");
            dataSource.setJdbcUrl(dbUrl);
            dataSource.setUsername(dbLogin);
            dataSource.setPassword(dbPassword);
        } catch (ClassNotFoundException e) {
            throw new UsersServiceException(UsersServiceExceptionEnum.JDBC_NOT_FOUND);
        }
        return dataSource;
    }

    @Bean
    @Scope("singleton")
    public UsersRepository usersRepository() {
        return new UsersRepositoryJdbcImpl(new JdbcTemplate(dataSource()));
    }

    @Bean
    @Scope("singleton")
    public AuthenticationsRepository authenticationsRepository() {
        return new AuthenticationsRepositoryJdbcImpl(new JdbcTemplate(dataSource()));
    }

    @Bean
    @Scope("singleton")
    public ImagesRepository imagesRepository() {
        return new ImagesRepositoryJdbcImpl(new JdbcTemplate(dataSource()));
    }

    @Bean
    @Scope("singleton")
    public UsersService service() {
        return new UsersService();
    }

    @Bean
    @Scope("singleton")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
