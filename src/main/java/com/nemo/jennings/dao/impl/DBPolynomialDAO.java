package com.nemo.jennings.dao.impl;

import com.nemo.jennings.dao.DBManager;
import com.nemo.jennings.dao.PolynomialDAO;
import com.nemo.jennings.dto.PolynomialDTO;
import com.nemo.jennings.exception.ApplicationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBPolynomialDAO implements PolynomialDAO {

    private String loadAllSql = "select * from polynomial";
    private String loadByNSql = "select * from polynomial where n=?";
    private String loadByMaxNSql = "select * from polynomial where n<?";
    private String loadByMinNSql = "select * from polynomial where n>?";
    private String loadBetweenNSql = "select * from polynomial where n between ? and ?";
    private String loadInSql = "select * from polynomial where n in (?, ?, ?)";

    @Override
    public List<PolynomialDTO> loadByN(short n) {
        return selectWithParams(loadByNSql, n);
    }

    @Override
    public List<PolynomialDTO> loadByMaxN(short n) {
        return selectWithParams(loadByMaxNSql, n);
    }

    @Override
    public List<PolynomialDTO> loadByMinN(short n) {
        return selectWithParams(loadByMinNSql, n);
    }

    @Override
    public List<PolynomialDTO> loadBetweenN(short min, short max) {
        return selectWithParams(loadBetweenNSql, min, max);
    }

    @Override
    public List<PolynomialDTO> loadInN(short n1, short n2, short n3) {
        return selectWithParams(loadInSql, n1, n2, n3);
    }

    @Override
    public List<PolynomialDTO> loadAll() {
        List<PolynomialDTO> list = new ArrayList<>();
        try (Connection con = DBManager.getDBConnection();
             PreparedStatement ps = con.prepareStatement(loadAllSql);
             ResultSet rs = ps.executeQuery()) {
            handle(list, rs);
        } catch (SQLException e) {
            throw new ApplicationException("Error during query execution!", e);
        }
        return list;
    }

    private List<PolynomialDTO> selectWithParams(String sql, short... n) {
        List<PolynomialDTO> list = new ArrayList<>();
        try (Connection con = DBManager.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (n.length != 0) {
                int pos = 1;
                for (short i : n) {
                    ps.setInt(pos++, i);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                handle(list, rs);
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error during query execution!", e);
        }
        return list;
    }

    private void handle(List<PolynomialDTO> list, ResultSet rs) throws SQLException {
        while (rs.next()) {
            PolynomialDTO polynomial = new PolynomialDTO();
            polynomial.setN(rs.getShort("n"));
            polynomial.setJ(rs.getShort("j"));
            polynomial.setOctal(rs.getLong("oct"));
            polynomial.setLetter(rs.getString("letter").charAt(0));
            list.add(polynomial);
        }
    }

}
