package repository;

import domain.Bilet;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BiletDBRepository implements IBiletRepository {

    private JdbcUtils dbUtils;

    public BiletDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
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
    public Bilet create(Bilet entity) {
        //logger.traceEntry("saving task {} ",elem);

        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStnt=con.prepareStatement("insert into bilete (id_meci, nume_client, nr_locuri) values (?,?,?)")) {

            preStnt.setInt(1, entity.getMeci().getId());

            preStnt.setString(2, entity.getNumeClient());

            preStnt.setInt(3, entity.getNrLocuri());

            int result = preStnt.executeUpdate();

            //logger.trace("Saved {} instances", result);

            ResultSet generatedKeys = preStnt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                entity.setId(generatedId);
            }
        }
        catch (SQLException ex) {

            //logger.error(ex);

            System.err.println("Error DB + ex");
            //logger.traceExit();
        }
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
        int numarBilete = 0;
        Connection con = dbUtils.getConnection();

        try {
            PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS numar_bilete FROM bilete");
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                numarBilete = result.getInt("numar_bilete");
            }
        } catch (SQLException ex) {
            System.err.println("Eroare la interogare: " + ex.getMessage());
            ex.printStackTrace();
        }
        return numarBilete;
    }
}
