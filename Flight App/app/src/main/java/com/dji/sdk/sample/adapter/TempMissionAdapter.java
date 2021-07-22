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
import com.dji.sdk.sample.model.TemplatePoleMission;
import com.dji.sdk.sample.ui.activity.mission.MissionDetailActivity;
import com.dji.sdk.sample.ui.activity.mission.TemplatePoleMissionDetailActivity;

import java.util.ArrayList;

/**
 * The type Temp mission adapter.
 */
public class TempMissionAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<TemplatePoleMission> list;

    /**
     * Instantiates a new Mission adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param list    the list
     */
    public TempMissionAdapter(Context context, int layout, ArrayList<TemplatePoleMission> list) {
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
        TextView mtv_nametempmision;
        /**
         * The Mtv desmission.
         */
        TextView mtv_destempmission;
        /**
         * The Mtv typetempmission.
         */
        TextView mtv_typetempmission;
        /**
         * The Mbtn detailmission.
         */
        ImageButton mbtn_detailtempmission;
        /**
         * The Mbtn mapmission.
         */
        ImageButton mbtn_checktempmission;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TempMissionAdapter.ViewHolder holder;
        if (view == null) {
            holder = new TempMissionAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_nametempmision = view.findViewById(R.id.tv_lv_nametempmission);
            holder.mtv_destempmission = view.findViewById(R.id.tv_lv_destempmission);
            holder.mtv_typetempmission = view.findViewById(R.id.tv_lv_typetempmission);
            holder.mbtn_detailtempmission = view.findViewById(R.id.imgbtn_detailtempmission);
            holder.mbtn_checktempmission = view.findViewById(R.id.imgbtn_checktempmission);
            view.setTag(holder);

        } else {
            holder = (TempMissionAdapter.ViewHolder) view.getTag();
        }

        //lấy giá trị của từng Employee để gán vào các itemview tương ứng trong listview rồi trả về view
        TemplatePoleMission templatepolemission = list.get(i);
        holder.mtv_nametempmision.setText(templatepolemission.getName());
        holder.mtv_destempmission.setText(templatepolemission.getDes());
        holder.mtv_typetempmission.setText(templatepolemission.getPoletype());
        holder.mbtn_detailtempmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), TemplatePoleMissionDetailActivity.class);
                i.putExtra("ID_Mission",templatepolemission.get_id());
                view.getContext().startActivity(i);
            }
        });

        holder.mbtn_checktempmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setResultToToast("Chuyển qua chế độ check với id= " + templatepolemission.get_id());
            }
        });

        return view;

    }

}
