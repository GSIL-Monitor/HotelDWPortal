package com.ctrip.data.entity.osg;

/**
 * Created by j_le on 2017/5/17.
 */
public class ColumnInfo {
    private String table_comment;
    private String column_data;

    public String getTable_comment() {
        return table_comment;
    }

    public void setTable_comment(String table_comment) {
        this.table_comment = table_comment;
    }

    public String getColumn_data() {
        return column_data;
    }

    public void setColumn_data(String column_data) {
        this.column_data = column_data;
    }
}
