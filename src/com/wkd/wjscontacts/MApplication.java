package com.wkd.wjscontacts;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

public class MApplication extends Application {

	@Override
	public void onCreate() {
		AVOSCloud.initialize(this,
				"oye7noz4b9v3kdkp98gg4ldnvku29lermhtq5n6vwth7dvj2",
				"cm7vqq21ij7bzbzntrxksog8ilacx9izqu4vrnh80ftdqjg2");
		
		 AVAnalytics.enableCrashReport(this, true);
		
		super.onCreate();
	}

}
