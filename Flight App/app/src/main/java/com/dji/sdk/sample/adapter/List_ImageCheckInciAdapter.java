package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.dji.sdk.sample.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * The type List image check inci adapter.
 */
public class List_ImageCheckInciAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> listsp;

    /**
     * Instantiates a new List image check inci adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param listsp  the listsp
     */
    public List_ImageCheckInciAdapter(Context context, int layout, ArrayList<String> listsp) {
        this.context = context;
        this.layout = layout;
        this.listsp = listsp;
    }

    @Override
    public int getCount() {
        return listsp.size();
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
         * The Image view.
         */
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        List_ImageCheckInciAdapter.ViewHolder holder;
        if (view == null) {
            holder = new List_ImageCheckInciAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.imageView = view.findViewById(R.id.image_checiinci);
            view.setTag(holder);

        } else {
            holder = (List_ImageCheckInciAdapter.ViewHolder) view.getTag();
        }

        //Gán giá trị cho từng itemview với đầu vào là một list giá trị và trả về listview
        //Giá trị ở đây là chuỗi String id của Ảnh và get ảnh ra giao diện bằng picasso
        String sp = listsp.get(i);
        String imageUrl = R.string.URLDataDrone+"photos/byte/" + sp;
        Picasso.get().load(imageUrl).into(holder.imageView);
        return view;

    }
}
