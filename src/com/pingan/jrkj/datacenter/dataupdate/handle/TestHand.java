package com.pingan.jrkj.datacenter.dataupdate.handle;

import java.util.List;
import java.util.Map;

import com.pingan.jrkj.datacenter.dataupdate.service.UpdateService;
import com.pingan.jrkj.datacenter.dataupdate.util.ApplicationUtil;

public class TestHand {
	public static void main(String[] args) {
		ApplicationUtil.init();
		UpdateService us = (UpdateService) ApplicationUtil.getBean("updateService");
		List<Map<String, Object>> list = us.find("select * from test");
		System.out.println(list.get(0).get("Remark").toString());
		System.out.println(list);
	}
}
