package com.roboo.like.google.models;

import java.io.Serializable;
import java.util.LinkedList;

public class CommentItem implements Serializable
{
	private static final long serialVersionUID = 7439967154088301878L;
	/**楼层*/
	public String floor ;
	/**昵称*/
	public String nick;
	/**手机类型*/
	public String phoneType;
	/**回复时所在地址*/
	public String address;
	/**回复时间*/
	public String replyTime;
	/**回复内容*/
	public String replyContent;
	/**支持个数*/
	public String agreeCount;
	/**反对个数*/
	public String disAgressCount;
	/**跟帖列表*/
	public LinkedList<CommentItem> followReplyList;
	@Override
	public String toString()
	{
		return "楼层 = "+ floor + " 昵称 = "+ nick + " 手机型号 = "+ phoneType + " 地址 = "+ address + " 回复时间 =" + replyTime
				+" 回复内容 = "+ replyContent + " 支持 ="+ agreeCount + " 反对 = "+ disAgressCount + "跟帖 ="+((null == followReplyList)?0:followReplyList.size());
	}
}
