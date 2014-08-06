package com.wkd.wjscontacts;

import com.avos.avoscloud.AVAnalytics;

public class SplashActivity extends BaseActivity {

	@Override
	protected void setContentLayout() {
		setContentView(R.layout.splashactivity_layout);
	}

	@Override
	protected void beforeInitView() {
		//统计
		AVAnalytics.trackAppOpened(getIntent());
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void afterInitView() {
		
	}
}
