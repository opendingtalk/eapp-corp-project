package com.controller;

import com.alibaba.fastjson.JSON;
import com.config.Constant;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
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
 */
@RestController
public class WorkRecordController {
	private static final Logger bizLogger = LoggerFactory.getLogger(WorkRecordController.class);
	private volatile Map<String, String> localCache  = new HashMap<String, String>();

	/**
	 * 发起待办事项
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
			return "update success";

		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,"updateWorkRecord fail");
			bizLogger.info(errLog,e);
			return "update workrecord fail";
		}
	}

	/**
	 * 根据审批实例id获取审批详情
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value = "/workrecord/get", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getWorkRecordByUserId(@RequestParam String instanceId) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_GET);
			OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
			request.setProcessInstanceId(instanceId);
			OapiProcessinstanceGetResponse response = client.execute(request, AccessTokenUtil.getToken());
			if (response.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(response.getErrorCode()), response.getErrmsg());
			}
			return ServiceResult.success(response.getProcessInstance());
		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,
				LogFormatter.KeyValue.getNew("instanceId", instanceId));
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
}


