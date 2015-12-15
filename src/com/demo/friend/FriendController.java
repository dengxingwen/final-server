package com.demo.friend;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.TxtMessage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.demo.base.BaseController;
import com.demo.base.BaseResponse;
import com.demo.base.Constant;
import com.demo.user.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.ext.interceptor.POST;

public class FriendController extends BaseController {

	/**
	 * 添加好友请求
	 */
	@ClearInterceptor
	public void add() {
		String fromID = getPara("fromId");
		String toID = getPara("toId");
		List<String> toIds = new ArrayList<String>();
		toIds.add(toID);
		/*
		 * SdkHttpResult skdHttpResult = null; try { skdHttpResult =
		 * ApiHttpClient.publishSystemMessage(Constant.KEY, Constant.SECRET,
		 * fromID, toIds, new TxtMessage("addRequest"), "添加好友", "pushData",
		 * FormatType.json); } catch (Exception e) { e.printStackTrace(); }
		 */
		renderJson("");
	}

	public void addRequest() {
		String fromID = getPara("fromId");
		String toID = getPara("toId");
		Friend friend = new Friend();
		friend.put("addId", fromID);
		friend.put("addedId", toID);
		BaseResponse bResponse = new BaseResponse();
		List<String> list = new ArrayList();
		list.add(toID);
		if (!Friend.me.isHave(fromID, toID)) {
			if (friend.save()) {
				bResponse.setCode(200);// 添加成功
				bResponse.setResult(User.me.findById(toID));
				try {
					ApiHttpClient.publishMessage(Constant.KEY, Constant.SECRET, fromID, list,
							new TxtMessage("我们已经是好友了，开始聊天吧！"), FormatType.json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				bResponse.setCode(400);// 添加失败
			}
		} else {
			bResponse.setCode(600);// 已经是好友
		}

		renderJson(bResponse);
	}

	/**
	 * 查找好友
	 */
	@ClearInterceptor
	public void search() {
		BaseResponse bResponse = new BaseResponse();
		String account = getPara("account");
		String myaccount = getPara("myaccount");
		String sql_ = "SELECT * FROM user WHERE account=?;";

		User nowUser = User.me.findFirst(sql_, account);

		if (nowUser == null) {
			bResponse.setCode(400);
			bResponse.setResult("查无此人");
			renderJson(bResponse);
			return;
		}

		User myUser = User.me.findById(myaccount);
		String addId = myUser.getInt("id") + "";
		String addedId = nowUser.getInt("id") + "";
		if (Friend.me.isHave(addId, addedId)) {
			bResponse.setCode(600);
			bResponse.setResult(nowUser);
			renderJson(bResponse);
			return;
		}
		bResponse.setCode(200);
		bResponse.setResult(nowUser);
		renderJson(bResponse);
	}

	/**
	 * 同意添加请求
	 */
	@Before(POST.class)
	public void agree() {
		String fromID = getPara("fromId");
		String toID = getPara("toId");
		Friend friend = new Friend();
		friend.put("addId", fromID);
		friend.put("addedId", toID);
		BaseResponse bResponse = new BaseResponse();
		if (friend.save()) {
			bResponse.setCode(200);
			renderJson(bResponse);
		} else {
			bResponse.setCode(400);
			renderJson(bResponse);
		}
	}

	@ClearInterceptor
	public void getFriend() {
		String fromId = getPara("fromId");
		String sql = "SELECT * FROM friend WHERE addId=?;";
		List<Friend> list = Friend.me.find(sql, fromId);
		List<User> users = User.me.findAddedUserList(list);
		String sqlSecount = "SELECT * FROM friend WHERE addedId=?;";
		List<Friend> listScound = Friend.me.find(sqlSecount, fromId);
		users.addAll(User.me.findAddUserList(listScound));
		BaseResponse bResponse = new BaseResponse();
		if (users.size() > 0)
			bResponse.setCode(200);
		else
			bResponse.setCode(400);
		bResponse.setResult(users);
		renderJson(bResponse);
	}

	// 0.0558795
	//select * from user where latitude < 36.087886 and latitude > 36.079884 and longitude < 120.393906 and longitude > 120.371904 and id != 1;
	public void getNearByMe() {
		String fromId = getPara("id");
		Double latitude = Double.parseDouble(getPara("latitude", "0"));
		Double longitude = Double.parseDouble(getPara("longitude", "0"));
		String sql = "SELECT * FROM user where latitude < "
				+ (latitude + 0.004501) + " and latitude > "
				+ (latitude - 0.004501) + " and longitude < "
				+ (longitude + 0.0558795) + " and longitude > "
				+ (longitude - 0.0558795) + " and id != " + fromId + ";";
		List<User> listScound = User.me.find(sql);
		for (int i = listScound.size() - 1; i >= 0; i--) {
			String addId = fromId;
			String addedId = listScound.get(i).getInt("id")+"";
			if (Friend.me.isHave(addId, addedId)) {
				listScound.remove(i);
			}
		}
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setResult(listScound);
		baseResponse.setCode(200);
		renderJson(baseResponse);
		System.out.println(sql);
	}
	
	public void delete() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setCode(400);
		String fromID = getPara("fromId");
		String toID = getPara("id");
		String sql = "select * from friend where addId = ? and addedId = ?";
		Friend friend = Friend.me.findFirst(sql, fromID, toID);
		if(friend != null) {
			if(friend.delete()) {
				baseResponse.setCode(200);
			}
		}
		Friend friend_ = Friend.me.findFirst(sql, toID, fromID);
		if(friend_ != null) {
			if(friend_.delete()) {
				baseResponse.setCode(200);
			}
		}
		renderJson(baseResponse);
	}

}
