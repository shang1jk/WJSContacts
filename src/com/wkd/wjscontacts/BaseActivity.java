package com.wkd.wjscontacts;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public abstract class BaseActivity extends Activity {
	protected MApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (MApplication) getApplicationContext();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentLayout();
		beforeInitView();
		initView();
		afterInitView();
	}

	protected abstract void setContentLayout();
	protected abstract void beforeInitView();
	protected abstract void initView();
	protected abstract void afterInitView();

}
