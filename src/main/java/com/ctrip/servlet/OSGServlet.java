package com.ctrip.servlet;

import com.ctrip.data.entity.DataxTitanDBMapping;
import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingSourceTBInfo;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.entity.osg.*;
import com.ctrip.data.service.IDataxTitanDBMappingService;
import com.ctrip.data.service.IOSGService;
import com.ctrip.data.service.IShardingSourceDBInfoService;
import com.ctrip.data.service.IShardingSourceTBInfoService;
import com.ctrip.data.service.impl.DataxTitanDBMappingServiceImpl;
import com.ctrip.data.service.impl.OSGServiceImpl;
import com.ctrip.data.service.impl.ShardingSourceDBInfoServiceImpl;
import com.ctrip.data.service.impl.ShardingSourceTBInfoServiceImpl;
import com.ctrip.data.util.JSONUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by j_le on 2017/5/18.
 */

public class OSGServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IOSGService iOSGService = null;
    IShardingSourceDBInfoService iShardingSourceDBInfoService = null;
    IShardingSourceTBInfoService iShardingSourceTBInfoService = null;
    IDataxTitanDBMappingService iDataxTitanDBMappingService = null;

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        try {
            iOSGService = new OSGServiceImpl();
            iShardingSourceDBInfoService = new ShardingSourceDBInfoServiceImpl();
            iShardingSourceTBInfoService = new ShardingSourceTBInfoServiceImpl();
            iDataxTitanDBMappingService = new DataxTitanDBMappingServiceImpl();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public OSGServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if ("getOSGShardingTBInfoList".equals(type)) {
            getOSGShardingTBInfoList(request, response);
        }else if("getCreateTableScript".equals(type)){
            getCreateTableScript(request, response);
        }
    }

    private void getOSGShardingTBInfoList(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int sourceDBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
        List<ShardingDBInfo> shardingDBInfoList = null;
        try {
            shardingDBInfoList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //JSONArray shardingDBInfojsonList = JSONArray.fromObject(shardingDBInfoList);
        //JSONObject shardingDBInfoJsonObject = shardingDBInfojsonList.getJSONObject(0);

        DataxTitanDBMapping dataxTitanDBMapping = new DataxTitanDBMapping();
        try {
            if (shardingDBInfoList != null) {
                dataxTitanDBMapping = iDataxTitanDBMappingService.getShardingDBInfoByDBName(shardingDBInfoList.get(0).getShardingDB());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //JSONObject dataxTitanDBMappingJsonObject = JSONObject.fromObject(dataxTitanDBMapping);

        TableInfoRequest tableInfoRequest = new TableInfoRequest();
        tableInfoRequest.setTable_name("");
        tableInfoRequest.setDb_name(dataxTitanDBMapping.getDbName());
        tableInfoRequest.setDb_type(dataxTitanDBMapping.getDbType());
        tableInfoRequest.setDns(dataxTitanDBMapping.getServer());
        JSONObject request_body = JSONObject.fromObject(tableInfoRequest);

        List<TableInfo> tableInfoList = iOSGService.getTableinfoList(request_body);
        JSONArray tableInfojsonList = JSONArray.fromObject(tableInfoList);

        JSONUtil jSONUtil = new JSONUtil();
        jSONUtil.sort(tableInfojsonList);
        //System.out.println(tableInfojsonList);
        pw.print(tableInfojsonList);
    }

    private void getCreateTableScript(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int shardingSourceTBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("shardingSourceTBID")) ? "0" : request.getParameter("shardingSourceTBID"));
        String targetDB = request.getParameter("targetDB");
        String targetTB = request.getParameter("targetTB");

        ShardingSourceTBInfo shardingSourceTBInfo = null;
        try {
            shardingSourceTBInfo = iShardingSourceTBInfoService.getShardingSourceTBById(shardingSourceTBID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<ShardingTBInfo> shardingTBInfoList = null;
        try {
            shardingTBInfoList = iShardingSourceTBInfoService.getShardingTBInfoList(shardingSourceTBInfo.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ShardingTBInfo shardingTBInfo = shardingTBInfoList.get(0);

        List<ShardingDBInfo> shardingDBInfoList = null;
        try {
            shardingDBInfoList = iShardingSourceDBInfoService.getShardingDBInfoList(shardingSourceTBInfo.getSourceDBID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ShardingDBInfo shardingDBInfo = shardingDBInfoList.get(0);

        DataxTitanDBMapping dataxTitanDBMapping = new DataxTitanDBMapping();
        try {
            if (shardingDBInfo != null) {
                dataxTitanDBMapping = iDataxTitanDBMappingService.getShardingDBInfoByDBName(shardingDBInfo.getShardingDB());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableInfoRequest tableInfoRequest = new TableInfoRequest();
        tableInfoRequest.setTable_name(shardingTBInfo.getShardingTB());
        tableInfoRequest.setDb_name(dataxTitanDBMapping.getDbName());
        tableInfoRequest.setDb_type(dataxTitanDBMapping.getDbType());
        tableInfoRequest.setDns(dataxTitanDBMapping.getServer());
        JSONObject request_body = JSONObject.fromObject(tableInfoRequest);
        List<TableInfo> tableInfoList = iOSGService.getTableinfoList(request_body);
        TableInfo tableInfo = tableInfoList.get(0);

        JSONObject columnInforequest_body = null;
        if("sqlserver".equals(dataxTitanDBMapping.getDbType().toLowerCase())){
            ColumnInfoSqlserverRequest columnInfoSqlserverRequest = new ColumnInfoSqlserverRequest();
            columnInfoSqlserverRequest.setDb_type("sqlserver");
            columnInfoSqlserverRequest.setDb_id(tableInfo.getDb_id());
            columnInfoSqlserverRequest.setMachine_name(tableInfo.getMachine_name());
            columnInfoSqlserverRequest.setObject_id(tableInfo.getObject_id());
            columnInforequest_body = JSONObject.fromObject(columnInfoSqlserverRequest);
        } else {
            ColumnInfoMysqlRequest columnInfoMysqlRequest = new ColumnInfoMysqlRequest();
            columnInfoMysqlRequest.setDb_type("mysql");
            columnInfoMysqlRequest.setTable_name(tableInfo.getTable_name());
            columnInfoMysqlRequest.setMachine_name(tableInfo.getMachine_name());
            columnInfoMysqlRequest.setDb_name(tableInfo.getDb_name());
            columnInforequest_body = JSONObject.fromObject(columnInfoMysqlRequest);
        }
        ColumnInfo columnInfo = iOSGService.getColumnInfo(columnInforequest_body);
        //System.out.println(columnInfo);
        JSONObject columnInfoJSONObject = JSONObject.fromObject(columnInfo);
        //System.out.println(columnInfoJSONObject);
        String createMidtargettbScript = "USE " + targetDB + ";\\n" +
                                         "CREATE TABLE " + targetTB + "_base(\\n";
        String createtargettbScript = "USE " + targetDB + ";\\n" +
                                      "CREATE TABLE " + targetTB + "(\\n";
        String fields = "";
        for(Object columnInfoObject : JSONArray.fromObject(columnInfoJSONObject.get("column_data"))){
            fields += "\\t" + (((JSONObject)columnInfoObject).get("column_name")==null?"":((JSONObject) columnInfoObject).get("column_name").toString()) + " " +
                      iOSGService.getCreateFieldsType(dataxTitanDBMapping.getDbType(), ((JSONObject) columnInfoObject).get("type").toString()) + " COMMENT '" +
                    (((JSONObject)columnInfoObject).get("column_comment")==null?"":((JSONObject) columnInfoObject).get("column_comment").toString().replace(";", ": ").replace("\r\n", " ").replace("\"", " ")) + "',\\n";
        }
        fields = fields.substring(0, fields.length()-3);
        //System.out.println(fields);
        createMidtargettbScript = createMidtargettbScript + fields + ")\\n" +
                                  "COMMENT '" + (columnInfoJSONObject.get("table_comment")==null?"":columnInfoJSONObject.get("table_comment")) + "'\\n" +
                                  "PARTITIONED BY(d STRING COMMENT 'date',shardid STRING COMMENT '二级分区shardid')\\n" +
                                  "STORED AS ORC;";
        //System.out.println(createMidtargettbScript);
        createtargettbScript = createtargettbScript + fields + ",\\n\\t" +
                                "shardid STRING COMMENT '批次标记')\\n" +
                                "COMMENT '" + (columnInfoJSONObject.get("table_comment")==null?"":columnInfoJSONObject.get("table_comment")) + "'\\n" +
                                "PARTITIONED BY(d STRING COMMENT 'date')\\n" +
        "STORED AS ORC;";
        //System.out.println(createtargettbScript);
        JSONObject createScript = JSONObject.fromObject("{\"createMidtargettbScript\" : \"" + createMidtargettbScript + "\",\"createtargettbScript\" : \"" + createtargettbScript + "\"}");
        System.out.println(createScript);
        pw.print(createScript);
    }
}
