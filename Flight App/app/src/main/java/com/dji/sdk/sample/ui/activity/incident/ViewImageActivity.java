package com.dji.sdk.sample.ui.activity.incident;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

/**
 * The type View image activity.
 */
public class ViewImageActivity extends AppCompatActivity {

    private PhotoView photoView;
    private Button mbtn_addinci;
    private Button mbtn_gohome;
    private  String idimage,idpole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        photoView = findViewById(R.id.view_image_checkinci);
        mbtn_addinci = findViewById(R.id.btn_viewimage_addinci);
        mbtn_gohome = findViewById(R.id.btn_viewimage_gohome);

        //Get Intent từ activity để có thể lấy giá trị truyền vào từ activity trước
        //lấy id của ảnh và id của cột điện
        Intent get = getIntent();
        idimage = get.getStringExtra("idimage");
        idpole = get.getStringExtra("idpole");
        //Lấy ảnh và hiển thị ra giao diện
        String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + idimage;
        Picasso.get().load(imageUrl).into(photoView);

        //Sự kiện trở về trang chủ khi chọn button "Go Home"
        mbtn_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Sự kiện chọn button "Thêm sự cố"
        mbtn_addinci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Khởi tạo, truyền dữ liệu và start để chuyển sang activity thêm sự cố
                Intent intent = new Intent(getApplicationContext(),AddIncidentActivity.class);
                intent.putExtra("idimage",idimage);
                intent.putExtra("idpole",idpole);
                startActivity(intent);
            }
        });

    }
}
