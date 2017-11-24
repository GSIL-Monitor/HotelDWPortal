package com.ctrip.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AssertionHolder;

import com.ctrip.data.service.IShardingUserInfoService;
import com.ctrip.data.service.impl.ShardingUserInfoServiceImpl;
import com.ctrip.data.util.JSONUtil;

/**
 * Servlet implementation class ShardingUserServlet
 */
public class ShardingUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IShardingUserInfoService iShardingUserInfoService = null;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		try {
			iShardingUserInfoService = new ShardingUserInfoServiceImpl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShardingUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if ("checkShardingUserInfo".equals(type)) {
			checkShardingUserInfo(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void checkShardingUserInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
			boolean flag = iShardingUserInfoService.isExistUserName(userName);
			if (flag) {
				String json = "{\"flag\":\"1\"}";
				JSONUtil.writeJsonToResponse(response, json, JSONUtil.OBJECT_TYPE_BEAN);
			}else{
				String json = "{\"flag\":\"0\"}";
				JSONUtil.writeJsonToResponse(response, json, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
