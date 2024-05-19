package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Meci;
import org.example.utils.DateUtils;
import org.example.utils.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MeciDBRepository2 implements IMeciRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger("FisierLog");

    public MeciDBRepository2() {
        logger.info("Initializing MeciDBRepository");
    }

    public MeciDBRepository2(JdbcUtils jdbcUtils) {
//        Properties props=new Properties();
//        try {
//            props.load(new FileReader("../bd.config"));
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config "+e);
//        }
        dbUtils = new JdbcUtils();
        logger.info("Initializing MeciDBRepository with DBUtils: {} ", dbUtils);
    }

    @Autowired
    public void setJdbcUtils(JdbcUtils jdbcUtils) {
        this.dbUtils = jdbcUtils;
    }

    @Override
    public Meci findOne(Integer integer) {
        logger.traceEntry();
        Meci meci = null;
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from meciuri where id=?"))
        {
            logger.info("Prepare Statement : select * from meciuri where id={}", integer);
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String nume = result.getString("nume");
                    Double pretBilet = result.getDouble("pret_bilet");
                    int capacitate = result.getInt("capacitate");
                    LocalDate data = DateUtils.fromString(result.getString("data"));
                    meci = new Meci(nume, pretBilet, capacitate, data);

                    meci.setId(integer);
                    logger.info("Meci gasit : {} ", meci);
                }

            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
                return meci;

            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return meci;
        }
        logger.traceExit(meci);
        return meci;
    }

    @Override
    public Iterable<Meci> findAll() {
        logger.traceEntry();
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();
        List<Meci> meciuri = new ArrayList<>();
        logger.info("Prepare Statement: select * from meciuri");
        try (PreparedStatement preStmt = con.prepareStatement("select * from meciuri")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    Double pretBilet = result.getDouble("pret_bilet");
                    int capacitate = result.getInt("capacitate");
                    LocalDate data = DateUtils.fromString(result.getString("data"));
                    Meci meci = new Meci(nume, pretBilet, capacitate, data);
                    meci.setId(id);
                    logger.info("Meci gasit : {} ", meci);
                    meciuri.add(meci);
                }

            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
                return meciuri;

            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return meciuri;
        }
        logger.traceExit(meciuri);
        return meciuri;
    }

    @Override
    public Iterable<Meci> findMeciuriDisponibile() {
        logger.traceEntry();
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();
        List<Meci> meciuri = new ArrayList<>();
        logger.info("Prepare Statement: SELECT m.id, m.nume, m.pret_bilet, m.capacitate, m.data\n" +
                "FROM meciuri m\n" +
                "         LEFT JOIN (\n" +
                "    SELECT id_meci, SUM(nr_locuri) AS total_locuri\n" +
                "    FROM bilete\n" +
                "    GROUP BY id_meci\n" +
                ") b ON m.id = b.id_meci\n" +
                "WHERE total_locuri < m.capacitate OR total_locuri IS NULL\n" +
                "ORDER BY CASE WHEN total_locuri IS NULL THEN 0 ELSE 1 END;");
        try (PreparedStatement preStmt = con.prepareStatement("SELECT m.id, m.nume, m.pret_bilet, m.capacitate, m.data\n" +
                "FROM meciuri m\n" +
                "         LEFT JOIN (\n" +
                "    SELECT id_meci, SUM(nr_locuri) AS total_locuri\n" +
                "    FROM bilete\n" +
                "    GROUP BY id_meci\n" +
                ") b ON m.id = b.id_meci\n" +
                "WHERE total_locuri < m.capacitate OR total_locuri IS NULL\n" +
                "ORDER BY CASE WHEN total_locuri IS NULL THEN m.capacitate ELSE total_locuri END DESC;")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    Double pretBilet = result.getDouble("pret_bilet");
                    int capacitate = result.getInt("capacitate");
                    LocalDate data = DateUtils.fromString(result.getString("data"));
                    Meci meci = new Meci(nume, pretBilet, capacitate, data);
                    meci.setId(id);
                    logger.info("Meci gasit : {} ", meci);
                    meciuri.add(meci);
                }

            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
                return meciuri;

            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return meciuri;
        }
        logger.traceExit(meciuri);
        return meciuri;
    }

    @Override
    public Meci save(Meci meci) {
        logger.traceEntry("saving meci {}", meci);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "insert into meciuri (nume, pret_bilet, capacitate, data) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            logger.info("Prepare Statement : insert into meciuri (nume, pret_bilet, capacitate, data)");
            preStmt.setString(1, meci.getNume());
            preStmt.setDouble(2, meci.getPretBilet());
            preStmt.setInt(3, meci.getCapacitate());
            preStmt.setString(4, meci.getData().toString());
            int result = preStmt.executeUpdate();
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    meci.setId(generatedKeys.getInt(1));
                }
            }
            logger.info("Meci salvat cu succes : {}", meci);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return null;
        }
        logger.traceExit(meci);
        return meci;
    }

    @Override
    public Meci delete(Integer integer) {
        logger.traceEntry("removing meci with id {}", integer);
        Connection con = dbUtils.getConnection();
        Meci meci = null;

        // Găsim entitatea înainte de a o șterge
        try (PreparedStatement findStmt = con.prepareStatement(
                "select * from meciuri where id=?")) {
            logger.info("Prepare Statement : select * from meciuri where id={}", integer);
            findStmt.setInt(1, integer);
            try (ResultSet result = findStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("nume");
                    Double pretBilet = result.getDouble("pret_bilet");
                    int capacitate = result.getInt("capacitate");
                    LocalDate data = DateUtils.fromString(result.getString("data"));
                    meci = new Meci(nume, pretBilet, capacitate, data);
                    meci.setId(integer);
                    logger.info("Meci găsit pentru ștergere: {}", meci);
                } else {
                    logger.info("Meciul nu a fost găsit pentru ștergere: {}", integer);
                    return null;
                }
            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return null;
        }

        // Ștergem entitatea
        try (PreparedStatement deleteStmt = con.prepareStatement(
                "delete from meciuri where id=?")) {
            logger.info("Prepare Statement : delete from meciuri where id={}", integer);
            deleteStmt.setInt(1, integer);
            int result = deleteStmt.executeUpdate();
            if (result == 0) {
                logger.info("Meciul nu a fost găsit pentru ștergere: {}", integer);
                return null;
            }
            logger.info("Meci șters cu succes: {}", meci);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return null;
        }

        logger.traceExit(meci);
        return meci;
    }

    @Override
    public Meci update(Meci entity) {
        logger.traceEntry("updating meci {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "update meciuri set nume=?, pret_bilet=?, capacitate=?, data=? where id=?")) {
            logger.info("Prepare Statement : update meciuri set nume=?, pret_bilet=?, capacitate=?, data=? where id={}", entity.getId());
            preStmt.setString(1, entity.getNume());
            preStmt.setDouble(2, entity.getPretBilet());
            preStmt.setInt(3, entity.getCapacitate());
            preStmt.setString(4, entity.getData().toString());
            preStmt.setInt(5, entity.getId());
            int result = preStmt.executeUpdate();
            if (result == 0) {
                logger.info("Meciul nu a fost găsit pentru actualizare: {}", entity);
                return null;
            }
            logger.info("Meci actualizat cu succes: {}", entity);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return null;
        }
        logger.traceExit(entity);
        return entity;
    }

    @Override
    public int size() {
        return 0;
    }
}
