package com.example.lenovo.supercourse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JSAdapter extends ArrayAdapter<JS> {
    private Context context;
    private int resource;
    private List<JS> allJS;
    public JSAdapter(@NonNull Context context, int resource, List<JS> allJS) {
        super(context, resource,allJS);
        this.context=context;
        this.resource=resource;
        this.allJS=allJS;
        Log.i("zm1",allJS.size()+"");
    }

    @Override
    public int getCount() {
        return allJS.size();
    }

    @Nullable
    @Override
    public JS getItem(int position) {
        return allJS.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource,parent,false);
        }
        TextView nameTv=convertView.findViewById(R.id.JSnameTv);
        TextView valueTv=convertView.findViewById(R.id.JSvalueTv);
        nameTv.setText(getItem(position).getJSname());
        valueTv.setText(getItem(position).getValue());

        return convertView;
    }

}
