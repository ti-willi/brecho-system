package com.tiwilli.bechosystem.model.dao.impl;

import com.tiwilli.bechosystem.db.DB;
import com.tiwilli.bechosystem.db.DbException;
import com.tiwilli.bechosystem.db.DbIntegrityException;
import com.tiwilli.bechosystem.model.dao.CategoryDao;
import com.tiwilli.bechosystem.model.entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoJDBC implements CategoryDao {

    private Connection conn;

    public CategoryDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Category obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                    INSERT INTO category
                    (name) VALUES (?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());

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
    public void update(Category obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("""
                            UPDATE category
                            SET name = ? WHERE id = ?
                            """);

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

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
                    "DELETE FROM category WHERE id = ?");

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
    public Category findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM category WHERE id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Category obj = new Category();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                return obj;
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
    public List<Category> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT * FROM category
                            ORDER BY UPPER(name)
                            """);

            rs = st.executeQuery();

            List<Category> list = new ArrayList<>();

            while (rs.next()) {
                Category obj = new Category(rs.getInt("id"), rs.getString("name"));
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
    public List<Category> findByName(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                            SELECT * FROM category
                            WHERE LOWER(category.name) LIKE LOWER(CONCAT('%', ?, '%'))
                            """);

            st.setString(1, name);
            rs = st.executeQuery();

            List<Category> list = new ArrayList<>();

            while (rs.next()) {
                Category obj = new Category(rs.getInt("id"), rs.getString("name"));
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
