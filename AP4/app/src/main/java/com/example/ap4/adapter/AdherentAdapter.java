package com.example.ap4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ap4.Model.AdherentModel;

import java.util.List;

public class AdherentAdapter extends ArrayAdapter<AdherentModel> {
    public AdherentAdapter(Context ctx, List<AdherentModel> data) {
        super(ctx, android.R.layout.simple_list_item_2, android.R.id.text1, data);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = super.getView(pos, convertView, parent);
        TextView t1 = v.findViewById(android.R.id.text1);
        TextView t2 = v.findViewById(android.R.id.text2);
        AdherentModel a = getItem(pos);
        t1.setText(a.getPrenom() + " " + a.getNom());
        t2.setText(a.getMail());
        return v;
    }
}
