package com.grayapps.saccodemo.Demo.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import com.grayapps.saccodemo.R;

public class ShowDialog {
    private Activity activity;
    private AlertDialog dialog;
    public ShowDialog(Activity myActivity){
        activity = myActivity;
    }
    public void loginDialog(){
        if(activity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.login_as_layout, null));
            builder.setCancelable(true);

            dialog = builder.create();
            dialog.show();
        }
    }
    public void viewNextActivity(final Context context, final String title, String message){

    }
   public void dismissDialog(){
        dialog.dismiss();
    }
}
