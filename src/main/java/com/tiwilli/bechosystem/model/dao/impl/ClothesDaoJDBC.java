package com.tiwilli.bechosystem.model.dao.impl;

import com.tiwilli.bechosystem.db.DB;
import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.gui.util.Utils;
import com.tiwilli.bechosystem.model.dao.ClothesDao;
import com.tiwilli.bechosystem.model.entities.Category;
import com.tiwilli.bechosystem.model.entities.Clothes;
import com.tiwilli.bechosystem.model.entities.Sales;
import com.tiwilli.bechosystem.model.entities.enums.ClothesStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothesDaoJDBC implements ClothesDao {

    private Connection conn;

    public ClothesDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Clothes obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                    INSERT INTO clothes
                    (name, size, purchase_value, sales_value, purchase_date, sales_date, post_date, status, category_id, sales_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getSize());
            Utils.trySetPreparedStatementToDouble(st, 3, obj.getPurchaseValue());
            Utils.trySetPreparedStatementToDouble(st, 4, obj.getSalesValue());
            Utils.trySetPreparedStatementToDate(st, 5, obj.getPurchaseDate());
            Utils.trySetPreparedStatementToDate(st, 6, obj.getSalesDate());
            Utils.trySetPreparedStatementToDate(st, 7, obj.getPostDate());
            st.setInt(8, obj.getStatus().getCode());
            st.setInt(9, obj.getCategory().getId());
            Utils.trySetPreparedStatementToInt(st, 10, obj.getSales().getId());

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
    public void update(Clothes obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                            UPDATE clothes
                            SET name = ?, size = ?, purchase_value = ?, sales_value = ?, purchase_date = ?,
                            sales_date = ?, post_date = ?, status = ?, category_id = ?, sales_id = ?
                            WHERE id = ?
                            """);

            st.setString(1, obj.getName());
            st.setString(2, obj.getSize());
            Utils.trySetPreparedStatementToDouble(st, 3, obj.getPurchaseValue());
            Utils.trySetPreparedStatementToDouble(st, 4, obj.getSalesValue());
            Utils.trySetPreparedStatementToDate(st, 5, obj.getPurchaseDate());
            Utils.trySetPreparedStatementToDate(st, 6, obj.getSalesDate());
            Utils.trySetPreparedStatementToDate(st, 7, obj.getPostDate());
            st.setInt(8, obj.getStatus().getCode());
            st.setInt(9, obj.getCategory().getId());
            Utils.trySetPreparedStatementToInt(st, 10, obj.getSales().getId());
            st.setInt(11, obj.getId());

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
                    "DELETE FROM clothes WHERE id = ?");

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
    public Clothes findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT clothes.*, category.name as cat_name
                            FROM clothes
                            INNER JOIN category ON clothes.category_id = category.id
                            WHERE clothes.id = ?
                            """
                    );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Category cat = instantiateCategory(rs);
                return instantiateClothes(rs, cat);
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

    private Clothes instantiateClothes(ResultSet rs, Category cat) throws SQLException {
        Clothes obj = new Clothes();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setSize(rs.getString("size"));
        obj.setPurchaseValue(rs.getDouble("purchase_value"));
        obj.setSalesValue(rs.getDouble("sales_value"));
        obj.setPurchaseDate(Utils.getDateOrNull(rs, "purchase_date"));
        obj.setSalesDate(Utils.getDateOrNull(rs, "sales_date"));
        obj.setPostDate(Utils.getDateOrNull(rs, "post_date"));
        obj.setStatus(ClothesStatus.valueOf(rs.getInt("status")));
        obj.setCategory(cat);
        return obj;
    }

    private Category instantiateCategory(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setId(rs.getInt("category_id"));
        cat.setName(rs.getString("cat_name"));
        return cat;
    }

    @Override
    public List<Clothes> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT clothes.*, category.name as cat_name
                            FROM clothes
                            INNER JOIN category ON clothes.category_id = category.id
                            ORDER BY UPPER(clothes.name)
                            """);

            rs = st.executeQuery();

            List<Clothes> list = new ArrayList<>();
            Map<Integer, Category> map = new HashMap<>();

            while (rs.next()) {

                Category cat = map.get(rs.getInt("category_id"));
                if (cat == null) {
                    cat = instantiateCategory(rs);
                    map.put(rs.getInt("category_id"), cat);
                }

                Clothes obj = instantiateClothes(rs, cat);
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

    @Override
    public List<Clothes> findByCategory(Category category) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                    SELECT clothes.*, category.name as cat_name
                    FROM clothes
                    INNER JOIN category ON clothes.category_id = category.id
                    WHERE category_id = ?
                    ORDER BY UPPER(clothes.name)
                    """);
            st.setInt(1, category.getId());
            rs = st.executeQuery();

            List<Clothes> list = new ArrayList<>();
            Map<Integer, Category> map = new HashMap<>();

            while (rs.next()) {

                Category cat = map.get(rs.getInt("category_id"));
                if (cat == null) {
                    cat = instantiateCategory(rs);
                    map.put(rs.getInt("category_id"), cat);
                }

                Clothes obj = instantiateClothes(rs, cat);
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
