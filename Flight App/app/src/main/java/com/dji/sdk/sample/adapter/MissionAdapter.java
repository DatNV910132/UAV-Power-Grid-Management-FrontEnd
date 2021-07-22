package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.model.Mission;
import com.dji.sdk.sample.ui.activity.mission.MissionDetailActivity;
import com.dji.sdk.sample.ui.activity.mission.PAPMissionDetailActivity;

import java.util.ArrayList;


/**
 * The type Mission adapter.
 */
public class MissionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Mission> list;

    /**
     * Instantiates a new Mission adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param list    the list
     */
    public MissionAdapter(Context context, int layout, ArrayList<Mission> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
         * The Mtv namemision.
         */
        TextView mtv_namemision;
        /**
         * The Mtv desmission.
         */
        TextView mtv_desmission;
        /**
         * The Mbtn detailmission.
         */
        ImageButton mbtn_detailmission;
        /**
         * The Mbtn mapmission.
         */
        ImageButton mbtn_mapmission;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MissionAdapter.ViewHolder holder;
        if (view == null) {
            holder = new MissionAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_namemision = view.findViewById(R.id.tv_lv_namemission);
            holder.mtv_desmission = view.findViewById(R.id.tv_lv_desmission);
            holder.mbtn_detailmission = view.findViewById(R.id.imgbtn_detailmission);
            holder.mbtn_mapmission = view.findViewById(R.id.imgbtn_mapmission);
            view.setTag(holder);

        } else {
            holder = (MissionAdapter.ViewHolder) view.getTag();
        }

        //lấy giá trị của từng Employee để gán vào các itemview tương ứng trong listview rồi trả về view
        Mission mission = list.get(i);
        holder.mtv_namemision.setText(mission.getName());
        holder.mtv_desmission.setText(mission.getDes());
        holder.mbtn_detailmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MissionDetailActivity.class);
                i.putExtra("ID_Mission",mission.get_id());
                view.getContext().startActivity(i);
            }
        });

        holder.mbtn_mapmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setResultToToast("Chuyển qua chế độ map với id= " + mission.get_id());
            }
        });

        return view;

    }
}
