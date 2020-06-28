package io.generator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.generator.dao.StructureDao;
import io.generator.entity.ColumnsStructure;

@Service
public class StructureService {
	
	@Autowired
	private StructureDao structureDao;
	
	//查询表结构
	public List<ColumnsStructure> structureSel(String tableSchema, String tableName){
		
		return structureDao.structureSel(tableSchema,tableName);
	}
	
}
