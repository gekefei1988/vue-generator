package io.generator.pojo.vo;

public class ColumnsStructureVo{
	private String humpColumnName;
	private String tableName;
	public ColumnsStructureVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ColumnsStructureVo(String humpColumnName, String tableName) {
		super();
		this.humpColumnName = humpColumnName;
		this.tableName = tableName;
	}
	@Override
	public String toString() {
		return "ColumnsStructureVo [humpColumnName=" + humpColumnName + ", tableName=" + tableName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((humpColumnName == null) ? 0 : humpColumnName.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnsStructureVo other = (ColumnsStructureVo) obj;
		if (humpColumnName == null) {
			if (other.humpColumnName != null)
				return false;
		} else if (!humpColumnName.equals(other.humpColumnName))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	public String getHumpColumnName() {
		return humpColumnName;
	}
	public void setHumpColumnName(String humpColumnName) {
		this.humpColumnName = humpColumnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
