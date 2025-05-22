package com.example.ap4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ap4.R;
import com.example.ap4.Model.PlayerModel;

import java.util.List;

public class PlayerAdapter extends ArrayAdapter<PlayerModel> {
    public PlayerAdapter(Context ctx, List<PlayerModel> items) {
        super(ctx, 0, items);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.joueur_item, parent, false);
        }
        PlayerModel player = getItem(pos);

        ImageView img = convertView.findViewById(R.id.imgAvatar);
        TextView  tv  = convertView.findViewById(R.id.txtPlayerName);

        img.setImageResource(player.getAvatarResId());
        tv.setText(player.getName());

        return convertView;
    }
}
