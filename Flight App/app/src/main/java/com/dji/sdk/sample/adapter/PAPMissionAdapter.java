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
import com.dji.sdk.sample.model.PoleAndPointMission;
import com.dji.sdk.sample.ui.activity.mission.MissionDetailActivity;
import com.dji.sdk.sample.ui.activity.mission.PAPMissionDetailActivity;

import java.util.ArrayList;

/**
 * The type Pap mission adapter.
 */
public class PAPMissionAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<PoleAndPointMission> list;

    /**
     * Instantiates a new Mission adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param list    the list
     */
    public PAPMissionAdapter(Context context, int layout, ArrayList<PoleAndPointMission> list) {
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
        TextView mtv_namepapmission;
        /**
         * The Mtv desmission.
         */
        TextView mtv_despapmission;
        /**
         * The Mtv typetempmission.
         */
        TextView mtv_polepapmission;
        /**
         * The Mbtn detailmission.
         */
        ImageButton mbtn_detailpapmission;
        /**
         * The Mbtn mapmission.
         */
        ImageButton mbtn_checkpapmission;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PAPMissionAdapter.ViewHolder holder;
        if (view == null) {
            holder = new PAPMissionAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_namepapmission = view.findViewById(R.id.tv_lv_namepapmission);
            holder.mtv_despapmission = view.findViewById(R.id.tv_lv_despapmission);
            holder.mtv_polepapmission = view.findViewById(R.id.tv_lv_polepapmission);
            holder.mbtn_detailpapmission = view.findViewById(R.id.imgbtn_detailpapmission);
            holder.mbtn_checkpapmission = view.findViewById(R.id.imgbtn_checkpapmission);
            view.setTag(holder);

        } else {
            holder = (PAPMissionAdapter.ViewHolder) view.getTag();
        }

        //lấy giá trị của từng Employee để gán vào các itemview tương ứng trong listview rồi trả về view
        PoleAndPointMission poleAndPointMission = list.get(i);
        holder.mtv_namepapmission.setText(poleAndPointMission.getName());
        holder.mtv_despapmission.setText(poleAndPointMission.getDes());

        if(poleAndPointMission.getIdpole().equals("no")){
            holder.mtv_polepapmission.setText("No");
        } else {
            holder.mtv_polepapmission.setText("Yes");
        }


        holder.mbtn_detailpapmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PAPMissionDetailActivity.class);
                i.putExtra("ID_Mission",poleAndPointMission.get_id());
                view.getContext().startActivity(i);
            }
        });

        holder.mbtn_checkpapmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setResultToToast("Chuyển qua chế độ check với id= " + poleAndPointMission.get_id());
            }
        });

        return view;

    }
}
