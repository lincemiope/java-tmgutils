package com.uodreams.tmgutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uodreams.tmgutils.R;
import com.uodreams.tmgutils.model.requestUser;
import com.uodreams.tmgutils.utils.Misc;

import java.util.ArrayList;

public class RequestsModelAdapter extends ArrayAdapter<requestUser> {
    private final Context mContext;
    public RequestsModelAdapter(Context context, ArrayList<requestUser> users) {
        super(context, 0, users);
        this.mContext = context;
    }

    private static class ViewHolder {
        public TextView tvPlayerPosition;
        public TextView tvPlayerName;
        public TextView tvPlayerRank;
        public ImageView ivPVMRole;
        public ImageView ivPVPRole;
        public ImageView ivHidderRole;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final requestUser user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_requests, parent, false);
            // Lookup view for data population
            viewHolder.tvPlayerPosition = (TextView) convertView.findViewById(R.id.tvPlayerPosition);
            viewHolder.tvPlayerName = (TextView) convertView.findViewById(R.id.tvPlayerName);
            viewHolder.tvPlayerRank = (TextView) convertView.findViewById(R.id.tvPlayerRank);

            viewHolder.ivPVMRole = (ImageView) convertView.findViewById(R.id.ivPVMRole);
            viewHolder.ivPVPRole = (ImageView) convertView.findViewById(R.id.ivPVPRole);
            viewHolder.ivHidderRole = (ImageView) convertView.findViewById(R.id.ivHidderRole);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate colors to better list view
        if (position % 2 != 0) {
            convertView.setBackgroundResource(R.color.pal_IV);
        } else {
            convertView.setBackgroundResource(R.color.pal_IV_alt);
        }
        // Populate the data into the template view using the data object
        final String userRank = Misc.getRankName(user.rank);
        viewHolder.tvPlayerPosition.setText("" + (position + 1));
        viewHolder.tvPlayerName.setText(user.alias);
        viewHolder.tvPlayerRank.setText(userRank);

        if (user.roles.charAt(0) == '1') {
            viewHolder.ivPVMRole.setImageResource(R.drawable.role_pvm);
        } else {
            viewHolder.ivPVMRole.setImageResource(R.drawable.role_non_pvm);
        }

        if (user.roles.charAt(1) == '1') {
            viewHolder.ivPVPRole.setImageResource(R.drawable.role_pvp);
        } else {
            viewHolder.ivPVPRole.setImageResource(R.drawable.role_non_pvp);
        }

        if (user.roles.charAt(2) == '1') {
            viewHolder.ivHidderRole.setImageResource(R.drawable.role_hidder);
        } else {
            viewHolder.ivHidderRole.setImageResource(R.drawable.role_non_hidder);
        }

        /*final int pos = position;
        final ViewGroup par = parent;

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_requests_disabled, par, false);
                //view.setVisibility(View.INVISIBLE);
                ((RequestsActivity) mContext).removeRequest(pos);
                return true;
            }
        });*/

        //TODO: Implementare la funzione in un secondo momento, ora è buggatissia
        // Return the completed view to render on screen
        return convertView;
    }
}
