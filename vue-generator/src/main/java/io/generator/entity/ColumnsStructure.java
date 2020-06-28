package io.generator.entity;

import io.generator.pojo.vo.ColumnsStructureVo;

/**
 * 表结构
 * @author wangyue
 *
 */
public class ColumnsStructure extends ColumnsStructureVo{
	//列名
	private String columnName;
	//注释
	private String columnComment;
	//类型
	private String dataType;
	public ColumnsStructure() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ColumnsStructure(String humpColumnName, String tableName) {
		super(humpColumnName, tableName);
		// TODO Auto-generated constructor stub
	}
	public ColumnsStructure(String columnName, String columnComment, String dataType) {
		super();
		this.columnName = columnName;
		this.columnComment = columnComment;
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "ColumnsStructure [columnName=" + columnName + ", columnComment=" + columnComment + ", dataType="
				+ dataType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((columnComment == null) ? 0 : columnComment.hashCode());
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnsStructure other = (ColumnsStructure) obj;
		if (columnComment == null) {
			if (other.columnComment != null)
				return false;
		} else if (!columnComment.equals(other.columnComment))
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		return true;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
