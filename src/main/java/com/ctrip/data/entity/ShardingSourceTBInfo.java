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

/** 
*
* @Description:
* @author j_le E-mail: j_le@Ctrip.com
* @version 创建时间：2017年4月10日 下午3:47:34 
* 
*/

@Entity
@Database(name = "PublicDB")
@Table(name = "ShardingSourceTBInfo")

public class ShardingSourceTBInfo implements DalPojo {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(value = Types.INTEGER)
	private Integer id;
	
	@Column(name="SourceDBID")
	@Type(value=Types.INTEGER)
	private Integer sourceDBID;
	
	@Column(name="SourceTB")
	@Type(value=Types.VARCHAR)
	private String sourceTB;
	
	@Column(name="Columns")
	@Type(value=Types.NVARCHAR)
	private String columns;
	
	@Column(name="IncrCondition")
	@Type(value=Types.VARCHAR)
	private String incrCondition;
	
	@Column(name="ThresholdValue")
	@Type(value=Types.INTEGER)
	private Integer thresholdValue;
	
	@Column(name="TargetDB")
	@Type(value=Types.VARCHAR)
	private String targetDB;
	
	@Column(name="TargetTB")
	@Type(value=Types.VARCHAR)
	private String targetTB;
	
	@Column(name="Comment")
	@Type(value=Types.NVARCHAR)
	private String comment;
	
	@Column(name="InsertDT")
	@Type(value=Types.TIMESTAMP)
	private Timestamp insertDT;
	
	@Column(name="UpdateDT")
	@Type(value=Types.TIMESTAMP)
	private Timestamp updateDT;
	
	@Column(name="IsValid")
	@Type(value=Types.CHAR)
	private String isValid;

	@Column(name="PKColumns")
	@Type(value=Types.NVARCHAR)
	private String pKColumns;
	
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

	public String getSourceTB() {
		return sourceTB;
	}

	public void setSourceTB(String sourceTB) {
		this.sourceTB = sourceTB;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getIncrCondition() {
		return incrCondition;
	}

	public void setIncrCondition(String incrCondition) {
		this.incrCondition = incrCondition;
	}

	public Integer getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(Integer thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public String getTargetDB() {
		return targetDB;
	}

	public void setTargetDB(String targetDB) {
		this.targetDB = targetDB;
	}

	public String getTargetTB() {
		return targetTB;
	}

	public void setTargetTB(String targetTB) {
		this.targetTB = targetTB;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
	
	public String getpKColumns() {
		return pKColumns;
	}

	public void setpKColumns(String pKColumns) {
		this.pKColumns = pKColumns;
	}

	public String getOperUid() {
		return operUid;
	}

	public void setOperUid(String operUid) {
		this.operUid = operUid;
	}
	
}
