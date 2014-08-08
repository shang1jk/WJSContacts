package com.wkd.wjscontacts.activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.app.DownloadManager;
import android.net.Uri;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.wkd.wjscontacts.R;
import com.wkd.wjscontacts.utils.CommonUtil;
import com.wkd.wjscontacts.utils.PromptUtil;

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
		//检查更新
		AVQuery<AVObject> avQuery = new AVQuery<AVObject>("app_version");
		avQuery.orderByDescending("appVer").getFirstInBackground(new GetCallback<AVObject>() {
			
			@Override
			public void done(AVObject avObject, AVException e) {
				if (e == null) {
					int currVer	= CommonUtil.getAppVer(SplashActivity.this);
					int lastestVer	= avObject.getInt("appVer");
					if (currVer < lastestVer) {	//有新版本
						//下载并安装新版本
						doDownloadAndInstall(avObject.getString("url"));
					}else{
						//获取最新的联系人数据
						getNewContacts();
					} 
					
				}else{
					e.printStackTrace();
					PromptUtil.toast(SplashActivity.this, "版本检查失败！没事儿。");
				}
			}
		});
		
		
		
	
		

	}
	

	
	/**
	 * 下载并安装apk
	 * 调用系统下载管理器下载
	 */
	protected void doDownloadAndInstall(String apkUrl) {
		DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
		request.setTitle("WJSContacts");
		request.setDescription("WJSContacts 下载中...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		
		
		long enqueueId = dManager.enqueue(request);
	}

	/**
	 * 如果联系人数据有变动，则获取并更新本地数据库
	 */
	private void getNewContacts() {
		
	}
	
	/**
	 * 当前用户的手机号是否存在于数据库中
	 */
	private void isUserInDB() {
		// TODO Auto-generated method stub
		
	}
}
