package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.ElectricPole;
import com.dji.sdk.sample.model.Incident;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type List confirm inci adapter.
 */
public class ListConfirmInciAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Incident> listincident;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    /**
     * Instantiates a new List confirm inci adapter.
     *
     * @param context the context
     * @param layout  the layout
     * @param listsp  the listsp
     */
    public ListConfirmInciAdapter(Context context, int layout, ArrayList<Incident> listsp) {
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
         * The Mtv confinci name.
         */
        TextView mtv_confinci_name;
        /**
         * The Mtv confinci pole.
         */
        TextView mtv_confinci_pole;
        /**
         * The Mptv confinci image.
         */
        PhotoView mptv_confinci_image;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListConfirmInciAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ListConfirmInciAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            //khai báo và link các itemview với các itemview trong giao diện
            holder.mtv_confinci_name = view.findViewById(R.id.tv_listitem_confirminci_name);
            holder.mtv_confinci_pole = view.findViewById(R.id.tv_listitem_confirminci_pole);
            holder.mptv_confinci_image = view.findViewById(R.id.ptv_listitem_confirminci_image);
            view.setTag(holder);

        } else {
            holder = (ListConfirmInciAdapter.ViewHolder) view.getTag();
        }

        //Khai báo retrofit để get API để lấy các giá trị cụ thể đưa vào listview
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        //Link API của DataService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.valueOf(R.string.URLDataService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //lấy giá trị từ list đầu vào
        Incident incident = listincident.get(i);
        //gán giá trị cụ thể vào itemview
        holder.mtv_confinci_name.setText(incident.getName());

        //Call API lấy thông tin cột điện theo ID đầu vào
        Call<ElectricPole> call1 = jsonPlaceHolderApi.getEPbyID(incident.getIdpole());
        call1.enqueue(new Callback<ElectricPole>() {
            @Override
            public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                ElectricPole electricPole = response.body();
                holder.mtv_confinci_pole.setText(electricPole.getPole_Name());
            }

            @Override
            public void onFailure(Call<ElectricPole> call, Throwable t) {
                holder.mtv_confinci_pole.setText("Lỗi lấy tên cột");
            }
        });

        //Lấy ảnh từ dữ liệu sự cố bằng các Get API từ DataDrone Service với picasso
        String imageUrl = R.string.URLDataDrone+"photos/byte/" + incident.getImage();

        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(holder.mptv_confinci_image);

        return view;

    }

}