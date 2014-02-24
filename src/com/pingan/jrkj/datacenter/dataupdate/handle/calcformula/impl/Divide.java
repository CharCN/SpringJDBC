package com.pingan.jrkj.datacenter.dataupdate.handle.calcformula.impl;

import java.math.BigDecimal;

import com.pingan.jrkj.datacenter.dataupdate.handle.calcformula.CalcFormula;

/**
 * 除法计算
 * 
 * @author heyunyang
 * 
 */
public class Divide implements CalcFormula {

	@Override
	public String execute(String a, String b) {
		// TODO Auto-generated method stub
		a = a == null || a.equals("") ? "0" : a;
		b = b == null || b.equals("") ? "0" : b;
		if(b.equals("0")){
			return new BigDecimal("0").toString();
		}
		Double d1 = Double.parseDouble(a);
		Double d2 = Double.parseDouble(b);
		Double d = d1/d2;
		return d.toString();
//		BigDecimal bd1 = new BigDecimal(a);
//		BigDecimal bd2 = new BigDecimal(b);
//		return bd1.divide(bd2);
	}

}
