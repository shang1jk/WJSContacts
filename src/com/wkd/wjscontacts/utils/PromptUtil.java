package com.wkd.wjscontacts.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;

public class PromptUtil {
	
	/**	  居中土司  */
	public static void toast(Context ctx, String msg){
		Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**  提示对话框  */
	public static void showDialog(Context ctx, String title, String msg, String ok, String cancel, OnClickListener okListener, OnClickListener cancelListener){
		
		Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
					.setMessage(msg)
					.setPositiveButton(ok, okListener)
					.setNegativeButton(cancel, cancelListener)
					.setCancelable(false)
					.show();
	}
}
