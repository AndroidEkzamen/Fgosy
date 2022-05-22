package com.elena.fgosy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//адаптер, чтобы в listview на fragmentlist было 3 колонки с именем, номером и информацией объекта
public class MyObjectAdapter extends ArrayAdapter<MyObject> {

    private LayoutInflater inflater;
    private List<MyObject> myObjects;
    private int layout;
    private AdapterView.OnItemClickListener mListener;

    public MyObjectAdapter(Context context, int resource,List<MyObject> myObjects) {
        super(context, resource,myObjects);
        this.myObjects = myObjects;
        this.layout = resource;
        inflater = LayoutInflater.from(context);
    }
    // пункт списка
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(this.layout, viewGroup, false);
        }

        TextView nameView = (TextView) view.findViewById(R.id.tvName);
        TextView numberView = (TextView) view.findViewById(R.id.tvNumber);
        TextView info = (TextView) view.findViewById(R.id.tvInfo);

        MyObject obj = myObjects.get(i);

        nameView.setText(obj.getObjectName());
        numberView.setText(String.valueOf(obj.getObjectNumber()));
        info.setText(obj.getObjectInfo());

        return view;
    }
}
