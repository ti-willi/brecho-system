package com.tiwilli.bechosystem.model.dao.impl;

import com.tiwilli.bechosystem.db.DB;
import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.model.dao.ClientDao;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.ClientAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDaoJDBC implements ClientDao {

    private Connection conn;

    public ClientDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Client obj) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                    INSERT INTO client
                    (name, phone, email, address_id)
                    VALUES (?, ?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getPhone());
            st.setString(3, obj.getEmail());
            st.setInt(4, obj.getAddress().getId());

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
    public void update(Client obj) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                            UPDATE client
                            SET name = ?, phone = ?, email = ?
                            WHERE id = ?
                            """);

            st.setString(1, obj.getName());
            st.setString(2, obj.getPhone());
            st.setString(3, obj.getEmail());
            st.setInt(4, obj.getId());

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
                    "DELETE FROM client WHERE id = ?");

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
    public Client findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT client.*, client_address.state as addressState,
                            client_address.city as addressCity,
                            client_address.district as addressDistrict,
                            client_address.street as addressStreet,
                            client_address.address_complement as addressComplement,
                            client_address.number as addressNumber,
                            client_address.zip_code as addressZipCode
                            FROM client
                            INNER JOIN client_address ON client.address_id = client_address.id
                            WHERE client.id = ?
                            """
                    );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                ClientAddress address = instantiateClientAddress(rs);
                return instantiateClient(rs, address);
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
    public List<Client> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT client.*, client_address.state as addressState,
                            client_address.city as addressCity,
                            client_address.district as addressDistrict,
                            client_address.street as addressStreet,
                            client_address.address_complement as addressComplement,
                            client_address.number as addressNumber,
                            client_address.zip_code as addressZipCode
                            FROM client
                            INNER JOIN client_address ON client.address_id = client_address.id
                            ORDER BY UPPER(name)
                            """);

            rs = st.executeQuery();

            List<Client> list = new ArrayList<>();
            Map<Integer, ClientAddress> map = new HashMap<>();

            while (rs.next()) {
                ClientAddress address = map.get(rs.getInt("address_id"));

                if (address == null) {
                    address = instantiateClientAddress(rs);
                    map.put(rs.getInt("address_id"), address);
                }

                Client obj = instantiateClient(rs, address);
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
    
    private Client instantiateClient(ResultSet rs, ClientAddress address) throws SQLException {
        Client obj = new Client();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setPhone(rs.getString("phone"));
        obj.setEmail(rs.getString("email"));
        obj.setAddress(address);
        return obj;
    }

    private ClientAddress instantiateClientAddress(ResultSet rs) throws SQLException {
        ClientAddress obj = new ClientAddress();
        obj.setId(rs.getInt("address_id"));
        obj.setState(rs.getString("addressState"));
        obj.setCity(rs.getString("addressCity"));
        obj.setDistrict(rs.getString("addressDistrict"));
        obj.setStreet(rs.getString("addressStreet"));
        obj.setAddressComplement(rs.getString("addressComplement"));
        obj.setNumber(rs.getString("addressNumber"));
        obj.setZipCode(rs.getString("addressZipCode"));
        return obj;
    }
}
