package com.pingan.jrkj.datacenter.dataupdate.handle.calcformula.impl;

import java.math.BigDecimal;

import com.pingan.jrkj.datacenter.dataupdate.handle.calcformula.CalcFormula;

/**
 * 乘法计算
 * 
 * @author heyunyang
 * 
 */
public class Multiply implements CalcFormula {

	@Override
	public String execute(String a, String b) {
		// TODO Auto-generated method stub
		a = a == null || a.equals("") ? "0" : a;
		b = b == null || b.equals("") ? "0" : b;
		BigDecimal bd1 = new BigDecimal(a);
		BigDecimal bd2 = new BigDecimal(b);
		return bd1.multiply(bd2).toString();
	}

}
