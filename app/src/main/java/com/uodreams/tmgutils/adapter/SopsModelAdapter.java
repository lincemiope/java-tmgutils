package com.uodreams.tmgutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uodreams.tmgutils.R;
import com.uodreams.tmgutils.SopListActivity;
import com.uodreams.tmgutils.model.Sop;

import java.util.ArrayList;

public class SopsModelAdapter extends ArrayAdapter<Sop> {
    private final Context mContext;
    public SopsModelAdapter(Context context, ArrayList<Sop> sops) {
        super(context, 0, sops);
        this.mContext = context;
    }

    private static class ViewHolder {
        public TextView tvSopValue;
        public TextView tvSopSkill;
        public TextView tvSopDays;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Sop sop = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_soplist, parent, false);
            // Lookup view for data population
            viewHolder.tvSopValue = (TextView) convertView.findViewById(R.id.tvSopValue);
            viewHolder.tvSopSkill = (TextView) convertView.findViewById(R.id.tvSopSkill);
            viewHolder.tvSopDays = (TextView) convertView.findViewById(R.id.tvSopDays);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvSopValue.setText(String.valueOf(sop.value));
        viewHolder.tvSopSkill.setText(String.valueOf(sop.skill));
        viewHolder.tvSopDays.setText(String.valueOf(sop.days).replace(".0",""));

        if (position % 2 != 0) {
            convertView.setBackgroundResource(R.color.pal_IV);
        } else {
            convertView.setBackgroundResource(R.color.pal_IV_alt);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SopListActivity) mContext).queryForRequests(sop);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
