package com.ctrip.data.service.impl;

import com.ctrip.data.dao.HttpPostDao;
import com.ctrip.data.entity.osg.ColumnInfo;
import com.ctrip.data.entity.osg.TableInfo;
import com.ctrip.data.service.IOSGService;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by j_le on 2017/5/18.
 */
public class OSGServiceImpl implements IOSGService {
    HttpPostDao httpPostDao = new HttpPostDao();
    public List<TableInfo> getTableinfoList(JSONObject request_body){
        return httpPostDao.getTableinfoList(request_body);
    }

    public ColumnInfo getColumnInfo(JSONObject request_body) {
        return httpPostDao.getColumnInfo(request_body);
    }

    public String getCreateFieldsType (String srcType, String type) {
        String createFieldsType = "";
        HashMap<String, String> sqlserverMap = new HashMap<String, String>() {
            {
                put("TINYINT", "TINYINT");
                put("SMALLINT", "SMALLINT");
                put("MEDIUMINT", "INT");
                put("INT", "INT");
                put("BIGINT", "BIGINT");
                put("FLOAT", "FLOAT");
                put("REAL", "DOUBLE");
                put("DECIMAL", "DECIMAL(19,4)");
                put("NUMERIC", "DECIMAL(19,4)");
                put("SMALLMONEY", "DECIMAL(10,4)");
                put("MONEY", "DECIMAL(19,4)");
                put("CHAR", "STRING");
                put("VARCHAR", "STRING");
                put("TEXT", "STRING");
                put("NCHAR", "STRING");
                put("NVARCHAR", "STRING");
                put("NTEXT", "STRING");
                put("DATE", "STRING");
                put("SMALLDATETIME", "STRING");
                put("DATETIME", "STRING");
                put("DATETIME2", "STRING");
                put("DATETIMEOFFSET", "STRING");
                put("TIMESTAMP", "STRING");
                put("TIME", "STRING");
                put("YEAR", "INT");
                put("BIT", "INT");
                put("BINARY", "BINARY");
                put("VARBINARY", "BINARY");
                put("IMAGE", "BINARY");
                put("UNIQUEIDENTIFIER", "STRING");
                put("XML", "STRING");
            }
        };
        HashMap<String, String> mysqlMap = new HashMap<String, String>() {
            {
                put("TINYINT", "TINYINT");
                put("SMALLINT", "SMALLINT");
                put("MEDIUMINT", "INT");
                put("INT", "INT");
                put("INTEGER", "INT");
                put("BIGINT", "BIGINT");
                put("FLOAT", "FLOAT");
                put("DOUBLE", "DOUBLE");
                put("DECIMAL", "DECIMAL(19,4)");
                put("NUMERIC", "DECIMAL(19,4)");

                put("CHAR", "STRING");
                put("VARCHAR", "STRING");
                put("TINYTEXT", "STRING");
                put("TEXT", "STRING");
                put("MEDIUMTEXT", "STRING");
                put("LONGTEXT", "STRING");
                put("ENUM", "STRING");
                put("SET", "STRING");

                put("DATE", "STRING");
                put("DATETIME", "STRING");
                put("TIMESTAMP", "STRING");
                put("TIME", "STRING");
                put("YEAR", "INT");

                put("BIT", "INT");
                put("TINYBLOB", "BINARY");
                put("BLOB", "BINARY");
                put("MEDIUMBLOB", "BINARY");
                put("LOGNGBLOB", "BINARY");
                put("VARBINARY", "STRING");
                put("BINARY", "STRING");
            }
        };
        HashMap<String, String> map = new HashMap<String, String>();
        if("MYSQL".equals(srcType.trim().toUpperCase())){
            map = mysqlMap;
        }else if("SQLSERVER".equals(srcType.trim().toUpperCase())){
            map = sqlserverMap;
        }
        String[] items =  type.trim().toUpperCase().split("\\(");
        String j = items[0].trim();
        if((j == "DECIMAL" || j == "NUMERIC") && (items.length == 2)){
            createFieldsType = map.get(j) + "(" + items[1];
        }else{
            createFieldsType = map.get(j);
        }
        return createFieldsType;
    }
}
