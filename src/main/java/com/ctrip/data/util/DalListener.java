package com.ctrip.data.util;

import com.ctrip.platform.dal.dao.DalClientFactory;

public class DalListener {
	public DalListener(){
		try {
			DalClientFactory.initClientFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
