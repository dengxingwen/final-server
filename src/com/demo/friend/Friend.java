package com.demo.friend;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class Friend extends Model<Friend> {
	
	public static final Friend me = new Friend();
	
	
	/**
	 * @param fromId 添加人ID
	 * @param toId 被添加人ID
	 * @return true查询到结果，false没查询到结果
	 */
	public boolean isHave(String fromId, String toId) {
		String sql = "SELECT * FROM friend WHERE addId=? and addedId=?;";
		List<Friend> list = Friend.me.find(sql, fromId, toId);
		list.addAll(Friend.me.find(sql, toId, fromId));
		if (list.size() >= 1)
			return true;
		return false;
	}

}
