package com.example.KeyStorageClient.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.example.KeyStorageClient.R;

/**
 * Created by Pasha on 5/31/2016.
 */
public abstract class AbstractFragment extends Fragment {

    protected ProgressDialog mProgressDialog = null;
    protected AlertDialog mAlertDialog = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.alert_dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
    }

    protected void showProgressBar(){
        if(mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    protected void dismissProgressbar(){
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected void showAlertMsg(String msg){
        String title = getResources().getString(R.string.alert_dialog_title_msg);
        if(TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg))
            return;

        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(msg);
        if(mAlertDialog != null && !mAlertDialog.isShowing()){
            this.mAlertDialog.show();
        }
    }

    protected void dismissAlertMsg(){
        if(mAlertDialog != null && mAlertDialog.isShowing())
            this.mAlertDialog.dismiss();
    }
}
