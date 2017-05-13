package com.sendmethen.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sendmethen.R;
import com.sendmethen.models.PublicMessages;

import java.util.ArrayList;

/**
 * Created by adam on 12/7/16.
 */

public class PublicMessagesAdapter extends ArrayAdapter<PublicMessages> {
    Context context;
    int layoutResourceId;
    ArrayList<PublicMessages> data = null;

    public PublicMessagesAdapter(Context context, int layoutResourceId, ArrayList<PublicMessages> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        TextView msgTitle=(TextView) row.findViewById(R.id.msgTitle);
        TextView msgShortText=(TextView) row.findViewById(R.id.msgShortText);

        PublicMessages message=data.get(position);
        msgTitle.setText(message.subject);
        msgShortText.setText(message.short_text);
//        TextView menuName= (TextView)row.findViewById(R.id.menuName);
//        TextView menuPrice= (TextView)row.findViewById(R.id.menuPrice);
//
//        MenuItem menuItem = data.get(position);
//
//        menuName.setText(menuItem.name);
//        menuPrice.setText(menuItem.price);

        return row;

    }
}