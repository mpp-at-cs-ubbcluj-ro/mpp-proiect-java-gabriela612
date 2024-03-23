package pachet.repository;

import pachet.domain.Bilet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pachet.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BiletDBRepository implements IBiletRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public BiletDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
        logger.info("Initializing MeciDBRepository with DBUtils: {} ", dbUtils);
    }

    @Override
    public Bilet findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        return null;
    }

    @Override
    public Bilet save(Bilet entity) {
        logger.traceEntry("saving bilet {} ", entity);

        logger.info("Getting a connection with db");

        Connection con=dbUtils.getConnection();

        logger.info("Prepare Statement: insert into bilete (id_meci, " +
                "nume_client, nr_locuri) values ({},{},{})", entity.getMeci().getId(),
                entity.getNumeClient(), entity.getNrLocuri());

        try(PreparedStatement preStnt=con.prepareStatement("insert into bilete (id_meci, nume_client, nr_locuri) values (?,?,?)")) {

            preStnt.setInt(1, entity.getMeci().getId());

            preStnt.setString(2, entity.getNumeClient());

            preStnt.setInt(3, entity.getNrLocuri());

            int result = preStnt.executeUpdate();

            logger.trace("Saved {} instances", result);

            ResultSet generatedKeys = preStnt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                entity.setId(generatedId);
            }
        }
        catch (SQLException ex) {

            logger.error(ex);

            System.err.println("Error DB + ex");
        }
        logger.traceExit(entity);
        return entity;
    }

    @Override
    public Bilet delete(Integer integer) {
        return null;
    }

    @Override
    public Bilet update(Bilet entity) {
        return null;
    }

    @Override
    public int size() {
        logger.traceEntry();
        int numarBilete = 0;
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();

        logger.info("Prepare Statement: SELECT COUNT(*) AS numar_bilete FROM bilete");

        try {
            PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS numar_bilete FROM bilete");
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                numarBilete = result.getInt("numar_bilete");
                logger.info("Numar bilete gasit : {} ", numarBilete);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return numarBilete;
        }
        logger.traceExit(numarBilete);
        return numarBilete;
    }

    @Override
    public int nrLocuriOcupateMeci(Integer idMeci) {
        logger.traceEntry();
        int numarBilete = 0;
        logger.info("Getting a connection with db");
        Connection con = dbUtils.getConnection();

        logger.info("Prepare Statement: SELECT SUM(nr_locuri) AS numar_bilete FROM bilete WHERE id_meci={}", idMeci);

        try {
            PreparedStatement preStmt = con.prepareStatement(
                    "SELECT SUM(nr_locuri) AS numar_bilete FROM bilete WHERE id_meci=?");
            preStmt.setInt(1, idMeci);
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                numarBilete = result.getInt("numar_bilete");
                logger.info("Numar bilete gasit : {} ", numarBilete);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            return numarBilete;
        }
        logger.traceExit(numarBilete);
        return numarBilete;
    }
}
