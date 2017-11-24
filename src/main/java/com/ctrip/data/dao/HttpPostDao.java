package com.ctrip.data.dao;

import com.ctrip.data.entity.osg.ColumnInfo;
import com.ctrip.data.entity.osg.ColumnInfoDetail;
import com.ctrip.data.entity.osg.TableInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j_le on 2017/5/18.
 */
public class HttpPostDao {
    public static final String ACCESS_TOKEN = "510f3ddd94423d80d09e9b332edd5ab5";
    public List<TableInfo> getTableinfoList(JSONObject request_body) {
        String url = "http://osg.ops.ctripcorp.com/api/querydb";
        //创建连接
        URL urlAddress = null;
        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) urlAddress.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        //connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //POST请求
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();

        obj.put("access_token", ACCESS_TOKEN);

        //SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(sf.format(new Date()));
        obj.put("request_body",request_body);

        //System.out.println(obj);
        try {
            //out.writeBytes(obj.toString());//这个中文会乱码
            out.write(obj.toString().getBytes("UTF-8"));//这样可以处理中文乱码问题
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取响应
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lines;
        StringBuffer sb = new StringBuffer("");
        try {
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(sb);
        String s = sb.toString();
        //System.out.println(s);
        JSONArray jsonList = JSONArray.fromObject(s);
        //System.out.println(jsonList);
        List<TableInfo> tableInfoList =  new ArrayList<TableInfo>();
        for (Object jsonObject : jsonList) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setDb_id(((JSONObject)jsonObject).get("db_id")==null?"":((JSONObject) jsonObject).get("db_id").toString());
            tableInfo.setDb_name(((JSONObject)jsonObject).get("db_name")==null?"":((JSONObject) jsonObject).get("db_name").toString());
            tableInfo.setMachine_name(((JSONObject)jsonObject).get("machine_name")==null?"":((JSONObject) jsonObject).get("machine_name").toString());
            tableInfo.setObject_id(((JSONObject)jsonObject).get("object_id")==null?"":((JSONObject) jsonObject).get("object_id").toString());
            tableInfo.setTable_name(((JSONObject)jsonObject).get("table_name")==null?"":((JSONObject) jsonObject).get("table_name").toString());
            tableInfoList.add(tableInfo);
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 断开连接
        connection.disconnect();
        return tableInfoList;
    }

    public ColumnInfo getColumnInfo(JSONObject request_body) {
        String url = "http://osg.ops.ctripcorp.com/api/tableinfo";
        //创建连接
        URL urlAddress = null;
        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) urlAddress.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        //connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //POST请求
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();

        obj.put("access_token", ACCESS_TOKEN);

        //SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(sf.format(new Date()));
        obj.put("request_body",request_body);

        //System.out.println(obj);
        try {
            //out.writeBytes(obj.toString());//这个中文会乱码
            out.write(obj.toString().getBytes("UTF-8"));//这样可以处理中文乱码问题
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取响应
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lines;
        StringBuffer sb = new StringBuffer("");
        try {
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(sb);
        String s = sb.toString();
        //System.out.println(s);
        JSONObject jsonObject = JSONObject.fromObject(s);
        //System.out.println(jsonList);
        ColumnInfo ColumnInfo =  new ColumnInfo();
        ColumnInfo.setTable_comment(jsonObject.get("table_comment")==null?"":jsonObject.get("table_comment").toString());
        ColumnInfo.setColumn_data(jsonObject.get("column_data")==null?"":jsonObject.get("column_data").toString());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 断开连接
        connection.disconnect();
        return ColumnInfo;
    }
}
