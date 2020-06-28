package io.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.alibaba.fastjson.JSONObject;

import io.generator.entity.ColumnEntity;
import io.generator.entity.TableEntity;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {

    public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();
      /*  templates.add("template/Entity.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/Dao.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        templates.add("template/menu.sql.vm");
*/
        templates.add("template/index.vue.vm");
        templates.add("template/detail.vue.vm");
        templates.add("template/api.js.vm");
        templates.add("template/singleSelect.vue.vm");

        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName" ));
        tableEntity.setComments(table.get("tableComment" ));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getStringArray("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for(Map<String, String> column : columns){
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType" ));
            columnEntity.setComments(column.get("columnComment" ));
            columnEntity.setExtra(column.get("extra" ));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType" );
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey" )) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
        String mainPath = config.getString("mainPath" );
        mainPath = StringUtils.isBlank(mainPath) ? "io.generator" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author" ));
        map.put("email", config.getString("email" ));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8" );
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package" ), config.getString("moduleName" ))));
                IOUtils.write(sw.toString(), zip, "UTF-8" );
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }
    
    public static void createCode(JSONObject obj) throws Exception {
    	
    	try {
    		
			String tableName = obj.getString("tableName");//表名
			String tableSimpleName = obj.getString("tableSimpleName");//表简称
			String functionName = obj.getString("functionName");//功能名称
			String popConfirmColumn = obj.getString("popConfirmColumn");//小弹窗确认返回字段
			List<Map> queryList = JSONObject.parseArray(obj.get("queryConditionList").toString(),Map.class);
			List<Map> inventoryList = JSONObject.parseArray(obj.get("inventoryList").toString(),Map.class);
			List<Map> maintainList = JSONObject.parseArray(obj.get("maintainList").toString(),Map.class);
			List<Map> popSelectList = JSONObject.parseArray(obj.get("popSelectList").toString(),Map.class);//小弹窗查询条件列表
			List<Map> columnList = JSONObject.parseArray(obj.get("columnList").toString(),Map.class);//维护页面字段集合
			
			Collections.sort(queryList, new Comparator<Map>() {
	
				@Override
				public int compare(Map o1, Map o2) {
					
					int sort1 = (int) o1.get("queryConditionSequence");
					int sort2 = (int) o2.get("queryConditionSequence");
					
					return sort1 - sort2;
				}
			});
			
			Collections.sort(inventoryList, new Comparator<Map>() {
	
				@Override
				public int compare(Map o1, Map o2) {
					
					int sort1 = (int) o1.get("inventoryListOrder");
					int sort2 = (int) o2.get("inventoryListOrder");
					
					return sort1 - sort2;
				}
			});
			
			Collections.sort(maintainList, new Comparator<Map>() {
	
				@Override
				public int compare(Map o1, Map o2) {
					
					int sort1 = (int) o1.get("maintainOrder");
					int sort2 = (int) o2.get("maintainOrder");
					
					return sort1 - sort2;
				}
			});
			
			Collections.sort(popSelectList, new Comparator<Map>() {
	
				@Override
				public int compare(Map o1, Map o2) {
					
					int sort1 = (int) o1.get("popSelectOrder");
					int sort2 = (int) o2.get("popSelectOrder");
					
					return sort1 - sort2;
				}
			});
			
			//设置velocity资源加载器
	        Properties prop = new Properties();
	        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
	        Velocity.init(prop);
	        
	        //列的数据类型，转换成Java类型
	        Configuration config = getConfig();
			
			//封装模板数据
	        Map<String, Object> map = new HashMap<>();
	        map.put("moduleName", config.getString("moduleName"));
	        map.put("projectName", config.getString("projectName"));
	        map.put("tableSimpleName", tableSimpleName);
	        map.put("tableName", tableName);
	        map.put("functionName", functionName);
	        map.put("popConfirmColumn", popConfirmColumn);
	        map.put("queryConditionList", queryList);
	        map.put("inventoryListList", inventoryList);
	        map.put("maintainList", maintainList);
	        map.put("popSelectList", popSelectList);
	        map.put("columnList", columnList);
	        VelocityContext context = new VelocityContext(map);
	        String filePath = obj.getString("filePath");//文件下载路径
			
	        //获取模板列表
	        List<String> templates = getTemplates();
        	
        	for (String template : templates) {
                //渲染模板
                //StringWriter sw = new StringWriter();
            	File file = new File(filePath,getFileName(template));
            	
                if (!file.getParentFile().exists()) {
                	
                	file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                	
                	file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(outStream,"UTF-8");
                Template tpl = Velocity.getTemplate(template, "UTF-8" );
            	BufferedWriter sw = new BufferedWriter(writer);
                tpl.merge(context, sw);
                
                sw.flush();
                sw.close();
                outStream.close();
            }
        } catch (Exception e) {
        	
            //throw new RRException("下载失败", e);
        	e.printStackTrace();
        	throw e;
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "" );
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if(null != tablePrefixArray && tablePrefixArray.length>0){
            for(String tablePrefix : tablePrefixArray){
                tableName = tableName.replace(tablePrefix, "");
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties" );
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Entity.java.vm" )) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }

        if (template.contains("Dao.java.vm" )) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm" )) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm" )) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm" )) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }

        if (template.contains("menu.sql.vm" )) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.contains("index.vue.vm" )) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        	return "index.vue";
        }

        if (template.contains("add-or-update.vue.vm" )) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
        	return "detail.vue";
        }

        return null;
    }
    
    public static String getFileName(String template) {
    	
    	if (template.contains("index.vue.vm" )) {
    		
    		return "index.vue";
    	}

    	if (template.contains("detail.vue.vm" )) {
    		
	      	return "detail.vue";
	    }
    	
    	if (template.contains("api.js.vm" )) {
    		
	      	return "api.js";
	    }
    	
    	if (template.contains("singleSelect.vue.vm" )) {
    		
	      	return "single-select.vue";
	    }
      
    	return null;
    }
}