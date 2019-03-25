package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {

	public static boolean checkVerigyCode(HttpServletRequest request) {
		String verigyCodeExpeted=(String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		
		String verifyCodeActual=HttpServletRequestUtil.getString(request, "verifyCodeActual");
		System.out.println("verifyCodeActual="+verifyCodeActual+"    verigyCodeExpeted="+verigyCodeExpeted);
		if(verifyCodeActual==null||!verifyCodeActual.equals(verigyCodeExpeted)) {
			return false;
		}
		
		return true;
	}
}
