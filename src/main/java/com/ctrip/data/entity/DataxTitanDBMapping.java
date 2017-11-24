package com.ctrip.data.entity;

import com.ctrip.platform.dal.dao.annotation.Database;
import com.ctrip.platform.dal.dao.annotation.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by j_le on 2017/5/12.
 */

@Entity
@Database(name = "Zeus")
@Table(name = "Datax_Titan_DB_Mapping")
public class DataxTitanDBMapping {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(value = Types.INTEGER)
    private Integer id;

    @Column(name="Titankey")
    @Type(value= Types.VARCHAR)
    private String titankey;

    @Column(name="Mode")
    @Type(value= Types.VARCHAR)
    private String mode;

    @Column(name="DBName")
    @Type(value= Types.VARCHAR)
    private String dbName;

    @Column(name="DBType")
    @Type(value= Types.VARCHAR)
    private String dbType;

    @Column(name="Owner")
    @Type(value= Types.VARCHAR)
    private String owner;

    @Column(name="Server")
    @Type(value= Types.VARCHAR)
    private String server;

    @Column(name="Port")
    @Type(value= Types.INTEGER)
    private Integer port;

    @Column(name="Uid")
    @Type(value= Types.VARCHAR)
    private String uid;

    @Column(name="Create_Time")
    @Type(value=Types.TIMESTAMP)
    private Timestamp create_Time;

    @Column(name="Is_effective")
    @Type(value=Types.VARCHAR)
    private String is_effective;

    public Integer getId() {
        return id;
    }

    public String getTitankey() {
        return titankey;
    }

    public String getMode() {
        return mode;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbType() {
        return dbType;
    }

    public String getOwner() {
        return owner;
    }

    public String getServer() {
        return server;
    }

    public Integer getPort() {
        return port;
    }

    public String getUid() {
        return uid;
    }

    public Timestamp getCreate_Time() {
        return create_Time;
    }

    public String getIs_effective() {
        return is_effective;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitankey(String titankey) {
        this.titankey = titankey;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCreate_Time(Timestamp create_Time) {
        this.create_Time = create_Time;
    }

    public void setIs_effective(String is_effective) {
        this.is_effective = is_effective;
    }
}
