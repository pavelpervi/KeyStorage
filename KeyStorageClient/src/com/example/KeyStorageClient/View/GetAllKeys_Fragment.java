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
public class GetAllKeys_Fragment extends AbstractFragment {
    // Store instance variables
    private String title;
    private int page;
    private EditText etPattern = null;
    private Button btnGet = null;
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
    public static GetAllKeys_Fragment newInstance(int page, String title) {
        GetAllKeys_Fragment fragment = new GetAllKeys_Fragment();
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
        View view = inflater.inflate(R.layout.get_all_keys_fragment, container, false);
        btnGet = (Button) view.findViewById(R.id.btnGet);
        etPattern = (EditText)view.findViewById(R.id.etPattern);
        lvValues = (ListView)view.findViewById(R.id.lvValues);

        if (mAdapter == null) {
            mAdapter = new ListViewAdapter(getActivity());
            lvValues.setAdapter(mAdapter);
        }


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = etPattern.getText().toString();
                if(TextUtils.isEmpty(pattern)) {
                    showAlertMsg(getResources().getString(R.string.alert_dialog_msg));
                    return;
                }

                final HashMap<String, Object> requestValue = new HashMap<String, Object>();
                requestValue.put("pattern", pattern);
                new AsyncMethodTransaction(new Request("getAllKeys", requestValue))
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
                                        if(response.getResult() == null || response.getResult().isEmpty()) {
                                            showAlertMsg(getResources().getString(R.string.alert_dialog_msg_empty_list));
                                        }else {
                                            showAlertMsg(getResources().getString(R.string.alert_dialog_msg_success));
                                            mAdapter.setData(response.getResult());
                                            mAdapter.notifyDataSetChanged();
                                        }
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
