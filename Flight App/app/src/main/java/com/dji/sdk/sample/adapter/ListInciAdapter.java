package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.ElectricPole;
import com.dji.sdk.sample.model.Incident;

import java.util.ArrayList;

/**
 * The type List inci adapter.
 */
public class ListInciAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Incident> listincident;

    /**
     * Instantiates a new List inci adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param listsp  the listsp
     */
    public ListInciAdapter(Context context, int layout, ArrayList<Incident> listsp) {
        this.context = context;
        this.layout = layout;
        this.listincident = listsp;
    }

    @Override
    public int getCount() {
        return listincident.size();
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
         * The Mtv inci name.
         */
        TextView mtv_inci_name;
        /**
         * The Mtv inci des.
         */
        TextView mtv_inci_des;
        /**
         * The Mtv inci date.
         */
        TextView mtv_inci_date;
        /**
         * The Mtv inci status.
         */
        TextView mtv_inci_status;
        /**
         * The Mtv inci level.
         */
        TextView mtv_inci_level;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListInciAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ListInciAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_inci_name = view.findViewById(R.id.iteminci_name);
            holder.mtv_inci_des = view.findViewById(R.id.iteminci_des);
            holder.mtv_inci_date = view.findViewById(R.id.iteminci_date);
            holder.mtv_inci_status = view.findViewById(R.id.iteminci_status);
            holder.mtv_inci_level = view.findViewById(R.id.iteminci_level);
            view.setTag(holder);

        } else {
            holder = (ListInciAdapter.ViewHolder) view.getTag();
        }

        //Lấy một Incident từ list để gán vào các itemview cụ thể trong listviews
        Incident incident = listincident.get(i);
        holder.mtv_inci_name.setText(incident.getName());
        holder.mtv_inci_des.setText(incident.getDes());
        holder.mtv_inci_date.setText(incident.getDate());
        holder.mtv_inci_level.setText(incident.getLevel());
        if(incident.getStatus().equals("norepair")){
            holder.mtv_inci_status.setText("Chưa sửa chữa");
        }
        if(incident.getStatus().equals("repairing")){
            holder.mtv_inci_status.setText("Đang sửa chữa");
        }
        if(incident.getStatus().equals("wait")){
            holder.mtv_inci_status.setText("Chờ xác nhận");
        }
        if(incident.getStatus().equals("repaired")){
            holder.mtv_inci_status.setText("Đã sửa chữa");
        }

        return view;

    }

}