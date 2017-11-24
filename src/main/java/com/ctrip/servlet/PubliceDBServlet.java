package com.ctrip.servlet;

import com.ctrip.data.entity.ShardingSourceDBInfo;
import com.ctrip.data.entity.ShardingSourceTBInfo;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.entity.osg.ColumnInfo;
import com.ctrip.data.entity.osg.ColumnInfoMysqlRequest;
import com.ctrip.data.entity.osg.ColumnInfoSqlserverRequest;
import com.ctrip.data.service.IOSGService;
import com.ctrip.data.service.IPubliceDBService;
import com.ctrip.data.service.IShardingSourceDBInfoService;
import com.ctrip.data.service.IShardingSourceTBInfoService;
import com.ctrip.data.service.impl.OSGServiceImpl;
import com.ctrip.data.service.impl.PubliceDBServiceImpl;
import com.ctrip.data.service.impl.ShardingSourceDBInfoServiceImpl;
import com.ctrip.data.service.impl.ShardingSourceTBInfoServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.AssertionHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j_le on 2017/5/19.
 */
public class PubliceDBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IOSGService iOSGService = null;
    IPubliceDBService iPubliceDBService = null;
    IShardingSourceTBInfoService iShardingSourceTBInfoService = null;
    IShardingSourceDBInfoService iShardingSourceDBInfoService = null;

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        try {
            iOSGService = new OSGServiceImpl();
            iPubliceDBService = new PubliceDBServiceImpl();
            iShardingSourceTBInfoService = new ShardingSourceTBInfoServiceImpl();
            iShardingSourceDBInfoService = new ShardingSourceDBInfoServiceImpl();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public PubliceDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if ("shardingtbSaveUpdateColumns".equals(type)) {
            shardingtbSaveUpdateColumns(request, response);
        }
    }

    /**
     *
     * @param request
     * @param response
     * 功能：保存插入的shardingtb数据并更新ShardingSourceTB的字段Columns和PKColumns
     */
    private void shardingtbSaveUpdateColumns(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userName = AssertionHolder.getAssertion().getPrincipal().getName();
        String ALLShardingTB = request.getParameter("ALLShardingTB");
        int sourceTBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
        List<ShardingTBInfo> stbList = new ArrayList<ShardingTBInfo>();
        String jSONArrayString = "[" + ALLShardingTB + "]";
        JSONArray jsonList1 = JSONArray.fromObject(jSONArrayString);
        //System.out.println(jsonList1);
        for (int i = 0; i < jsonList1.size(); i++) {
            JSONObject jSONObject = jsonList1.getJSONObject(i);
            ShardingTBInfo stb = new ShardingTBInfo();
            stb.setId(i);
            stb.setShardingTB(jSONObject.get("table_name").toString());
            stb.setIsValid("T");
            stb.setOperUid(userName);
            stb.setSourceTBID(sourceTBID);
            stbList.add(stb);
        }
        JSONObject jSONObjectFirst = jsonList1.getJSONObject(0);
        ShardingSourceTBInfo shardingSourceTBInfo = null;
        try {
            shardingSourceTBInfo = iShardingSourceTBInfoService.getShardingSourceTBById(sourceTBID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(shardingSourceTBInfo.getSourceDBID());
        ShardingSourceDBInfo shardingSourceDBInfo = null;
        try {
            shardingSourceDBInfo = iShardingSourceDBInfoService.getShardingSourceDBById(shardingSourceTBInfo.getSourceDBID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //获取字段信息
        JSONObject request_body = null;
        if (shardingSourceDBInfo.getSourceType().equals("SQLSERVER")) {
            ColumnInfoSqlserverRequest columnInfoSqlserverRequest = new ColumnInfoSqlserverRequest();
            columnInfoSqlserverRequest.setDb_type("sqlserver");
            columnInfoSqlserverRequest.setDb_id((String) jSONObjectFirst.get("db_id"));
            columnInfoSqlserverRequest.setMachine_name((String) jSONObjectFirst.get("machine_name"));
            columnInfoSqlserverRequest.setObject_id((String) jSONObjectFirst.get("object_id"));
            request_body = JSONObject.fromObject(columnInfoSqlserverRequest);
        } else {
            ColumnInfoMysqlRequest columnInfoMysqlRequest = new ColumnInfoMysqlRequest();
            columnInfoMysqlRequest.setDb_type("mysql");
            columnInfoMysqlRequest.setTable_name((String) jSONObjectFirst.get("table_name"));
            columnInfoMysqlRequest.setMachine_name((String) jSONObjectFirst.get("machine_name"));
            columnInfoMysqlRequest.setDb_name((String) jSONObjectFirst.get("db_name"));
            request_body = JSONObject.fromObject(columnInfoMysqlRequest);
        }
        //System.out.println(request_body);
        ColumnInfo columnInfo = iOSGService.getColumnInfo(request_body);
        //System.out.println(columnInfo);
        JSONArray column_dataJSONArray = JSONArray.fromObject(columnInfo.getColumn_data());
        //System.out.println(column_dataJSONArray);
        String updateCoulmninfo = "";
        String updatePKColumns = "";
        for (int i = 0; i < column_dataJSONArray.size(); i++) {
            JSONObject column_dataJSONObject = column_dataJSONArray.getJSONObject(i);
            updateCoulmninfo += "," + column_dataJSONObject.get("column_name");
            if (column_dataJSONObject.get("key").equals("PRI")) {
                updatePKColumns += "," + column_dataJSONObject.get("column_name");
            }
        }
        //System.out.println(updateCoulmninfo);
        updateCoulmninfo = updateCoulmninfo.substring(1, updateCoulmninfo.length());
        updatePKColumns = updatePKColumns.substring(1, updatePKColumns.length());
        //System.out.println(updateCoulmninfo);
        //System.out.println(updatePKColumns);
        int issuccess = 0;
        try {
            issuccess = iPubliceDBService.shardingtbSaveUpdateColumns(stbList, sourceTBID, updateCoulmninfo, updatePKColumns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(issuccess);
        if (issuccess == 1) {
            List<ShardingTBInfo> tbList = new ArrayList<ShardingTBInfo>();
            try {
                tbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (tbList != null && tbList.size() > 0) {
                JSONArray jsonList = JSONArray.fromObject(tbList);
                String json = "{\"total\":" + tbList.size() + ",\"rows\":" + jsonList + "}";
                pw.print(json);
            }
        }
    }

}
