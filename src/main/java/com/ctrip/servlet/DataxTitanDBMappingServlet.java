package com.ctrip.servlet;

import com.ctrip.data.entity.DataxTitanDBMapping;
import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.service.IDataxTitanDBMappingService;
import com.ctrip.data.service.IShardingSourceDBInfoService;
import com.ctrip.data.service.impl.DataxTitanDBMappingServiceImpl;
import com.ctrip.data.service.impl.ShardingSourceDBInfoServiceImpl;
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
 * Created by j_le on 2017/5/12.
 */
public class DataxTitanDBMappingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IDataxTitanDBMappingService iDataxTitanDBMappingService = null;
    IShardingSourceDBInfoService iShardingSourceDBInfoService = null;

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        try {
            iDataxTitanDBMappingService = new DataxTitanDBMappingServiceImpl();
            iShardingSourceDBInfoService = new ShardingSourceDBInfoServiceImpl();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public DataxTitanDBMappingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if ("shardingdbPreView".equals(type)) {
            shardingdbPreView(request, response);
        }else if("shardingdbSave".equals(type)){
            shardingdbSave(request, response);
        }else if ("getShardingDBInfoList".equals(type)) {
            getShardingDBInfoList(request, response);
//        } else if("getShardingDBInfoByDBName".equals(type)){
//            getShardingDBInfoByDBName(request, response);
        }
    }


//    private void getShardingDBInfoByDBName(HttpServletRequest request, HttpServletResponse response) {
//        response.setCharacterEncoding("utf-8");
//        PrintWriter pw = null;
//        try {
//            pw = response.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String DBName = request.getParameter("dbName");
//        //System.out.println(DBName);
//        DataxTitanDBMapping dataxTitanDBMapping = new DataxTitanDBMapping();
//        try {
//            dataxTitanDBMapping = iDataxTitanDBMappingService.getShardingDBInfoByDBName(DBName);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = JSONObject.fromObject(dataxTitanDBMapping);
//        //System.out.println(jsonObject);
//        pw.print(jsonObject);
//    }

    private void getShardingDBInfoList(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String DBType = request.getParameter("dbType");
        List<DataxTitanDBMapping> dataxTitanDBMappingList = null;
        try {
            dataxTitanDBMappingList = iDataxTitanDBMappingService.getShardingDBInfoList(DBType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonList = JSONArray.fromObject(dataxTitanDBMappingList);
        pw.print(jsonList);
    }

    private void shardingdbPreView(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String userName = AssertionHolder.getAssertion().getPrincipal().getName();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ALLShardingDB = request.getParameter("ALLShardingDB");
        int sourceDBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
        if (sourceDBID > 0) {
            List<ShardingDBInfo> sdbList = null;
            try {
                sdbList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(!ALLShardingDB.equals("0")){
                String[] strArray = null;
                strArray = ALLShardingDB.split(",");
                for(int i = 0;i < strArray.length;i++){
                    ShardingDBInfo sdb = new ShardingDBInfo();
                    sdb.setId(i);
                    sdb.setShardingDB(strArray[i]);
                    sdb.setIsValid("T");
                    sdb.setOperUid(userName);
                    sdb.setSourceDBID(sourceDBID);
                    sdbList.add(sdb);
                }
            }
            JSONArray jsonList = JSONArray.fromObject(sdbList);
            String json = "{\"total\":" + sdbList.size() + ",\"rows\":" + jsonList + "}";
            pw.print(json);
        }
    }

    private void shardingdbSave(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userName = AssertionHolder.getAssertion().getPrincipal().getName();
        String ALLShardingDB = request.getParameter("ALLShardingDB");
        int sourceDBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
        List<ShardingDBInfo> sdbList = new ArrayList<ShardingDBInfo>();
        String[] strArray = null;
        strArray = ALLShardingDB.split(",");
        for(int i = 0;i < strArray.length;i++){
            ShardingDBInfo sdb = new ShardingDBInfo();
            sdb.setId(i);
            sdb.setShardingDB(strArray[i]);
            sdb.setIsValid("T");
            sdb.setOperUid(userName);
            sdb.setSourceDBID(sourceDBID);
            sdbList.add(sdb);
        }
        try {
            iShardingSourceDBInfoService.insertBatch(sdbList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<ShardingDBInfo> dbList = new ArrayList<ShardingDBInfo>();
        try {
            dbList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dbList != null && dbList.size() > 0) {
            JSONArray jsonList = JSONArray.fromObject(dbList);
            String json = "{\"total\":" + dbList.size() + ",\"rows\":" + jsonList + "}";
            pw.print(json);
        }
    }



}
