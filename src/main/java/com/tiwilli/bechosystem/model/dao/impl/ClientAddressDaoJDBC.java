package com.tiwilli.bechosystem.model.dao.impl;

import com.tiwilli.bechosystem.db.DB;
import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.model.dao.ClientAddressDao;
import com.tiwilli.bechosystem.model.entities.ClientAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientAddressDaoJDBC implements ClientAddressDao {

    private Connection conn;

    public ClientAddressDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ClientAddress obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                    INSERT INTO client_address
                    (state, city, district, street, address_complement, number, zip_code)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getState());
            st.setString(2, obj.getCity());
            st.setString(3, obj.getDistrict());
            st.setString(4, obj.getStreet());
            st.setString(5, obj.getAddressComplement());
            st.setInt(6, obj.getNumber());
            st.setString(7, obj.getZipCode());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpeted error! No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(ClientAddress obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                            UPDATE client_address
                            SET state = ?, city = ?, district = ?, street = ?,
                            address_complement = ?, number = ?, zip_code = ?
                            WHERE id = ?
                            """);

            st.setString(1, obj.getState());
            st.setString(2, obj.getCity());
            st.setString(3, obj.getDistrict());
            st.setString(4, obj.getStreet());
            st.setString(5, obj.getAddressComplement());
            st.setInt(6, obj.getNumber());
            st.setString(7, obj.getZipCode());
            st.setInt(8, obj.getId());

            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "DELETE FROM client_address WHERE id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public ClientAddress findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM client_address WHERE id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateClientAddress(rs);
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<ClientAddress> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT * FROM client_address
                            ORDER BY UPPER(state)
                            """);

            rs = st.executeQuery();

            List<ClientAddress> list = new ArrayList<>();

            while (rs.next()) {
                ClientAddress obj = instantiateClientAddress(rs);
                list.add(obj);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private ClientAddress instantiateClientAddress(ResultSet rs) throws SQLException {
        ClientAddress obj = new ClientAddress();
        obj.setId(rs.getInt("id"));
        obj.setState(rs.getString("state"));
        obj.setCity(rs.getString("city"));
        obj.setDistrict(rs.getString("district"));
        obj.setStreet(rs.getString("street"));
        obj.setAddressComplement(rs.getString("address_complement"));
        obj.setNumber(rs.getInt("number"));
        obj.setZipCode(rs.getString("zip_code"));
        return obj;
    }
}
