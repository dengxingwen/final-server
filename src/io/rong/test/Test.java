package io.rong.test;

import java.util.ArrayList;
import java.util.List;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;
import io.rong.models.TxtMessage;

import com.demo.base.Constant;

public class Test {
	
	public static void main(String[] args) {
		SdkHttpResult skdHttpResult = null;
		List<String> list = new ArrayList<String>();
		list.add("1");
		try {
			skdHttpResult = ApiHttpClient.publishSystemMessage(Constant.KEY, Constant.SECRET, "2",
					list, new TxtMessage("addRequest"), "添加好友",
					"pushData", FormatType.json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(skdHttpResult.getResult());
	}

}
