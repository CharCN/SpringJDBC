package com.pingan.jrkj.datacenter.dataload.handle;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pingan.jrkj.datacenter.dataload.entity.User;
import com.pingan.jrkj.datacenter.dataload.service.UserService;

public class Test {
	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		System.out.println(ctx);

		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("dcdsJdbcTemplate");

		// jdbcTemplate.execute("insert into test(name) values('aaa')");

		List list = jdbcTemplate.queryForList("select * from AL_InputFile");

		for (Object o : list) {
			Map map = (Map) o;
			System.out.println(map.get("RecordID") + " | " + (map.get("SplitSign") == null));
		}

		UserService userService = (UserService) ctx.getBean("userService");
		User user = new User();
		user.setAge(11);
		user.setName("heyunyang");
//		userService.add(user);

	}
}
