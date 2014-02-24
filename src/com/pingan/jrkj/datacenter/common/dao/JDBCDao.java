package com.pingan.jrkj.datacenter.common.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCDao {

	private static final Logger logger = LoggerFactory.getLogger(JDBCDao.class);

	private JdbcTemplate dcdsJdbcTemplate;

	public void setDcdsJdbcTemplate(JdbcTemplate dcdsJdbcTemplate) {
		this.dcdsJdbcTemplate = dcdsJdbcTemplate;
	}

	public List<Map<String, Object>> find(String findSql) {
		logger.debug(findSql);
		return dcdsJdbcTemplate.queryForList(findSql);
	}

	public List<Map<String, Object>> find(String findSql, Object... args) {
		logger.debug(findSql);
		return dcdsJdbcTemplate.queryForList(findSql, args);
	}

	public Map<String, Object> findByUnique(String findSql) {
		logger.debug(findSql);
		return dcdsJdbcTemplate.queryForList(findSql).get(0);
	}

	public int update(String updateSql) {
		logger.debug(updateSql);
		return dcdsJdbcTemplate.update(updateSql);
	}

	public int update(String updateSql, Object... args) {
		logger.debug(updateSql);
		return dcdsJdbcTemplate.update(updateSql, args);
	}

}
