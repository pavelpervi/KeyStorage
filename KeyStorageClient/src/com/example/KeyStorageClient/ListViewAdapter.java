package com.example.KeyStorageClient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/31/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<String> mData = new ArrayList<>();

    public ListViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public ArrayList<String> getData() {
        return mData;
    }

    public void setData(ArrayList<String> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_view_item, parent, false);
            TextView tvRow = (TextView)convertView.findViewById(R.id.tvRow);
            tvRow.setText(mData.get(position));
        }

        return convertView;
    }

    public boolean remove(String object){
        if(object != null && mData.contains(object)) {
            mData.remove(object);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void add(String object){
        if(object == null) return;
        mData.add(object);
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

}
