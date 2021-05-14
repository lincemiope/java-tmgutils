package com.uodreams.tmgutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uodreams.tmgutils.R;
import com.uodreams.tmgutils.model.Request;
import com.uodreams.tmgutils.myRequestsActivity;

import java.util.ArrayList;

public class MyRequestsListAdapter extends ArrayAdapter<Request> {
    private final Context mContext;
    public MyRequestsListAdapter(@NonNull Context context, final ArrayList<Request> requests) {
        super(context, 0, requests);
        this.mContext = context;
    }

    private static class ViewHolder {
        public TextView tvMyRequestSkill;
        public TextView tvMyRequestValue;
        //public TextView tvMyRequestPos; //this.mUserId
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Request request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        MyRequestsListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new MyRequestsListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_myrequests, parent, false);
            // Lookup view for data population
            viewHolder.tvMyRequestSkill = (TextView) convertView.findViewById(R.id.tvMyRequestSkill);
            viewHolder.tvMyRequestValue = (TextView) convertView.findViewById(R.id.tvMyRequestValue);
            //viewHolder.tvMyRequestPos = (TextView) convertView.findViewById(R.userId.tvMyRequestPos);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (MyRequestsListAdapter.ViewHolder) convertView.getTag();
        }

        // alternate colors to better list view
        if (position % 2 != 0) {
            convertView.setBackgroundResource(R.color.pal_IV);
        } else {
            convertView.setBackgroundResource(R.color.pal_IV_alt);
        }
        // Populate the data into the template view using the data object
        final String skillName = request.skill;
        final String skillValue = "" + request.value;
        //final String skillPos = "" + position;
        //viewHolder.tvMyRequestPos.setText(skillPos);
        viewHolder.tvMyRequestValue.setText(skillValue);
        viewHolder.tvMyRequestSkill.setText(skillName);

        final boolean hasIt = ((myRequestsActivity) mContext).isThereMyID(request.userIds);
        if (hasIt) {
            //viewHolder.tvMyRequestPos.setTextColor(mContext.getResources().getColor(R.color.pal_III));
            viewHolder.tvMyRequestValue.setTextColor(mContext.getResources().getColor(R.color.pal_III));
            viewHolder.tvMyRequestSkill.setTextColor(mContext.getResources().getColor(R.color.pal_III));
        } else {
            //viewHolder.tvMyRequestPos.setTextColor(mContext.getResources().getColor(R.color.pal_II));
            viewHolder.tvMyRequestValue.setTextColor(mContext.getResources().getColor(R.color.pal_II));
            viewHolder.tvMyRequestSkill.setTextColor(mContext.getResources().getColor(R.color.pal_II));
        }


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((myRequestsActivity) mContext).updateRequest(request);
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((myRequestsActivity) mContext).shortPressToast(!hasIt);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
