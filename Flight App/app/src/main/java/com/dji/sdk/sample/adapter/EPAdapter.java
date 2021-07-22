package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.ElectricPole;

import java.util.ArrayList;

/**
 * The type Ep adapter.
 */
public class EPAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ElectricPole> listpole;

    /**
     * Instantiates a new Ep adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param listsp  the listsp
     */
    public EPAdapter(Context context, int layout, ArrayList<ElectricPole> listsp) {
        this.context = context;
        this.layout = layout;
        this.listpole = listsp;
    }

    @Override
    public int getCount() {
        return listpole.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * The type View holder.
     */
//Khai báo các itemview trong giao diện của một mục trong list
    public static class ViewHolder {

        /**
         * The Mtv ep name.
         */
        TextView mtv_ep_name;
        /**
         * The Mtv ep latitude.
         */
        TextView mtv_ep_latitude;
        /**
         * The Mtv ep longitude.
         */
        TextView mtv_ep_longitude;
    }

    //Get từng các giá trị đầu vào vào từng item của listview
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EPAdapter.ViewHolder holder;
        if (view == null) {
            holder = new EPAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_ep_name = view.findViewById(R.id.tv_ep_name);
            holder.mtv_ep_latitude = view.findViewById(R.id.tv_ep_latitude);
            holder.mtv_ep_longitude = view.findViewById(R.id.tv_ep_longitude);
            view.setTag(holder);

        } else {
            holder = (EPAdapter.ViewHolder) view.getTag();
        }

        //Gán giá trị cho từng itemview với đầu vào là một list giá trị và trả về listview
        ElectricPole pole = listpole.get(i);
        holder.mtv_ep_name.setText(pole.getPole_Name());
        holder.mtv_ep_latitude.setText(pole.getPole_Latitude().toString());
        holder.mtv_ep_longitude.setText(pole.getPole_Longitude().toString());
        return view;

    }

}