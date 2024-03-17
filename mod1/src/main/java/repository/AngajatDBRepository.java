package repository;

import domain.Angajat;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AngajatDBRepository implements IAngajatRepository {

    private JdbcUtils dbUtils;

    public AngajatDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Angajat findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Angajat> findAll() {
        return null;
    }

    @Override
    public Angajat create(Angajat entity) {
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
        //logger.traceEntry();
        Angajat angajat = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from angajati where username=?"))
        {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String parola = result.getString("parola");
                    angajat = new Angajat(parola, username);
                    angajat.setId(id);
                }

            } catch (SQLException e) {
                //logger.error(e);
                System.err.println("Error DB " + e);
                return angajat;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return angajat;
    }
}
