package com.escornabot.btremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class CommandListAdapter extends ArrayAdapter<Command> {

    public CommandListAdapter(Context context, List<Command> commands) {
        super(context, 0, commands);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Command command = getItem(position);
        DraggableActionLayout view = (DraggableActionLayout)
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, null);

        ImageView actionImage = (ImageView) view.findViewById(R.id.action_image);

        actionImage.setImageResource(command.getImageResourceId());
        view.setTag(command);

        return view;
    }
}
