package com.ctrip.data.entity;

import java.sql.Timestamp;
import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ctrip.platform.dal.dao.DalPojo;
import com.ctrip.platform.dal.dao.annotation.Database;
import com.ctrip.platform.dal.dao.annotation.Type;

@Entity
@Database(name = "PublicDB")
@Table(name = "ShardingDBInfo")
public class ShardingDBInfo {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(value = Types.INTEGER)
	private Integer id;
	
	@Column(name="SourceDBID")
	@Type(value=Types.INTEGER)
	private Integer sourceDBID;
	
	@Column(name="ShardingDB")
	@Type(value=Types.VARCHAR)
	private String shardingDB;
	
	@Column(name="InsertDT")
	@Type(value=Types.TIMESTAMP)
	private Timestamp insertDT;
	
	@Column(name="UpdateDT")
	@Type(value=Types.TIMESTAMP)
	private Timestamp updateDT;
	
	@Column(name="IsValid")
	@Type(value=Types.CHAR)
	private String isValid;
	
	@Column(name="OperUid")
	@Type(value=Types.VARCHAR)
	private String operUid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourceDBID() {
		return sourceDBID;
	}

	public void setSourceDBID(Integer sourceDBID) {
		this.sourceDBID = sourceDBID;
	}

	public String getShardingDB() {
		return shardingDB;
	}

	public void setShardingDB(String shardingDB) {
		this.shardingDB = shardingDB;
	}

	public Timestamp getInsertDT() {
		return insertDT;
	}

	public void setInsertDT(Timestamp insertDT) {
		this.insertDT = insertDT;
	}

	public Timestamp getUpdateDT() {
		return updateDT;
	}

	public void setUpdateDT(Timestamp updateDT) {
		this.updateDT = updateDT;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getOperUid() {
		return operUid;
	}

	public void setOperUid(String operUid) {
		this.operUid = operUid;
	}


}
