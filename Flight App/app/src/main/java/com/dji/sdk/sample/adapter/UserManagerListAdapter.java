package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.Employee;

import java.util.ArrayList;


/**
 * The type User manager list adapter.
 */
public class UserManagerListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Employee> listemployee;

    /**
     * Instantiates a new User manager list adapter.
     *
     * @param context      the context
     * @param layout       the layout
     * @param listemployee the listemployee
     */
    public UserManagerListAdapter(Context context, int layout, ArrayList<Employee> listemployee) {
        this.context = context;
        this.layout = layout;
        this.listemployee = listemployee;
    }

    @Override
    public int getCount() {
        return listemployee.size();
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
         * The Mtv name.
         */
        TextView mtv_name;
        /**
         * The Mtv phone.
         */
        TextView mtv_phone;
        /**
         * The Mtv role.
         */
        TextView mtv_role;
        /**
         * The Mtv mail.
         */
        TextView mtv_mail;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UserManagerListAdapter.ViewHolder holder;
        if (view == null) {
            holder = new UserManagerListAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_name = view.findViewById(R.id.tv_lv_name);
            holder.mtv_phone = view.findViewById(R.id.tv_lv_phone);
            holder.mtv_role = view.findViewById(R.id.tv_lv_role);
            holder.mtv_mail = view.findViewById(R.id.tv_lv_mail);
            view.setTag(holder);

        } else {
            holder = (UserManagerListAdapter.ViewHolder) view.getTag();
        }

        //lấy giá trị của từng Employee để gán vào các itemview tương ứng trong listview rồi trả về view
        Employee employee = listemployee.get(i);
        holder.mtv_name.setText(employee.getName());
        holder.mtv_phone.setText(employee.getPhone());

        if (employee.getIdrole().equals("5e86f1e756de7131e29ed620")) {
            holder.mtv_role.setText("Phi Công");
        }
        if (employee.getIdrole().equals("5e86f1d656de7131e29ed61f")) {
            holder.mtv_role.setText("Manager");
        }
        if (employee.getIdrole().equals("5e86f20e56de7131e29ed621")) {
            holder.mtv_role.setText("Fixer");
        }

        holder.mtv_mail.setText(employee.getMail());
        return view;

    }

}
