package com.ctrip.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctrip.data.entity.osg.TableInfo;
import com.ctrip.data.service.IOSGService;
import com.ctrip.data.service.impl.OSGServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.AssertionHolder;

import com.ctrip.data.dao.ShardingTBInfoDao_bak;
import com.ctrip.data.entity.Message;
import com.ctrip.data.entity.ShardingSourceTBInfo;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.service.IShardingSourceTBInfoService;
import com.ctrip.data.service.impl.ShardingSourceTBInfoServiceImpl;
import com.ctrip.data.util.CacheMap;
import com.ctrip.data.util.JSONUtil;
import com.ctrip.platform.dal.dao.DalClientFactory;

import net.sf.json.JSONArray;

/**
 *
 * @Description:
 * @author j_le
 * @version 创建时间：2017年4月11日 下午9:13:53
 * 
 */
public class ShardingTBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IShardingSourceTBInfoService iShardingSourceTBInfoService = null;
	IOSGService iOSGService =null;
	CacheMap<String, List> cacheMap = null;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		try {
			iShardingSourceTBInfoService = new ShardingSourceTBInfoServiceImpl();
			iOSGService = new OSGServiceImpl();
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
	public ShardingTBServlet() {
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
		// if ("getShardingSourceTBInfo".equals(type)) {
		// getShardingSourceTBInfo(request, response);
		// } else
		if ("getSourceTBInfo".equals(type)) {
			getSourceTBInfo(response);
		} else if ("shardingtbPreView".equals(type)) {
			shardingtbPreView(request, response);
		} else if ("saveShardingSourceTBInfo".equals(type)) {
			saveShardingSourceTBInfo(request, response);
		} else if ("getShardingSourceTBByID".equals(type)) {
			getShardingSourceTBByID(request, response);
		} else if ("shardingtbSourceDel".equals(type)) {
			shardingtbSourceDel(request, response);
		} else if ("shardingtbSave".equals(type)) {
			shardingtbSave(request, response);
		} else if ("checkShardingSourceTB".equals(type)) {
			checkShardingSourceTB(request, response);
		} else if ("shardingtbDel".equals(type)) {
			shardingtbDel(request, response);
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

	/**
	 * 
	 * @Description:删除tb源下的所有shardingtb
	 */
	private void shardingtbDel(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer id = Integer.valueOf(request.getParameter("sourceTBID"));
			List<ShardingTBInfo> shardingTBInfoList = iShardingSourceTBInfoService.getShardingTBInfoList(id);
			boolean flag = iShardingSourceTBInfoService.deleteShardingTBBySourceDBId(shardingTBInfoList);
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
	 * @Description:删除TB源和其下的shardingtb
	 */
	private void shardingtbSourceDel(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer id = Integer.valueOf(request.getParameter("shardingSourceTBID"));
			boolean flag = iShardingSourceTBInfoService.deleteShardingSourceTBInfoById(id);
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
	 * @Description:保存shardingtb
	 */
//	private void shardingtbSave(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			response.setCharacterEncoding("utf-8");
//			PrintWriter pw = response.getWriter();
//			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
//			List<ShardingTBInfo> list = new ArrayList<ShardingTBInfo>();
//			String shardingPrefix = request.getParameter("shardingPrefix");
//			int shardingNum = Integer.parseInt(StringUtils.isBlank(request.getParameter("shardingNum")) ? "0"
//					: request.getParameter("shardingNum"));
//			int sourceTBID = Integer.parseInt(
//					StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
//			if (shardingNum == 1) {
//				ShardingTBInfo stb = new ShardingTBInfo();
//				stb.setId(1);
//				stb.setSourceTBID(sourceTBID);
//				stb.setShardingTB(shardingPrefix);
//				stb.setIsValid("T");
//				stb.setOperUid(userName);
//				list.add(stb);
//				boolean flag = iShardingSourceTBInfoService.insertBatch(list);
//				if (flag) {
//					List<ShardingTBInfo> tbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
//					if (tbList != null && tbList.size() > 0) {
//						JSONArray jsonList = JSONArray.fromObject(tbList);
//						String json = "{\"total\":" + tbList.size() + ",\"rows\":" + jsonList + "}";
//						pw.print(json);
//					}
//				}
//			} else if (shardingNum > 1) {
//				for (int i = 1; i <= shardingNum; i++) {
//					ShardingTBInfo stb = new ShardingTBInfo();
//					stb.setId(i);
//					stb.setSourceTBID(sourceTBID);
//					if (i < 10) {
//						stb.setShardingTB(shardingPrefix + '0' + i);
//					} else {
//						stb.setShardingTB(shardingPrefix + i);
//					}
//					stb.setIsValid("T");
//					stb.setOperUid(userName);
//					list.add(stb);
//				}
//				boolean flag = iShardingSourceTBInfoService.insertBatch(list);
//				if (flag) {
//					List<ShardingTBInfo> tbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
//					if (tbList != null && tbList.size() > 0) {
//						JSONArray jsonList = JSONArray.fromObject(tbList);
//						String json = "{\"total\":" + tbList.size() + ",\"rows\":" + jsonList + "}";
//						pw.print(json);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private void shardingtbSave(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String userName = AssertionHolder.getAssertion().getPrincipal().getName();
		String ALLShardingTB = request.getParameter("ALLShardingTB");
		int sourceTBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
		List<ShardingTBInfo> sdbList = new ArrayList<ShardingTBInfo>();
		String jSONArrayString = "[" + ALLShardingTB + "]";
		JSONArray jsonList1 = JSONArray.fromObject(jSONArrayString);
		//System.out.println(jsonList1);
		for(int i = 0;i < jsonList1.size();i++){
			JSONObject jSONObject = jsonList1.getJSONObject(i);
			ShardingTBInfo stb = new ShardingTBInfo();
			stb.setId(i);
			stb.setShardingTB(jSONObject.get("table_name").toString());
			stb.setIsValid("T");
			stb.setOperUid(userName);
			stb.setSourceTBID(sourceTBID);
			sdbList.add(stb);
		}
		try {
			iShardingSourceTBInfoService.insertBatch(sdbList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<ShardingTBInfo> tbList = new ArrayList<ShardingTBInfo>();
		try {
			tbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (tbList != null && tbList.size() > 0) {
			JSONArray jsonList = JSONArray.fromObject(tbList);
			String json = "{\"total\":" + tbList.size() + ",\"rows\":" + jsonList + "}";
			pw.print(json);
		}
	}

	/**
	 * 
	 * @Description:校验tb源是否存在
	 */
	private void checkShardingSourceTB(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sourceTB = request.getParameter("sourceTB");
			boolean flag = iShardingSourceTBInfoService.isExistShardingSourceTB(sourceTB);
			if (flag) {
				Message msg = new Message();
				msg.setStatus(Message.SUCC);
				msg.setMsg("[" + sourceTB + "]已经存在,请换一个TB源名称!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 根据id查询ShardingSourceTB
	 */
	private void getShardingSourceTBByID(HttpServletRequest request, HttpServletResponse response) {
		try {
			String id = request.getParameter("shardingSourceTBID");
			ShardingSourceTBInfo sstb = iShardingSourceTBInfoService.getShardingSourceTBById(Integer.valueOf(id));
			if (sstb != null) {
				JSONUtil.writeJsonToResponse(response, sstb, JSONUtil.OBJECT_TYPE_BEAN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:updateType：0为update,1为insert
	 */
	private void saveShardingSourceTBInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
			String shardingSourceTBID = request.getParameter("shardingSourceTBID");
			String sourceDBID = request.getParameter("sourceDBID");
			String sourceTB = request.getParameter("sourceTB");
//			String columns = request.getParameter("columns");
//			String pKColumns = request.getParameter("pKColumns");
			String incrCondition = request.getParameter("incrCondition");
			String thresholdValue = request.getParameter("thresholdValue");
			String targetDB = request.getParameter("targetDB");
			String targetTB = request.getParameter("targetTB");
			String comment = request.getParameter("comment");
			String isValid = request.getParameter("isValid");
			String updateType = request.getParameter("updateType");
			ShardingSourceTBInfo sstb = new ShardingSourceTBInfo();
			Message msg = new Message();
			if (StringUtils.isNotBlank(sourceDBID)) {
				sstb.setSourceDBID(Integer.valueOf(sourceDBID.trim()));
			}
			if (StringUtils.isNotBlank(sourceTB)) {
				sstb.setSourceTB(sourceTB.trim());
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("ShardingTB源名称不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
//			if (StringUtils.isNotBlank(columns)) {
//				sstb.setColumns(columns.trim());
//			} else {
//				msg.setStatus(Message.ERR);
//				msg.setMsg("字段列表不能为空!");
//				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
//			}
//			if (StringUtils.isNotBlank(pKColumns)) {
//				sstb.setpKColumns(pKColumns.trim());
//			} else {
//				msg.setStatus(Message.ERR);
//				msg.setMsg("源表主键不能为空!");
//				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
//			}
			if (StringUtils.isNotBlank(incrCondition)) {
				sstb.setIncrCondition(incrCondition.trim());
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("增量取数条件IncrCondition不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			if (StringUtils.isNotBlank(thresholdValue)) {
				sstb.setThresholdValue(Integer.valueOf(thresholdValue.trim()));
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("单个sharding表对应分区文件大小阀值（单位Ｋ） thresholdValue不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			if (StringUtils.isNotBlank(targetDB)) {
				sstb.setTargetDB(targetDB.trim());
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("HIVE目的库TargetDB不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			if (StringUtils.isNotBlank(targetTB)) {
				sstb.setTargetTB(targetTB.trim());
			} else {
				msg.setStatus(Message.ERR);
				msg.setMsg("HIVE目的表TargetTB不能为空!");
				JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
			}
			sstb.setComment(comment);
			sstb.setIsValid(isValid);
			sstb.setOperUid(userName);
			if ("add".equals(updateType)) {
				sstb.setInsertDT(new Timestamp(System.currentTimeMillis()));
				sstb.setUpdateDT(new Timestamp(System.currentTimeMillis()));
				sstb.setpKColumns("");
				sstb.setColumns("");
				int cnt;
				try {
					cnt = iShardingSourceTBInfoService.insert(sstb);
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
				if (StringUtils.isNotBlank(shardingSourceTBID)) {
					sstb.setId(Integer.valueOf(shardingSourceTBID.trim()));
				} else {
					msg.setStatus(Message.ERR);
					msg.setMsg("主键id不能为空!");
					JSONUtil.writeJsonToResponse(response, msg, JSONUtil.OBJECT_TYPE_BEAN);
				}
				sstb.setUpdateDT(new Timestamp(System.currentTimeMillis()));
				ShardingSourceTBInfo shardingSourceTBInfo = iShardingSourceTBInfoService.getShardingSourceTBById(Integer.valueOf(shardingSourceTBID.trim()));
				sstb.setColumns(shardingSourceTBInfo.getColumns());
				sstb.setpKColumns(shardingSourceTBInfo.getpKColumns());
				try {
					int cnt = iShardingSourceTBInfoService.update(sstb);
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

	/**
	 * 
	 * @Description:生成预览shardingtb
	 */
//	private void shardingtbPreView(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			response.setCharacterEncoding("utf-8");
//			PrintWriter pw = response.getWriter();
//			String userName = AssertionHolder.getAssertion().getPrincipal().getName();
//			String shardingPrefix = request.getParameter("shardingPrefix");
//			int shardingNum = Integer.parseInt(StringUtils.isBlank(request.getParameter("shardingNum")) ? "0"
//					: request.getParameter("shardingNum"));
//			int sourceTBID = Integer.parseInt(
//					StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
//			if (sourceTBID > 0) {
//				List<ShardingTBInfo> stbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
//				int num = 0;
//				if (stbList != null && stbList.size() > 0) {
//					num = stbList.size();
//				} else {
//					stbList = new ArrayList<ShardingTBInfo>();
//				}
//				if (shardingNum == 1) {
//					ShardingTBInfo stb = new ShardingTBInfo();
//					stb.setId(num + 1);
//					stb.setShardingTB(shardingPrefix);
//					stb.setIsValid("T");
//					stb.setOperUid(userName);
//					stbList.add(stb);
//				} else if (shardingNum > 1) {
//					for (int i = 1; i <= shardingNum; i++) {
//						ShardingTBInfo stb = new ShardingTBInfo();
//						stb.setId(i + num);
//						if (i < 10) {
//							stb.setShardingTB(shardingPrefix + '0' + i);
//						} else {
//							stb.setShardingTB(shardingPrefix + i);
//						}
//						stb.setIsValid("T");
//						stb.setOperUid(userName);
//						stbList.add(stb);
//					}
//				}
//				JSONArray jsonList = JSONArray.fromObject(stbList);
//				String json = "{\"total\":" + stbList.size() + ",\"rows\":" + jsonList + "}";
//				pw.print(json);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


//	private void shardingtbPreView(HttpServletRequest request, HttpServletResponse response) {
//		response.setCharacterEncoding("utf-8");
//		String userName = AssertionHolder.getAssertion().getPrincipal().getName();
//		PrintWriter pw = null;
//		try {
//			pw = response.getWriter();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		String ALLShardingTB = request.getParameter("ALLShardingTB");
//		int sourceTBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
//		//System.out.println(ALLShardingTB);
//		//System.out.println(sourceTBID);
//		if (sourceTBID > 0) {
//			List<ShardingTBInfo> stbList = null;
//			try {
//				stbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			if(!ALLShardingTB.equals("0")){
//				String[] strArray = null;
//				strArray = ALLShardingTB.split(",");
//				for(int i = 0;i < strArray.length;i++){
//					ShardingTBInfo stb = new ShardingTBInfo();
//					stb.setId(i);
//					stb.setShardingTB(strArray[i]);
//					stb.setIsValid("T");
//					stb.setOperUid(userName);
//					stb.setSourceTBID(sourceTBID);
//					stbList.add(stb);
//				}
//			}
//			JSONArray jsonList = JSONArray.fromObject(stbList);
//			String json = "{\"total\":" + stbList.size() + ",\"rows\":" + jsonList + "}";
//			//System.out.println(json);
//			pw.print(json);
//		}
//	}

	private void shardingtbPreView(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String userName = AssertionHolder.getAssertion().getPrincipal().getName();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ALLShardingTB = request.getParameter("ALLShardingTB");
		//System.out.println(ALLShardingTB);
		int sourceTBID = Integer.parseInt(StringUtils.isBlank(request.getParameter("sourceTBID")) ? "0" : request.getParameter("sourceTBID"));
		//System.out.println(sourceTBID);
		if (sourceTBID > 0) {
			List<ShardingTBInfo> stbList = null;
			try {
				stbList = iShardingSourceTBInfoService.getShardingTBInfoList(sourceTBID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(!ALLShardingTB.equals("0")){
				String jSONArrayString = "[" + ALLShardingTB + "]";
				JSONArray jsonList1 = JSONArray.fromObject(jSONArrayString);
				//System.out.println(jsonList1);
				for(int i = 0;i < jsonList1.size();i++){
					JSONObject jSONObject = jsonList1.getJSONObject(i);
					ShardingTBInfo stb = new ShardingTBInfo();
					stb.setId(i + 1);
					stb.setShardingTB(jSONObject.get("table_name").toString());
					stb.setIsValid("T");
					stb.setOperUid(userName);
					stb.setSourceTBID(sourceTBID);
					stbList.add(stb);
				}
			}
			JSONArray jsonList = JSONArray.fromObject(stbList);
			String json = "{\"total\":" + stbList.size() + ",\"rows\":" + jsonList + "}";
			//System.out.println(json);
			pw.print(json);
		}
	}

	/**
	 * 
	 * @Description:获取shardingtb
	 */
	private void getSourceTBInfo(HttpServletResponse response) {
		try {
			List list = cacheMap.get("shardingSourceTBInfo");
			if (list == null) {
				list = new ArrayList<ShardingSourceTBInfo>();
			}
			if (list.size() > 0) {
				JSONUtil.writeJsonToResponse(response, list, JSONUtil.OBJECT_TYPE_LIST);
			} else {
				list = iShardingSourceTBInfoService.getAll();
				if (list != null && list.size() > 0) {
					cacheMap.put("shardingSourceTBInfo", list);
					JSONUtil.writeJsonToResponse(response, list, JSONUtil.OBJECT_TYPE_LIST);
				} else {
					JSONUtil.writeJsonToResponse(response, new ArrayList<ShardingSourceTBInfo>(),
							JSONUtil.OBJECT_TYPE_LIST);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:更新shardingtb
	 */
	private void updateCache() {
		try {
			List list = cacheMap.get("shardingSourceTBInfo");
			if (list == null) {
				list = new ArrayList<ShardingSourceTBInfo>();
			}
			list = iShardingSourceTBInfoService.getAll();
			if (list != null && list.size() > 0) {
				cacheMap.put("shardingSourceTBInfo", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:获取shardingSourceTBInfo
	 */
	/*
	 * private void getShardingSourceTBInfo(HttpServletRequest request,
	 * HttpServletResponse response) { try {
	 * response.setCharacterEncoding("utf-8"); PrintWriter pw =
	 * response.getWriter(); int pageSize =
	 * Integer.parseInt(request.getParameter("pageSize")); int pageNumber =
	 * Integer.parseInt(request.getParameter("pageNumber")); String srcDB =
	 * request.getParameter("srcDB"); String srcTB =
	 * request.getParameter("srcTB"); int count =
	 * iShardingSourceTBInfoService.count(srcDB,srcTB); if (count > 0) {
	 * List<ShardingSourceTBInfo> sourceList =
	 * iShardingSourceTBInfoService.getShardingSourceTBInfoList(pageSize,
	 * pageNumber,srcDB,srcTB); JSONArray jsonList =
	 * JSONArray.fromObject(sourceList); String json = "{\"total\":" + count +
	 * ",\"rows\":" + jsonList + "}"; pw.print(json); } else { JSONArray
	 * jsonList = JSONArray.fromObject(new ArrayList<ShardingSourceTBInfo>());
	 * String json = "{\"total\":" + 0 + ",\"rows\":" + jsonList + "}";
	 * pw.print(json); } } catch (Exception e) { e.printStackTrace(); } }
	 */

	public static void main(String[] args) {
		try {
			DalClientFactory.initClientFactory();
			ShardingTBInfoDao_bak checkInfoDao = new ShardingTBInfoDao_bak();
			List<ShardingTBInfo> checkInfos = checkInfoDao.getShardingTBInfos(1, null);
			System.out.println(checkInfos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
