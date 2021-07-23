package com.galaxymerchantrading.handlingtransaction.dao;

import com.galaxymerchantrading.handlingtransaction.domain.Comodity;
import com.galaxymerchantrading.handlingtransaction.domain.IntergalacticUnitConfig;
import com.galaxymerchantrading.handlingtransaction.domain.RomanNumericConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
@Slf4j
public class DashoboardService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String insertIntergalacticUnitConfig = "INSERT INTO intergalactic_unit_config(intergalactic_unit_name,roman_numeral) VALUES (?,?)";
    private static final String insertComodity = "INSERT INTO comodity(comodity_name,comodity_value) VALUES (?,?)";

    public List<RomanNumericConfig> get(){
        String sql = "select * from roman_numeric_config";
        try {
            List<RomanNumericConfig> romanNumericConfigs = namedParameterJdbcTemplate.query(sql, new RowMapper<RomanNumericConfig>() {
                @Override
                public RomanNumericConfig mapRow(ResultSet resultSet, int i) throws SQLException {
                    RomanNumericConfig romanNumericConfig = new RomanNumericConfig();
                    romanNumericConfig.setId(resultSet.getInt("id"));
                    romanNumericConfig.setNumCode(resultSet.getString("num_code"));
                    romanNumericConfig.setNumValue(resultSet.getInt("num_value"));
                    return romanNumericConfig;
                }
            });
            return romanNumericConfigs;
        }catch (EmptyResultDataAccessException ex){
            return new ArrayList<RomanNumericConfig>();
        }
    }

    public List<IntergalacticUnitConfig> getIntergalacticUnit(){
        String sql = "select * from intergalactic_unit_config";
        try {
            List<IntergalacticUnitConfig> unitConfigs = namedParameterJdbcTemplate.query(sql, new RowMapper<IntergalacticUnitConfig>() {
                @Override
                public IntergalacticUnitConfig mapRow(ResultSet resultSet, int i) throws SQLException {
                    IntergalacticUnitConfig intergalacticUnitConfig = new IntergalacticUnitConfig();
                    intergalacticUnitConfig.setInterGalacticUnitName(resultSet.getString("intergalactic_unit_name"));
                    intergalacticUnitConfig.setRomanNumeral(resultSet.getString("roman_numeral"));
                    return intergalacticUnitConfig;
                }
            });
            return unitConfigs;
        }catch (EmptyResultDataAccessException ex){
            return new ArrayList<IntergalacticUnitConfig>();
        }
    }

    public Integer updateIntergalacticUnits(IntergalacticUnitConfig unitConfig){
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR};
        Object[] params = new Object[] { unitConfig.getInterGalacticUnitName().toLowerCase(), unitConfig.getRomanNumeral()};
        if(!unitConfig.getInterGalacticUnitName().trim().isEmpty()){
            return jdbcTemplate.update(insertIntergalacticUnitConfig,params,types);
        }else{
            return null;
        }

    }
    public Integer updateComodity(Comodity comodity){
        int[] types = new int[] { Types.VARCHAR, Types.DOUBLE};
        Object[] params = new Object[] { comodity.getComodityName(), comodity.getComodityValue()};
        return jdbcTemplate.update(insertComodity,params,types);


    }

    public Boolean deleteIntergalacticUnitByName(String name){
        String sql = "delete from intergalactic_unit_config where intergalactic_unit_name= ?";
        Object[] args = new Object[] {name};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        }catch (EmptyResultDataAccessException ex){
            return false;
        }
    }
    public Boolean deleteIntergalacticUnitByRomanNumeral(String roman){
        String sql = "delete from intergalactic_unit_config where roman_numeral= ?";
        Object[] args = new Object[] {roman};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        }catch (EmptyResultDataAccessException ex){
            return false;
        }
    }
    public Boolean deleteComodityByName(String comodityName){
        String sql = "delete from comodity where LOWER(comodity_name)= ?";
        Object[] args = new Object[] {comodityName.toLowerCase()};
        try {
            return jdbcTemplate.update(sql, args) > 0;
        }catch (EmptyResultDataAccessException ex){
            return false;
        }
    }

    public Boolean checkDataExist(String tableName,String columnName, String params){
        String sql = "select count(*) from ";
        String where = " where "+columnName+"='"+params+"'";
        String finalSql = sql+tableName+where;
        try {
            Integer result = jdbcTemplate.queryForObject(finalSql, Integer.class);
            return  result>0;
        }catch (EmptyResultDataAccessException ex){
           throw ex;
        }
    }

//    public Boolean checkIntergalacticUnitExist(String params1,String params2){
//        String sql = "select count(*) from intergalactic_unit_config where intergalactic_unit_name='"+params1+"' and ";
//        String where = " where "+columnName+"='"+params+"'";
//        String finalSql = sql+tableName+where;
//        try {
//            Integer result = jdbcTemplate.queryForObject(finalSql, Integer.class);
//            return  result>0;
//        }catch (EmptyResultDataAccessException ex){
//            throw ex;
//        }
//    }



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
