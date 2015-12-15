package com.demo.user;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import com.demo.base.BaseController;
import com.demo.base.Constant;
import com.google.gson.JsonObject;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class UserController extends BaseController {

	public void index() {
		System.out.println(getCookie("xys"));
		renderJson("{\"state\":0}");
	}

	@ClearInterceptor
	public void login() {
		SdkHttpResult result = null;
		String account = getPara("username");
		String password = getPara("password");
		String sql = "SELECT * FROM user WHERE account=? AND password=?;";
		User nowUser = User.me.findFirst(sql, account, password);
		if (nowUser == null) {
			renderJson("{\"code\":400}");
			return;
		}
		try {
			result = ApiHttpClient.getToken(Constant.KEY, Constant.SECRET,
					nowUser.getInt("id") + "", nowUser.getStr("username"),
					Constant.SERVER_URL + nowUser.getStr("photo"),
					
					FormatType.json);
			System.out.println(Constant.SERVER_URL + nowUser.getStr("photo"));
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("{\"code\":400}");
			return;
		}
		JSONObject jsonObject = JSONObject.fromObject(result.getResult().toString());
		jsonObject.put("game", nowUser.get("game"));
		renderJson(jsonObject);;
	}
	
	@ClearInterceptor
	public void register() {
		UploadFile file = getFile("imgFile", PathKit.getWebRootPath() + "/temp");
		Map<String, Object> map = saveFile(file);//code 0成功，1失败
		if (Integer.parseInt(map.get("code").toString()) == 200) {
			User user = new User();
			user.put("photo", map.get("url"));
			user.put("account", getPara("account"));
			user.put("password", getPara("password"));
			user.put("username", getPara("username"));
			user.put("sex", getPara("sex"));
			user.put("game", getPara("game"));
			user.put("phone", getPara("phone"));
			user.put("signer", getPara("signer"));
			if (User.me.queryAccount(getPara("account"))) {
				map.put("code", 400);
				map.put("error", "账户已经存在");
				renderJson(map);
				return;
			}
			if (user.save()) {
				map.put("code", 200);
				renderJson(map);
			} else {
				map.put("code", 400);
				map.put("error", "注册失败");
				renderJson(map);
			}
		} else {
			renderJson(map);
		}
	}
	//Whether there is any query account
	public void query() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (User.me.queryAccount(getPara("account"))) {
			map.put("code", 400);
			map.put("error", "账户已经存在");
		} else {
			map.put("code", 200);
			map.put("error", "可以注册");
		}
		renderJson(map);
	}
	
	public void location() {
		User user = new User();
		user.set("latitude", getPara("latitude"));
		user.set("longitude", getPara("longitude"));
		user.set("id", getPara("id"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (user.update()) {
			map.put("code", 200);
			map.put("error", "修改成功");
		} else {
			map.put("code", 400);
			map.put("error", "账户已经存在");
		}
		renderJson(map);
	}

}
