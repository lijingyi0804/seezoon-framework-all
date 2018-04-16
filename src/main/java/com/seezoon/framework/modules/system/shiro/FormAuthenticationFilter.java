package com.seezoon.framework.modules.system.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

/**
 * 自定义shiro 登录filter 适合ajax 登录
 * 
 * 自定义HTTP 请求头，310 未登录
 * @author hdf
 *
 */
@Component
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		System.out.println(SecurityUtils.getSubject().isAuthenticated() );
		System.out.println(SecurityUtils.getSubject().getPrincipal() != null);
		System.out.println(JSON.toJSONString(SecurityUtils.getSubject().getPrincipal() ));
		//检查是否登录
		if (ShiroUtils.isLogin()) {
			super.onAccessDenied(request, response);
			return true;
		} else {
			//String userId = ShiroUtils.getUserId();
			HttpServletResponse res = (HttpServletResponse)response;
			HttpServletRequest req = (HttpServletRequest)request;
			String method = req.getMethod();
			//如果是跨域的options 请求则放过，复杂contentType跨域，先发一个options 请求
			if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(method)) {
				res.setStatus(HttpStatus.NEED_LOGIN.getValue());
			}
			return false;
		}
	}
}
