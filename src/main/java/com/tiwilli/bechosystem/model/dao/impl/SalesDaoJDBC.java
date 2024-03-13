package com.tiwilli.bechosystem.model.dao.impl;

import com.tiwilli.bechosystem.db.DB;
import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.dao.ClothesDao;
import com.tiwilli.bechosystem.model.dao.SalesDao;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Client;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesDaoJDBC implements SalesDao {

    private Connection conn;

    public SalesDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Sales obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                    INSERT INTO sales
                    (client_id, sales_date, quantity, total_amount, profit)
                    VALUES (?, ?, ?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, obj.getClient().getId());
            Utils.trySetPreparedStatementToDate(st,2, obj.getSalesDate());
            st.setInt(3, obj.getQuantity());
            Utils.trySetPreparedStatementToDouble(st, 4,obj.getTotalAmount());
            Utils.trySetPreparedStatementToDouble(st, 5, obj.getProfit());

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
    public void update(Sales obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                            UPDATE sales
                            SET client_id = ?, sales_date = ?, quantity = ?, total_amount = ?, profit = ?
                            WHERE id = ?
                            """);

            st.setInt(1, obj.getClient().getId());
            Utils.trySetPreparedStatementToDate(st,2, obj.getSalesDate());
            st.setInt(3, obj.getQuantity());
            Utils.trySetPreparedStatementToDouble(st, 4,obj.getTotalAmount());
            Utils.trySetPreparedStatementToDouble(st, 5, obj.getProfit());

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
                    "DELETE FROM sales WHERE id = ?");

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
    public Sales findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT * FROM sales
                            INNER JOIN client ON sales.client = client.id
                            WHERE sales.id = ?
                            """
                    );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Client client = instantiateClient(rs);
                return instantiateSales(rs, client);
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

    private Sales instantiateSales(ResultSet rs, Client client) throws SQLException {
        Sales obj = new Sales();
        obj.setClient(client);
        obj.setSalesDate(Utils.getDateOrNull(rs, "sales_date"));
        obj.setQuantity(rs.getInt("quantity"));
        return obj;
    }

    private Client instantiateClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        return client;
    }

    @Override
    public List<Sales> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT * FROM sales
                            INNER JOIN client ON sales.client_id = client.id
                            """);

            rs = st.executeQuery();

            List<Sales> list = new ArrayList<>();

            while (rs.next()) {

                Client client = instantiateClient(rs);
                Sales obj = instantiateSales(rs, client);
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

}
