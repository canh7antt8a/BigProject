package com.t3h.bigproject.until;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.t3h.bigproject.R;

public class Dailoguntils{
    private static ProgressDialog dialog;
    public static void showDialog(Context context){
        cancelDialog();
        dialog = new ProgressDialog(context);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        dialog.setContentView(R.layout.loading_screen);
    }
    public static void cancelDialog(){
        if (dialog!=null&& dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
