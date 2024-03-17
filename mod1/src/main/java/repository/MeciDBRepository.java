package repository;

import domain.Angajat;
import domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DateUtils;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MeciDBRepository implements IMeciRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public MeciDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
        logger.info("Initializing MeciDBRepository with DBUtils: {} ", dbUtils);
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
    public Meci create(Meci entity) {
        return null;
    }

    @Override
    public Meci delete(Integer integer) {
        return null;
    }

    @Override
    public Meci update(Meci entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
