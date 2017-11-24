package com.ctrip.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctrip.data.entity.ShardingSourceGroup;
import com.ctrip.data.service.IShardingSourceGroupService;
import com.ctrip.data.service.impl.ShardingSourceGroupServiceImpl;
import com.ctrip.data.util.CacheMap;
import com.ctrip.data.util.JSONUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ShardingSourceGroupServlet
 */
public class ShardingSourceGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IShardingSourceGroupService iShardingSourceGroupService = null;
	CacheMap<String, List> cacheMap = null;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		try {
			iShardingSourceGroupService = new ShardingSourceGroupServiceImpl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// cacheMap = new ConcurrentHashMap<String, List>();
		cacheMap = new CacheMap<String, List>(30 * 1000);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShardingSourceGroupServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		if ("getShardingSourceGroupList".equals(type)) {
			getShardingSourceGroupList(request, response);
		} else if ("getShardingSourceGroupByID".equals(type)) {
			getShardingSourceGroupByID(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void getShardingSourceGroupList(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			String srcDB = request.getParameter("srcDB");
			String srcTB = request.getParameter("srcTB");
			int count = iShardingSourceGroupService.count(srcDB, srcTB);
			if (count > 0) {
				List<ShardingSourceGroup> sourceList = iShardingSourceGroupService.getShardingSourceGroupList(pageSize,
						pageNumber, srcDB, srcTB);
				JSONArray jsonList = JSONArray.fromObject(sourceList);
				String json = "{\"total\":" + count + ",\"rows\":" + jsonList + "}";
				pw.print(json);
			} else {
				JSONArray jsonList = JSONArray.fromObject(new ArrayList<ShardingSourceGroup>());
				String json = "{\"total\":" + 0 + ",\"rows\":" + jsonList + "}";
				pw.print(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getShardingSourceGroupByID(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			String id = request.getParameter("shardingSourceTBID");
			List<ShardingSourceGroup> sourceList = iShardingSourceGroupService
					.getShardingSourceGroupByID(Integer.valueOf(id));
			if (sourceList != null) {
				JSONArray jsonList = JSONArray.fromObject(sourceList);
				JSONObject jsonobj = jsonList.getJSONObject(0);
				JSONUtil.writeJsonToResponse(response, jsonobj, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
