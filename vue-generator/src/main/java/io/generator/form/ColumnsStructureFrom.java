package io.generator.form;

import javax.validation.constraints.NotBlank;

public class ColumnsStructureFrom {
	@NotBlank
	private String tableSchema;
	@NotBlank
	private String tableName;
	public ColumnsStructureFrom() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ColumnsStructureFrom(@NotBlank String tableSchema, @NotBlank String tableName) {
		super();
		this.tableSchema = tableSchema;
		this.tableName = tableName;
	}
	@Override
	public String toString() {
		return "StructureFrom [tableSchema=" + tableSchema + ", tableName=" + tableName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((tableSchema == null) ? 0 : tableSchema.hashCode());
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
		ColumnsStructureFrom other = (ColumnsStructureFrom) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tableSchema == null) {
			if (other.tableSchema != null)
				return false;
		} else if (!tableSchema.equals(other.tableSchema))
			return false;
		return true;
	}
	public String getTableSchema() {
		return tableSchema;
	}
	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
