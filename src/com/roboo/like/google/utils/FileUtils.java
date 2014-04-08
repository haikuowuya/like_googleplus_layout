package com.roboo.like.google.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils
{
	public static final String TYPE_IMG = "images";
	public static final String TYPE_APK = "apks";
	public static final String TYPE_DATA = "datas";

	public static File getFileCacheDir(Context context, String fileType)
	{
		String path = null;
		if (hasSDCard())
		{
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "android" + File.separator + "data" + File.separator + context.getPackageName();
		}
		else
		{
			path = Environment.getDataDirectory().getAbsolutePath() + File.separator + "data" + File.separator + context.getPackageName();
		}
		if (!TextUtils.isEmpty(fileType))
		{
			path = path + File.separator + fileType;
		}
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdirs();
		}
		return file;
	}

	/** 修改文件权限(没有SD卡) */
	public  static void chmod(String path)
	{
		String[] command = { "chmod", "777", path };
		ProcessBuilder builder = new ProcessBuilder(command);
		try
		{
			builder.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static boolean hasSDCard()
	{
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
}
