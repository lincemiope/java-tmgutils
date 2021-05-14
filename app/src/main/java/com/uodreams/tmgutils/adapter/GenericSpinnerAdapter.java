package com.uodreams.tmgutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uodreams.tmgutils.R;

public class GenericSpinnerAdapter extends ArrayAdapter<String> {
    private final Context mContext;

    public GenericSpinnerAdapter(@NonNull Context context, final String[] strings) {
        super(context, 0, strings);
        this.mContext = context;
    }

    private static class ViewHolder {
        public TextView tvBody;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String string = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        GenericSpinnerAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new GenericSpinnerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_item_chaaamp, parent, false);
            // Lookup view for data population
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (GenericSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        // alternate colors to better list view
        if (position % 2 != 0) {
            convertView.setBackgroundResource(R.color.pal_IV);
        } else {
            convertView.setBackgroundResource(R.color.pal_IV_alt);
        }
        // Populate the data into the template view using the data object
        viewHolder.tvBody.setText(string);
        // Return the completed view to render on screen
        return convertView;
    }
}
