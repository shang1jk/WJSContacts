package com.wkd.wjscontacts.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class PromptUtil {
	
	/** 居中土司 */
	public static void toast(Context ctx, String msg){
		Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
