package com.example.KeyStorageClient.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.KeyStorageClient.Global.Globals;
import com.example.KeyStorageClient.Network.AsyncMethodTransaction;
import com.example.KeyStorageClient.Network.Protocol.Request;
import com.example.KeyStorageClient.Network.Protocol.Response;
import com.example.KeyStorageClient.Network.Protocol.ResponseCode;
import com.example.KeyStorageClient.R;

import java.util.HashMap;

/**
 * Created by Pasha on 5/30/2016.
 */
public class Add_Fragment extends AbstractFragment {
    // Store instance variables
    private String title;
    private int page;
    private Button btnAddLeft = null;
    private Button btnAddRight = null;
    private EditText etKey = null;
    private EditText etValue = null;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // newInstance constructor for creating fragment with arguments
    public static Add_Fragment newInstance(int page, String title) {
        Add_Fragment fragment = new Add_Fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        btnAddLeft = (Button)view.findViewById(R.id.btnAddLeft);
        btnAddRight = (Button)view.findViewById(R.id.btnAddRight);
        etKey = (EditText)view.findViewById(R.id.etKey);
        etValue = (EditText)view.findViewById(R.id.etValue);

        btnAddLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = etKey.getText().toString();
                String value = etValue.getText().toString();
                Log.e(Globals.LOG_TAG, "-------------------------btnAddLeft.setOnClickListener");
                if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }

                HashMap<String, Object> requestValue = new HashMap<String, Object>();
                requestValue.put("key", key);
                requestValue.put("value" ,value);
                new AsyncMethodTransaction(new Request("leftAdd", requestValue))
                        .connect(new AsyncMethodTransaction.TransactionListener() {
                            @Override
                            public void onStartTransaction() {
                                Log.e(Globals.LOG_TAG, "-------------------------onStartTransaction");
                                showProgressBar();
                            }

                            @Override
                            public void onFinishTransaction(Response response) {
                                Log.e(Globals.LOG_TAG, "-------------------------onFinishTransaction");
                                dismissProgressbar();
                                if(response != null){
                                    if(response.getStatus() == ResponseCode.OK){
                                        showAlertMsg(getResources().getString(R.string.alert_dialog_msg_success));
                                    }else {
                                        showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + response.getErrorMsg());
                                    }
                                }
                            }

                            @Override
                            public void onErrorTransaction(Exception e) {
                                Log.e(Globals.LOG_TAG, "-------------------------onErrorTransaction");
                                dismissProgressbar();
                                showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + (e != null ? e.getMessage() : getResources().getString(R.string.alert_dialog_msg_error_fatal)));
                            }
                        });
            }
        });

        btnAddRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = etKey.getText().toString();
                String value = etValue.getText().toString();

                if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }

                HashMap<String, Object> requestValue = new HashMap<String, Object>();
                requestValue.put("key", key);
                requestValue.put("value" ,value);
                new AsyncMethodTransaction(new Request("rightAdd", requestValue)).connect(new AsyncMethodTransaction.TransactionListener() {
                    @Override
                    public void onStartTransaction() {
                        showProgressBar();
                    }

                    @Override
                    public void onFinishTransaction(Response response) {
                        dismissProgressbar();
                        if(response != null){
                            if(response.getStatus() == ResponseCode.OK){
                                showAlertMsg(getResources().getString(R.string.alert_dialog_msg_success));
                            }else {
                                showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onErrorTransaction(Exception e) {
                        dismissProgressbar();
                        showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + (e != null ? e.getMessage() : getResources().getString(R.string.alert_dialog_msg_error_fatal)));
                    }
                });
            }
        });

        return view;
    }


}
