package com.pingan.jrkj.datacenter.dataload;

import java.util.Random;

public class Test {
	public static void main(String[] args) {
		System.out.println("常量会变哦：" + Const.RAND_CONST);
		
		 
	}
}

/* 接口常量 */
interface Const {
	// 这还是常量吗？
	public static final int RAND_CONST = new Random().nextInt();
}




