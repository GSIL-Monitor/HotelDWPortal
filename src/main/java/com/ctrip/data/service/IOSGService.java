package com.ctrip.data.service;

import com.ctrip.data.entity.osg.ColumnInfo;
import com.ctrip.data.entity.osg.TableInfo;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by j_le on 2017/5/18.
 */
public interface IOSGService {
    public List<TableInfo> getTableinfoList(JSONObject request_body);

    public ColumnInfo getColumnInfo(JSONObject request_body);

    public String getCreateFieldsType(String srcType, String type);
}
