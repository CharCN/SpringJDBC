package com.pingan.jrkj.datacenter.dataload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.pingan.jrkj.datacenter.dataload.entity.User;

public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private JdbcTemplate dcdsJdbcTemplate;

	public void setDcdsJdbcTemplate(JdbcTemplate dcdsJdbcTemplate) {
		this.dcdsJdbcTemplate = dcdsJdbcTemplate;
	}

	public void add(User user) throws Exception {
		logger.info("add User " + user.toString());
		dcdsJdbcTemplate.execute("insert into test(name, age) values('" + user.getName() + "', " + user.getAge() + ")");
		throw new Exception();
	}

}
