package org.example.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JdbcUtils {

    public JdbcUtils() {
        if (url == null) {
//            url = "jdbc:sqlite:../../baschet";
            url = "jdbc:sqlite:C:\\Users\\gabig\\Desktop\\Javra\\mpp-proiect-java-gabriela612\\baschet";
        }
    }

//    @Value("${spring.datasource.url:jdbc:sqlite:../../baschet}")
@Value("${spring.datasource.url:jdbc:sqlite:C:\\Users\\gabig\\Desktop\\Javra\\mpp-proiect-java-gabriela612\\baschet}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

//    private final Properties properties;
//    private static final Logger logger = LogManager.getLogger();

//    public JdbcUtils(Properties properties) {
//        this.properties = properties;
//    }

    private Connection connection = null;

    private Connection getNewConnection() {
//        logger.traceEntry();

        String url = this.url;
        String username = this.username;
        String password = this.password;
        System.out.println(this.url + " acesta este url-ul");

//        logger.info("Trying to connect to url...{}", url);
//        logger.info("Username...{}", username);
//        logger.info("Password...{}", password);

        Connection con = null;
        try {
            if (username == null || password == null) {
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException sqlException) {
//            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        return con;
    }

    public Connection getConnection() {
//        logger.traceEntry();
        try {
            if (connection == null || connection.isClosed()) {
                connection = getNewConnection();
            }
        } catch (SQLException e) {
//            logger.error(e);
            e.printStackTrace();
        }
//        logger.traceExit();
        return connection;
    }
}