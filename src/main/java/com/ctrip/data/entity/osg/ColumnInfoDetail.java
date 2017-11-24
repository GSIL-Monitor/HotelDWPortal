package com.ctrip.data.entity.osg;

/**
 * Created by j_le on 2017/5/17.
 */
public class ColumnInfoDetail {
    private String column_comment;
    private String column_name;
    private Integer is_identity;
    private String type;
    private String key;

    public String getColumn_comment() {
        return column_comment;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public Integer getIs_identity() {
        return is_identity;
    }

    public void setIs_identity(Integer is_identity) {
        this.is_identity = is_identity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
