package com.wkd.wjscontacts.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CommonUtil {

	/**
	 * @return 版本号 除以 100。
	 */
	public static int getAppVer(Context ctx) {

		try {
			PackageManager pManager = ctx.getPackageManager();
			PackageInfo info = pManager.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			return info.versionCode / 100;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}
}
