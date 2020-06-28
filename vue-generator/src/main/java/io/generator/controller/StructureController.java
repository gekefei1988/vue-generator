package io.generator.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.generator.entity.ColumnsStructure;
import io.generator.form.ColumnsStructureFrom;
import io.generator.service.StructureService;
import io.generator.utils.Tool;

@RestController
@RequestMapping("/structure")
public class StructureController {
	private static final Logger log = LoggerFactory.getLogger(StructureController.class);
	@Autowired
	private StructureService structureService;
	//查询表结构
	@PostMapping("/structureSel")
	public List<ColumnsStructure>  structureSel(@Valid ColumnsStructureFrom form){
		List<ColumnsStructure> structureSelService = structureService.structureSel(form.getTableSchema(),form.getTableName());
		for(ColumnsStructure columnsStructure:structureSelService) {
			String lineToHump = Tool.lineToHump(columnsStructure.getColumnName());
			columnsStructure.setHumpColumnName(lineToHump);
			String tableName = Tool.lineToHump(form.getTableName());
			columnsStructure.setTableName(tableName);
		}
		System.out.println(structureSelService);
		return structureSelService;
	}
}
