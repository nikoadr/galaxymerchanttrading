package com.galaxymerchantrading.handlingtransaction.dao;

import com.galaxymerchantrading.handlingtransaction.domain.Comodity;
import com.galaxymerchantrading.handlingtransaction.domain.RomanNumericConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class DashoboardService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RomanNumericConfig> get(){
        String sql = "select * from roman_numeric_config";
        try {
            List<RomanNumericConfig> romanNumericConfigs = namedParameterJdbcTemplate.query(sql, new RowMapper<RomanNumericConfig>() {
                @Override
                public RomanNumericConfig mapRow(ResultSet resultSet, int i) throws SQLException {
                    RomanNumericConfig romanNumericConfig = new RomanNumericConfig();
                    romanNumericConfig.setId(resultSet.getInt("id"));
                    romanNumericConfig.setNumCode(resultSet.getString("num_code"));
                    romanNumericConfig.setNumName(resultSet.getString("num_name"));
                    romanNumericConfig.setNumValue(resultSet.getInt("num_value"));
                    return romanNumericConfig;
                }
            });
            return romanNumericConfigs;
        }catch (EmptyResultDataAccessException ex){
            return new ArrayList<RomanNumericConfig>();
        }
    }

    public List<Comodity> getComodity(){
        String sql = "select * from comodity";
        try {
            List<Comodity> comodities = namedParameterJdbcTemplate.query(sql, new RowMapper<Comodity>() {
                @Override
                public Comodity mapRow(ResultSet resultSet, int i) throws SQLException {
                    Comodity comodity = new Comodity();
                    comodity.setId(resultSet.getInt("id"));
                    comodity.setComodityName(resultSet.getString("comodity_name"));
                    comodity.setComodityValue(resultSet.getDouble("comodity_value"));

                    return comodity;
                }
            });
            return comodities;
        }catch (EmptyResultDataAccessException ex){
            return new ArrayList<Comodity>();
        }
    }

}
