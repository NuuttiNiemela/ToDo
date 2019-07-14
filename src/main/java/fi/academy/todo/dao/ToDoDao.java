package fi.academy.todo.dao;

import fi.academy.todo.Tehtava;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class ToDoDao {
    private List<Tehtava> tehtavat;
    private Connection con;
    private String table = "todo";


    public ToDoDao() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tehtavalista", "postgres", "Nuutti123");
        tehtavat = new ArrayList<>();
    }

    public List<Tehtava> haeKaikki() {
        String sql = "SELECT * FROM " + table + " ORDER BY id";
        tehtavat.clear();
        List<Tehtava> haetut = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (ResultSet rs = pstmt.executeQuery(); rs.next() ;) {
                Tehtava t = new Tehtava();
                t.setId(rs.getInt("id"));
                t.setTehtava(rs.getString("tehtava"));
                t.setTarkempiTehtava(rs.getString("tarkempitehtava"));
                t.setValmis(rs.getBoolean("valmis"));
                t.setAikataulu(rs.getTimestamp("aikataulu"));

                haetut.add(t);
                tehtavat.add(t);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return haetut;
    }

    public int lisaa(Tehtava tehtava) {
        String sql = "INSERT INTO " + table + " (tehtava, tarkempitehtava, valmis, aikataulu) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tehtava.getTehtava());
            pstmt.setString(2, tehtava.getTarkempiTehtava());
            pstmt.setBoolean(3, tehtava.isValmis());
            pstmt.setTimestamp(4, new Timestamp(tehtava.getAikataulu().getTime()));
            pstmt.execute();
            tehtavat.add(tehtava);
            return tehtava.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Tehtava poista(int id) {
        String sql = "DELETE FROM " + table + " WHERE id= " + id + ";";
        Tehtava t = null;
        try (PreparedStatement pstmt = con.prepareStatement(sql)){
            for (Tehtava haku : tehtavat) {
                if (haku.getId()==id) t = haku;
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public boolean muuta(int id, Tehtava tehtava) {

        for(Tehtava t : tehtavat) {
            if(t.getId()==id) {
                if(tehtava.getTehtava() != null) t.setTehtava(tehtava.getTehtava());
                if(tehtava.getTarkempiTehtava() != null) t.setTarkempiTehtava(tehtava.getTarkempiTehtava());
                t.setValmis(tehtava.isValmis());
                t.setAikataulu(tehtava.getAikataulu());
                String sql = "UPDATE " + table + " SET valmis = " + t.isValmis() + ", tehtava = '" + t.getTehtava() + "', tarkempitehtava = '" + t.getTarkempiTehtava() + "', aikataulu = '" + t.getAikataulu() + "' WHERE id = " + id + ";";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

}
