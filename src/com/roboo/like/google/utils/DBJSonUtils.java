package com.roboo.like.google.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.droidux.trial.da;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author bo.li 获取类路径下存放数据库脚本文件信息，用于数据库表的创建 2014-7-11 下午4:40:20
 */
public class DBJSonUtils
{
	private static final String DB_JSON_NAME = "db.json";
	private static final String JSON_KEY_TABLE = "table";

	/***
	 * 获取数据库脚本文件字符串信息
	 * 
	 * @param context
	 *            :上下文对象
	 * @return "" 或者 null
	 */
	public static String getSQL(Context context)
	{
		String json = null;
		InputStream inputstream = context.getClassLoader().getResourceAsStream(DB_JSON_NAME);
		if (null != inputstream)
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream, Charset.forName(HTTP.UTF_8)));
			StringBuffer stringBuffer = new StringBuffer();
			try
			{
				String str = null;
				while ((str = bufferedReader.readLine()) != null)
				{
					stringBuffer.append(str);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			json = stringBuffer.toString();
		}
		return json;
	}

	/**
	 * 获取的数据库脚本文件处理
	 * 
	 * @param context
	 *            :上下文对象
	 * @return null或者 LinkedList<String> 对象[有数据]
	 */
	public static LinkedList<String> getSQList(Context context)
	{
		String json = getSQL(context);
		LinkedList<String> data = null;
		if (!TextUtils.isEmpty(json))
		{
			try
			{
				JSONArray jsonArray = new JSONArray(json);
				data = new LinkedList<String>();
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsonObject = jsonArray.optJSONObject(i);
					if (null != jsonObject)
					{
						String sql = jsonObject.optString(JSON_KEY_TABLE);
						if (!TextUtils.isEmpty(sql))
						{
							data.add(sql);
						}
					}
				}
				if (data.size() == 0)//确保返回结果与说明一致
				{
					data = null;
				}
			}
			catch (Exception e)
			{

			}
		}
		return data;
	}
}
