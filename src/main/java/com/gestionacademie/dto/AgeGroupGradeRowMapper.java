package com.gestionacademie.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgeGroupGradeRowMapper implements RowMapper<AgeGroupGradeDTO> {

    @Override
    public AgeGroupGradeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AgeGroupGradeDTO(
                rs.getString("ageGroup"),
                rs.getString("gradeCategorie"),
                rs.getLong("nombreMilitaires")
        );
    }
}
