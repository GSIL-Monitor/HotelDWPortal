package com.ctrip.data.entity;

import java.sql.Timestamp;
import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ctrip.platform.dal.dao.annotation.Database;
import com.ctrip.platform.dal.dao.annotation.Type;


/** 
*
* @Description:
* @author j_le E-mail: j_le@Ctrip.com
* @version 创建时间：2017年4月10日 下午3:46:34 
* 
*/

@Entity
@Database(name = "PublicDB")
@Table(name = "ShardingTBInfo")

public class ShardingTBInfo {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(value = Types.INTEGER)
	private Integer id;

	@Column(name = "SourceTBID")
	@Type(value = Types.INTEGER)
	private Integer sourceTBID;

	@Column(name = "ShardingTB")
	@Type(value = Types.VARCHAR)
	private String shardingTB;

	@Column(name = "InsertDT")
	@Type(value = Types.TIMESTAMP)
	private Timestamp insertDT;

	@Column(name = "UpdateDT")
	@Type(value = Types.TIMESTAMP)
	private Timestamp updateDT;

	@Column(name = "IsValid")
	@Type(value = Types.CHAR)
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

	public Integer getSourceTBID() {
		return sourceTBID;
	}

	public void setSourceTBID(Integer sourceTBID) {
		this.sourceTBID = sourceTBID;
	}

	public String getShardingTB() {
		return shardingTB;
	}

	public void setShardingTB(String shardingTB) {
		this.shardingTB = shardingTB;
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
