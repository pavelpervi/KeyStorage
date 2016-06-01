package com.example.KeyStorageClient.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.KeyStorageClient.Global.Globals;
import com.example.KeyStorageClient.ListViewAdapter;
import com.example.KeyStorageClient.Network.AsyncMethodTransaction;
import com.example.KeyStorageClient.Network.Protocol.Request;
import com.example.KeyStorageClient.Network.Protocol.Response;
import com.example.KeyStorageClient.Network.Protocol.ResponseCode;
import com.example.KeyStorageClient.R;

import java.util.HashMap;

/**
 * Created by Pasha on 5/30/2016.
 */
public class Set_Fragment extends AbstractFragment {
    // Store instance variables
    private String title;
    private int page;
    private EditText etKey = null;
    private EditText etValue = null;
    private Button btnAdd = null;
    private Button btnDelete = null;
    private Button btnSend = null;

    private ListView lvValues = null;
    private ListViewAdapter mAdapter = null;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // newInstance constructor for creating fragment with arguments
    public static Set_Fragment newInstance(int page, String title) {
        Set_Fragment fragment = new Set_Fragment();
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
        View view = inflater.inflate(R.layout.set_fragment, container, false);
        etKey = (EditText)view.findViewById(R.id.etKey);
        etValue = (EditText)view.findViewById(R.id.etValue);
        btnAdd = (Button)view.findViewById(R.id.btnAdd);
        btnDelete = (Button)view.findViewById(R.id.btnDelete);
        btnSend = (Button)view.findViewById(R.id.btnSend);
        lvValues = (ListView)view.findViewById(R.id.lvValues);

        mAdapter = new ListViewAdapter(getActivity());
        lvValues.setAdapter(mAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etValue.getText().toString();
                if(TextUtils.isEmpty(value)) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }
                if(etKey.isEnabled())
                    etKey.setEnabled(false);
                mAdapter.add(value);
                lvValues.invalidateViews();
                lvValues.refreshDrawableState();
                etValue.setText("");
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etValue.getText().toString();
                if(TextUtils.isEmpty(value)) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }
                if(mAdapter.remove(value)) {
                    lvValues.invalidateViews();
                    lvValues.refreshDrawableState();
                    etValue.setText("");
                    if(mAdapter.getData().isEmpty() && !etKey.isEnabled())
                        etKey.setEnabled(true);
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = etKey.getText().toString();
                if(TextUtils.isEmpty(key) || mAdapter.getData().isEmpty()) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }

                final HashMap<String, Object> requestValue = new HashMap<String, Object>();
                requestValue.put("key", key);
                requestValue.put("value", mAdapter.getData());
                new AsyncMethodTransaction(new Request("set", requestValue))
                        .connect(new AsyncMethodTransaction.TransactionListener() {
                            @Override
                            public void onStartTransaction() {
                                Log.i(Globals.LOG_TAG, "[Get_Fragment] onStartTransaction");
                                showProgressBar();
                            }

                            @Override
                            public void onFinishTransaction(Response response) {
                                Log.i(Globals.LOG_TAG, "[Get_Fragment] onFinishTransaction " + response);
                                dismissProgressbar();
                                if(response != null){
                                    if(response.getStatus() == ResponseCode.OK){
                                        showAlertMsg(getResources().getString(R.string.alert_dialog_msg_success));
                                        mAdapter.clear();
                                        lvValues.invalidateViews();
                                        lvValues.refreshDrawableState();
                                        if(!etKey.isEnabled())
                                            etKey.setEnabled(true);
                                    }else {
                                        showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + response.getErrorMsg());
                                    }
                                }
                            }

                            @Override
                            public void onErrorTransaction(Exception e) {
                                Log.i(Globals.LOG_TAG, "[Get_Fragment] onErrorTransaction ", e);
                                dismissProgressbar();
                                showAlertMsg(getResources().getString(R.string.alert_dialog_msg_error) + (e != null ? e.getMessage() : getResources().getString(R.string.alert_dialog_msg_error_fatal)));
                            }
                        });
            }
        });

        return view;
    }

}
