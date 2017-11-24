package com.ctrip.servlet;

import com.ctrip.data.dao.ShardingDBInfoDao_bak;
import com.ctrip.data.entity.Message;
import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingSourceDBInfo;
import com.ctrip.data.service.IShardingSourceDBInfoService;
import com.ctrip.data.service.impl.ShardingSourceDBInfoServiceImpl;
import com.ctrip.data.util.CacheMap;
import com.ctrip.data.util.JSONUtil;
import com.ctrip.platform.dal.dao.DalClientFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.AssertionHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class SourceDBInfoServlet
 */
public class ShardingDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IShardingSourceDBInfoService iShardingSourceDBInfoService = null;
	CacheMap<String, List> cacheMap = null;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		try {
			iShardingSourceDBInfoService = new ShardingSourceDBInfoServiceImpl();
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
	public ShardingDBServlet() {
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
		if ("getShardingSourceDBInfo".equals(type)) {
			getShardingSourceDBInfo(request, response);
		} else if ("getSourceDBInfo".equals(type)) {
			getSourceDBInfo(response);
		} else if ("shardingdbPreView".equals(type)) {
			shardingdbPreView(request, response);
		} else if ("saveShardingSourceDBInfo".equals(type)) {
			saveShardingSourceDBInfo(request, response);
		} else if ("getShardingSourceDBByID".equals(type)) {
			getShardingSourceDBByID(request, response);
		} else if ("shardingdbSourceDel".equals(type)) {
			shardingdbSourceDel(request, response);
		} else if ("shardingdbSave".equals(type)) {
			shardingdbSave(request, response);
		} else if ("checkShardingSourceDB".equals(type)) {
			checkShardingSourceDB(request, response);
		} else if ("shardingdbDel".equals(type)) {
			shardingdbDel(request, response);
//		} else if ("getShardingDBInfoTopOne".equals(type)) {
//			getShardingDBInfoTopOne(request, response);
		}
	}

	/**
	 * 
	 * @Description:删除db源下的所有shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午5:08:21
	 */
	private void shardingdbDel(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer id = Integer.valueOf(request.getParameter("sourceDBID"));
			List<ShardingDBInfo> shardingDBInfoList = iShardingSourceDBInfoService.getShardingDBInfoList(id);
			boolean flag = iShardingSourceDBInfoService.deleteShardingDBBySourceDBId(shardingDBInfoList);
			if (flag) {
				Message msg = new Message();
				msg.setStatus(Message.SUCC);
				msg.setMsg("删除成功!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:删除DB源和其下的shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午3:46:03
	 */
	private void shardingdbSourceDel(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer id = Integer.valueOf(request.getParameter("shardingSourceID"));
			boolean flag = iShardingSourceDBInfoService.deleteShardingSourceDBInfoById(id);
			if (flag) {
				Message msg = new Message();
				msg.setStatus(Message.SUCC);
				msg.setMsg("删除成功!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			updateCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:保存shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午3:45:34
	 */
	private void shardingdbSave(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
			List<ShardingDBInfo> list = new ArrayList<ShardingDBInfo>();
			String shardingPrefix = request.getParameter("shardingPrefix");
			int shardingNum = Integer.parseInt(StringUtils.isBlank(request.getParameter("shardingNum")) ? "0"
					: request.getParameter("shardingNum"));
			int sourceDBID = Integer.parseInt(
					StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
			if (shardingNum == 1) {
				ShardingDBInfo sdb = new ShardingDBInfo();
				sdb.setId(1);
				sdb.setSourceDBID(sourceDBID);
				sdb.setShardingDB(shardingPrefix + "DB");
				sdb.setIsValid("T");
				sdb.setOperUid(userName);
				list.add(sdb);
				boolean flag = iShardingSourceDBInfoService.insertBatch(list);
				if (flag) {
					List<ShardingDBInfo> dbList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
					if (dbList != null && dbList.size() > 0) {
						JSONArray jsonList = JSONArray.fromObject(dbList);
						String json = "{\"total\":" + dbList.size() + ",\"rows\":" + jsonList + "}";
						pw.print(json);
					}
				}
			} else if (shardingNum > 1) {
				for (int i = 1; i <= shardingNum; i++) {
					ShardingDBInfo sdb = new ShardingDBInfo();
					sdb.setId(i);
					sdb.setSourceDBID(sourceDBID);
					if (i < 10) {
						sdb.setShardingDB(shardingPrefix + "0" + i + "DB");
					} else {
						sdb.setShardingDB(shardingPrefix + i + "DB");
					}
					sdb.setIsValid("T");
					sdb.setOperUid(userName);
					list.add(sdb);
				}
				boolean flag = iShardingSourceDBInfoService.insertBatch(list);
				if (flag) {
					List<ShardingDBInfo> dbList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
					if (dbList != null && dbList.size() > 0) {
						JSONArray jsonList = JSONArray.fromObject(dbList);
						String json = "{\"total\":" + dbList.size() + ",\"rows\":" + jsonList + "}";
						pw.print(json);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Description:校验db源是否存在
	 * 
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午3:36:04
	 */
	private void checkShardingSourceDB(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sourceDB = request.getParameter("sourceDB");
			boolean flag = iShardingSourceDBInfoService.isExistShardingSourceDB(sourceDB);
			if (flag) {
				Message msg = new Message();
				msg.setStatus(Message.SUCC);
				msg.setMsg("[" + sourceDB + "]已经存在,请换一个DB源名称!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 根据id查询shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 上午9:26:31
	 */
	private void getShardingSourceDBByID(HttpServletRequest request, HttpServletResponse response) {
		try {
			String id = request.getParameter("shardingSourceID");
			ShardingSourceDBInfo ssdb = iShardingSourceDBInfoService.getShardingSourceDBById(Integer.valueOf(id));
			if (ssdb != null) {
				JSONUtil.writeJsonToResponse(response, ssdb, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:updateType：0为update,1为insert
	 * @Auther: jy.lu
	 * @Date: 2017年3月22日 下午6:09:02
	 */
	private void saveShardingSourceDBInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
			String shardingSourceID = request.getParameter("shardingSourceID");
			String sourceType = request.getParameter("sourceType");
			String sourceDB = request.getParameter("sourceDB");
			String batchNum = request.getParameter("batchNum");
			String comment = request.getParameter("comment");
			String isValid = request.getParameter("isValid");
			String updateType = request.getParameter("updateType");
			ShardingSourceDBInfo ssdb = new ShardingSourceDBInfo();
			Message msg = new Message();
			if (StringUtils.isNotBlank(sourceType)) {
				ssdb.setSourceType(sourceType.trim());
			}
			if (StringUtils.isNotBlank(sourceDB)) {
				ssdb.setSourceDB(sourceDB.trim());
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("ShardingDB源名称不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			if (StringUtils.isNotBlank(batchNum)) {
				ssdb.setBatchNum(Integer.valueOf(batchNum.trim()));
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("batchNum不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			ssdb.setComment(comment);
			ssdb.setIsValid(isValid);
			ssdb.setOperUid(userName);
			if ("add".equals(updateType)) {
				ssdb.setInsertDT(new Timestamp(System.currentTimeMillis()));
				ssdb.setUpdateDT(new Timestamp(System.currentTimeMillis()));
				try {
					int cnt = iShardingSourceDBInfoService.insert(ssdb);
					if (cnt > 0) {
						msg.setMsg("保存成功!");
						msg.setStatus(Message.SUCC);
					} else {
						msg.setMsg("保存失败!");
						msg.setStatus(Message.ERR);
					}
				} catch (Exception e) {
					msg.setMsg(e.getMessage());
					msg.setStatus(Message.ERR);
				}
			} else if ("update".equals(updateType)) {
				if (StringUtils.isNotBlank(shardingSourceID)) {
					ssdb.setId(Integer.valueOf(shardingSourceID.trim()));
				} else {
					msg.setStatus(Message.ERR);
					msg.setMsg("主键id不能为空!");
					JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
				}
				ssdb.setUpdateDT(new Timestamp(System.currentTimeMillis()));
				try {
					int cnt = iShardingSourceDBInfoService.update(ssdb);
					if (cnt > 0) {
						msg.setMsg("更新成功!");
						msg.setStatus(Message.SUCC);
					} else {
						msg.setMsg("更新失败!");
						msg.setStatus(Message.ERR);
					}
				} catch (Exception e) {
					msg.setMsg(e.getMessage());
					msg.setStatus(Message.ERR);
				}
			}
			updateCache();
			JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void shardingdbPreView(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
			String shardingPrefix = request.getParameter("shardingPrefix");
			int shardingNum = Integer.parseInt(StringUtils.isBlank(request.getParameter("shardingNum")) ? "0"
					: request.getParameter("shardingNum"));
			int sourceDBID = Integer.parseInt(
					StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
			if (sourceDBID > 0) {
				List<ShardingDBInfo> sdbList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
				int num = 0;
				if (sdbList != null && sdbList.size() > 0) {
					num = sdbList.size();
				} else {
					sdbList = new ArrayList<ShardingDBInfo>();
				}
				if (shardingNum == 1) {
					ShardingDBInfo sdb = new ShardingDBInfo();
					sdb.setId(1);
					sdb.setShardingDB(shardingPrefix + "DB");
					sdb.setIsValid("T");
					sdb.setOperUid(userName);
					sdbList.add(sdb);
				}
				if (shardingNum > 1) {
					for (int i = 1; i <= shardingNum; i++) {
						ShardingDBInfo sdb = new ShardingDBInfo();
						sdb.setId(i + num);
						if (i < 10) {
							sdb.setShardingDB(shardingPrefix + "0" + i + "DB");
						} else {
							sdb.setShardingDB(shardingPrefix + i + "DB");
						}
						sdb.setIsValid("T");
						sdb.setOperUid(userName);
						sdbList.add(sdb);
					}
				}
				JSONArray jsonList = JSONArray.fromObject(sdbList);
				String json = "{\"total\":" + sdbList.size() + ",\"rows\":" + jsonList + "}";
				pw.print(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getSourceDBInfo(HttpServletResponse response) {
		try {
			List list = cacheMap.get("shardingSourceDBInfo");
			if (list == null) {
				list = new ArrayList<ShardingSourceDBInfo>();
			}
			if (list.size() > 0) {
				JSONUtil.writeJsonToResponse(response, list, JSONUtil.OBJECT_TYPE_LIST);
			} else {
				list = iShardingSourceDBInfoService.getAll();
				if (list != null && list.size() > 0) {
					cacheMap.put("shardingSourceDBInfo", list);
					JSONUtil.writeJsonToResponse(response, list, JSONUtil.OBJECT_TYPE_LIST);
				} else {
					JSONUtil.writeJsonToResponse(response, new ArrayList<ShardingSourceDBInfo>(),
							JSONUtil.OBJECT_TYPE_LIST);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCache() {
		try {
			List list = cacheMap.get("shardingSourceDBInfo");
			if (list == null) {
				list = new ArrayList<ShardingSourceDBInfo>();
			}
			list = iShardingSourceDBInfoService.getAll();
			if (list != null && list.size() > 0) {
				cacheMap.put("shardingSourceDBInfo", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getShardingSourceDBInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			String srcType = request.getParameter("srcType");
			String srcDB = request.getParameter("srcDB");
			int count = iShardingSourceDBInfoService.count(srcType, srcDB);
			if (count > 0) {
				List<ShardingSourceDBInfo> sourceList = iShardingSourceDBInfoService
						.getShardingSourceDBInfoList(pageSize, pageNumber, srcType, srcDB);
				JSONArray jsonList = JSONArray.fromObject(sourceList);
				String json = "{\"total\":" + count + ",\"rows\":" + jsonList + "}";
				pw.print(json);
			} else {
				JSONArray jsonList = JSONArray.fromObject(new ArrayList<ShardingSourceDBInfo>());
				String json = "{\"total\":" + 0 + ",\"rows\":" + jsonList + "}";
				pw.print(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void getShardingDBInfoTopOne(HttpServletRequest request, HttpServletResponse response) {
//		response.setCharacterEncoding("utf-8");
//		PrintWriter pw = null;
//		try {
//			pw = response.getWriter();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		int sourceDBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceDBID")) ? "0" : request.getParameter("sourceDBID"));
//		List<ShardingDBInfo> shardingDBInfoList = null;
//		try {
//			shardingDBInfoList = iShardingSourceDBInfoService.getShardingDBInfoList(sourceDBID);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		JSONArray jsonList = JSONArray.fromObject(shardingDBInfoList);
//		JSONObject reault = jsonList.getJSONObject(0);
//		pw.print(reault);
//	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public static void main(String[] args) {
		try {
			DalClientFactory.initClientFactory();
			ShardingDBInfoDao_bak checkInfoDao = new ShardingDBInfoDao_bak();
			List<ShardingDBInfo> checkInfos = checkInfoDao.getShardingDBInfos(1, null);
			System.out.println(checkInfos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
