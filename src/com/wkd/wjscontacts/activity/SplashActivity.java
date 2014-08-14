package com.wkd.wjscontacts.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogUtil.log;
import com.wkd.wjscontacts.R;
import com.wkd.wjscontacts.utils.CommonUtil;
import com.wkd.wjscontacts.utils.PromptUtil;

public class SplashActivity extends BaseActivity {
	protected static final int MSG_UPDATE_PROGRESS = 1;		//更新下载进度
	private final int NOTIFICATION_ID_DOWNLOAD = 0;
	
	private NotificationManager nManager;
	private Notification noti;
	private File apkFile;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case MSG_UPDATE_PROGRESS:
				
				RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.download_noti_layout);
				remoteViews.setProgressBar(R.id.pb_download_1, 100, (Integer)msg.obj, false);
				remoteViews.setProgressBar(R.id.pb_download_2, 100, (Integer)msg.obj, false);
				remoteViews.setProgressBar(R.id.pb_download_3, 100, (Integer)msg.obj, false);
				remoteViews.setProgressBar(R.id.pb_download_4, 100, (Integer)msg.obj, false);
				remoteViews.setProgressBar(R.id.pb_download_5, 100, (Integer)msg.obj, false);
				log.e("xxx", msg.obj.toString());
				noti.contentView = remoteViews;
				nManager.notify(NOTIFICATION_ID_DOWNLOAD, noti);
				
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void setContentLayout() {
		setContentView(R.layout.splashactivity_layout);
	}

	@Override
	protected void beforeInitView() {
		//统计
		AVAnalytics.trackAppOpened(getIntent());
		
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		noti = new Notification();
		noti.icon = R.drawable.ic_launcher;
		noti.tickerText = "微金所通讯录";
		noti.flags = Notification.FLAG_AUTO_CANCEL;
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
			public void done(final AVObject avObject, AVException e) {
				if (e == null) {
					int currVer	= CommonUtil.getAppVer(SplashActivity.this);
					int lastestVer	= avObject.getInt("appVer");
					if (currVer < lastestVer) {	//有新版本
						//下载并安装新版本
						PromptUtil.showDialog(SplashActivity.this,
								"发现新版本", avObject.getString("desc"),
								"更新", "就不", new OnClickListener() {

									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										doDownload(avObject.getString("url"));
										
									}
								}, null);
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
	 */
	protected void doDownload(final String apkUrl) {
		
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			PromptUtil.toast(SplashActivity.this, "没有找到sd卡");
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpUriRequest post = new HttpPost(apkUrl);
				
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					HttpResponse resp = client.execute(post);
					if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
						long apkSize = resp.getEntity().getContentLength();			//文件大小
						is = resp.getEntity().getContent();
						String apkDirPath = Environment.getExternalStorageDirectory() + "/WJSContacts";
						File apkDir = new File(apkDirPath);
						if (!apkDir.exists()) {
							apkDir.mkdirs();
						}
						apkFile = new File(apkDir+"/WJSContacts.apk");
						fos = new FileOutputStream(apkFile);
						
						byte[] buffer = new byte[256 * 1024];
						int length = 0, rate = 0;
						long sum = 0;
						while((length = is.read(buffer)) != -1){
							fos.write(buffer, 0, length);
							sum += length;
							int rateNow = (int) (sum * 100 / apkSize);
							if (rateNow > rate) {
								rate = rateNow;
								Message msg = Message.obtain();
								msg.what = MSG_UPDATE_PROGRESS;
								msg.obj = rate;
								
								if (100 == rateNow) {
									Intent installIntent = getInstallIntent(apkFile);
									startActivity(installIntent);
									noti.contentIntent = PendingIntent.getActivity(SplashActivity.this, 0, installIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
								}
								
								handler.sendMessage(msg);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						is.close();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
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
	
	/**
	 * @return		安装apk的intent
	 */
	private Intent getInstallIntent(File file) {
		Intent installIntent = new Intent();
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setAction(android.content.Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		return installIntent;
	}
	
}
