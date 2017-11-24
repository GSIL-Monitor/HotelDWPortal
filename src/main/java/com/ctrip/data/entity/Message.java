package com.ctrip.data.entity;

public class Message {
	public final static String SUCC = "succ";
	public final static String ERR = "err";
	private String status;
	private String msg;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Message [status=" + status + ", msg=" + msg + "]";
	}
	
}
