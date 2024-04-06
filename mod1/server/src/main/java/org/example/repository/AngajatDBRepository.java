package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Angajat;
import org.example.utils.*;
import org.example.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AngajatDBRepository implements IAngajatRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger("FisierLog");

    public AngajatDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
        logger.info("Initializing AngajatDBRepository with DBUtils: {} ", dbUtils);
    }

    @Override
    public Meci findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        return null;
    }

    @Override
    public Angajat save(Angajat entity) {
        return null;
    }

    @Override
    public Angajat delete(Integer integer) {
        return null;
    }

    @Override
    public Angajat update(Angajat entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Angajat findByUsername(String username) {
        logger.traceEntry();
        Angajat angajat = null;
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from angajati where username=?"))
        {
            logger.info("Prepare Statement: select * from angajati where username={} ", username);
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String parola = result.getString("parola");
                    angajat = new Angajat(parola, username);
                    angajat.setId(id);
                    logger.info("Angajat gasit : {} ", angajat);
                }

            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
                return angajat;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(angajat);
        return angajat;
    }
}
