package com.dji.sdk.sample.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.ui.activity.dji.LiveStreamActivity;
import com.dji.sdk.sample.ui.activity.dji.MediaViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Dji list features adapter.
 */
public class DJIListFeaturesAdapter extends RecyclerView.Adapter<DJIListFeaturesAdapter.RecyclerViewHolder>{

    private List<String> data = new ArrayList<>();

    /**
     * Instantiates a new Dji list features adapter.
     *
     * @param data the data
     */
    public DJIListFeaturesAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_item_listfeatures, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.mbtn_features.setText(data.get(position));

        holder.mbtn_features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( position == 0 ){
                    Intent i = new Intent(v.getContext(), LiveStreamActivity.class);
                    v.getContext().startActivity(i);
                }
                if( position == 3 ){
                    Intent i = new Intent(v.getContext(), MediaViewActivity.class);
                    v.getContext().startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * The type Recycler view holder.
     */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Mbtn features.
         */
        Button mbtn_features;

        /**
         * Instantiates a new Recycler view holder.
         *
         * @param itemView the item view
         */
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mbtn_features = itemView.findViewById(R.id.btn_rv_lfitem);
        }
    }
}

