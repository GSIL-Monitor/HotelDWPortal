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
* @author j_le
* @version 创建时间：2017年4月25日 上午11:12:37 
* 
*/

@Entity
@Database(name = "PublicDB")
@Table(name = "ShardingUserInfo")

public class ShardingUserInfo {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(value = Types.INTEGER)
	private Integer id;

	@Column(name = "UserName")
	@Type(value = Types.VARCHAR)
	private String userName;

	@Column(name = "InsertDT")
	@Type(value = Types.TIMESTAMP)
	private Timestamp insertDT;

	@Column(name = "UpdateDT")
	@Type(value = Types.TIMESTAMP)
	private Timestamp updateDT;

	@Column(name = "IsValid")
	@Type(value = Types.CHAR)
	private String isValid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

}
