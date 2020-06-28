package io.generator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.generator.entity.ColumnsStructure;

public interface StructureDao {

	 List<ColumnsStructure> structureSel(@Param("tableSchema")String tableSchema, @Param("tableName")String tableName);
}
