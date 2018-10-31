package com.controller;

import com.alibaba.fastjson.JSON;
import com.config.Constant;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.model.ProcessInstanceInputVO;
import com.util.AccessTokenUtil;
import com.util.LogFormatter;
import com.util.LogFormatter.LogEvent;
import com.util.ServiceResult;
import com.util.ServiceResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 企业 E应用审批解决方案示例代码
 * 实现了审批的基础功能
 * 内网穿透工具地址：https://open-doc.dingtalk.com/microapp/debug/ucof2g
 * 待办接口地址：https://open-doc.dingtalk.com/microapp/serverapi2/gdzay4
 */
@RestController
public class WorkRecordController {
	private static final Logger bizLogger = LoggerFactory.getLogger(WorkRecordController.class);
	private volatile Map<String, String> localCache  = new HashMap<String, String>();

	/**
	 * 发起待办事项
	 * 文档地址：https://open-doc.dingtalk.com/microapp/serverapi2/gdzay4
	 */
	@RequestMapping(value = "/workrecord/start", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult<String> startWorkRecord() {
		try {
			String userId = "manager7078";
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
			OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
			req.setUserid(userId);
			req.setCreateTime(System.currentTimeMillis());
			req.setTitle("title");

			String id = userId + System.currentTimeMillis();
			req.setUrl(Constant.CALLBACK_URL_HOST + "/workrecord/" + id + "/update");
			List<OapiWorkrecordAddRequest.FormItemVo> list2 = new ArrayList<>();
			OapiWorkrecordAddRequest.FormItemVo obj3 = new OapiWorkrecordAddRequest.FormItemVo();
			list2.add(obj3);
			obj3.setTitle("标题");
			obj3.setContent("内容");

			OapiWorkrecordAddRequest.FormItemVo obj4 = new OapiWorkrecordAddRequest.FormItemVo();
			list2.add(obj4);
			obj4.setTitle("发起时间");
			obj4.setContent(String.valueOf(System.currentTimeMillis()));
			req.setFormItemList(list2);
			OapiWorkrecordAddResponse rsp = client.execute(req, AccessTokenUtil.getToken());
			System.out.println(JSON.toJSONString(rsp));

			if (rsp.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(rsp.getErrorCode()), rsp.getErrmsg());
			}
			localCache.put(id, rsp.getRecordId());
			return ServiceResult.success(rsp.getRecordId());

		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,"startWorkRecord fail");
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}

	/**
	 * 发起待办事项
	 * 文档地址： https://open-doc.dingtalk.com/microapp/serverapi2/sltmwf
	 */
	@RequestMapping(value = "/workrecord/{id}/update", method = RequestMethod.GET)
	@ResponseBody
	public String updateWorkRecord(@PathVariable String id) {
		try {
			if (!localCache.containsKey(id)) {
				return "can't find the workrecord";
			}
			String recordId = localCache.get(id);

			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/update");
			OapiWorkrecordUpdateRequest req = new OapiWorkrecordUpdateRequest();
			req.setUserid("manager7078");
			req.setRecordId(recordId);
			OapiWorkrecordUpdateResponse response = client.execute(req, AccessTokenUtil.getToken());
			System.out.println(JSON.toJSONString(response));

			if (response.getErrcode().longValue() != 0) {
				return "update workrecord fail";
			}
			return "待办事项更新完成";

		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,"updateWorkRecord fail");
			bizLogger.info(errLog,e);
			return "update workrecord fail";
		}
	}

	/**
	 * 获取待办事项
	 * 文档地址：https://open-doc.dingtalk.com/microapp/serverapi2/ehn6bt
	 * @return
	 */
	@RequestMapping(value = "/workrecord/get", method = RequestMethod.GET)
	@ResponseBody
	public ServiceResult getWorkRecordByUserId() {
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/getbyuserid");
			OapiWorkrecordGetbyuseridRequest req = new OapiWorkrecordGetbyuseridRequest();
			req.setUserid("manager7078");
			req.setOffset(0L);
			req.setLimit(50L);
			req.setStatus(0L);
			OapiWorkrecordGetbyuseridResponse rsp = client.execute(req, AccessTokenUtil.getToken());
			System.out.println(rsp.getBody());

			return ServiceResult.success(rsp.getRecords());
		} catch (Exception e) {
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
}


