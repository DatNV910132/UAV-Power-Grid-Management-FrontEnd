package com.dji.sdk.sample.ui.fragment.dataflight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.adapter.SpinDroneAdapter;
import com.dji.sdk.sample.adapter.SpinEPAdapter;
import com.dji.sdk.sample.model.Drone;
import com.dji.sdk.sample.model.ElectricPole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dji.thirdparty.sanselan.common.ImageMetadata;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhotoFragment extends Fragment {

    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private static final int GALLERY_REQUEST_CODE = 1234;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private JsonPlaceHolderApi jsonPlaceHolderApi_photo;
    private List<ElectricPole> list_ep = new ArrayList<>();
    private List<Drone> list_drone = new ArrayList<>();
    private boolean crop = false;
    private MultipartBody.Part fileToUpload;

    private Button mbtn_addphoto_choosefile;
    private Button mbtn_addphoto_save;
    private Button mbtn_addphoto_cancel;
    private EditText medt_addphoto_title;
    private EditText medt_addphoto_datecreaete;
    private EditText medt_addphoto_dateimport;
    private EditText medt_addphoto_des;
    private Spinner msp_addphoto_idpole;
    private Spinner msp_addphoto_iddrone;
    private Spinner msp_addphoto_type;
    private RadioGroup mrg_addphoto;
    private RadioButton mrb_addphoto_true;
    private RadioButton mrb_addphoto_false;
    private ImageView mimv_addphoto_image;
    private TextView mtv_addphoto_status;

    /**
     * Instantiates a new Add photo fragment.
     */
    public AddPhotoFragment() {
        // Required empty public constructor
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    File file = new File(getPathFromUri(getContext(), selectedImage));

                    try {
                        ExifInterface exif = new ExifInterface(getPathFromUri(getContext(), selectedImage));

                        System.out.println(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
                        System.out.println(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
                        System.out.println(exif.getAttribute(ExifInterface.TAG_DATETIME));
                        System.out.println(exif.getAttribute(ExifInterface.TAG_MODEL));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                    mimv_addphoto_image.setImageURI(selectedImage);
                    break;

            }
    }

    /**
     * Gets path from uri.
     *
     * @param context the context
     * @param uri     the uri
     * @return the path from uri
     */
    @SuppressLint("NewApi")
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Gets data column.
     *
     * @param context       the context
     * @param uri           the uri
     * @param selection     the selection
     * @param selectionArgs the selection args
     * @return the data column
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * Is external storage document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * Is downloads document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Is media document boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Is google photos uri boolean.
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_photo, container, false);

        //Get dữ liệu thông tin người dùng đăng nhập vào hệ thống từ đầu
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, getContext().MODE_PRIVATE);
        String id_user = sharedPreferences.getString(ID_USER, "");

        //Link các trường giao diện khai báo và layout giao diện đã thiết kế
        medt_addphoto_title = root.findViewById(R.id.edt_addphoto_title);
        msp_addphoto_type = root.findViewById(R.id.sp_addphoto_type);
        medt_addphoto_datecreaete = root.findViewById(R.id.edt_addphoto_datecreate);
        medt_addphoto_dateimport = root.findViewById(R.id.edt_addphoto_dateimport);
        medt_addphoto_des = root.findViewById(R.id.edt_addphoto_des);
        msp_addphoto_idpole = root.findViewById(R.id.sp_addphoto_idpole);
        msp_addphoto_iddrone = root.findViewById(R.id.sp_addphoto_iddrone);
        mrg_addphoto = root.findViewById(R.id.rg_addphoto);
        mrb_addphoto_true = root.findViewById(R.id.rb_addphoto_true);
        mrb_addphoto_false = root.findViewById(R.id.rb_addphoto_false);
        mbtn_addphoto_choosefile = root.findViewById(R.id.btn_addphoto_choosefile);
        mbtn_addphoto_save = root.findViewById(R.id.btn_addphoto_save);
        mbtn_addphoto_cancel = root.findViewById(R.id.btn_addphoto_cancel);
        mimv_addphoto_image = root.findViewById(R.id.imv_addphoto_image);
        mtv_addphoto_status = root.findViewById(R.id.tv_addphoto_status);

        //Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataService))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Retrofit retrofit_photo = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataDrone))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi_photo = retrofit_photo.create(JsonPlaceHolderApi.class);

        String type[] = {
                "Trụ sứ",
                "Cột điện",
                "Đường dây"};
        ArrayAdapter<String> adaptertype = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, type);
        adaptertype.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner type
        msp_addphoto_type.setAdapter(adaptertype);

        //lấy thông tin của tất cả các drone trong hệ thống và dán vào spinner giao diện
        Call<List<Drone>> getalldrone = jsonPlaceHolderApi.getallDrone();
        getalldrone.enqueue(new Callback<List<Drone>>() {
            @Override
            public void onResponse(Call<List<Drone>> call, Response<List<Drone>> response) {
                list_drone = response.body();
                SpinDroneAdapter spinDroneAdapter = new SpinDroneAdapter(getContext(), android.R.layout.simple_spinner_item, (ArrayList<Drone>) list_drone);
                spinDroneAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                msp_addphoto_iddrone.setAdapter(spinDroneAdapter);
            }

            @Override
            public void onFailure(Call<List<Drone>> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

        //Lấy tất cả các thông tin về cột điện và hiện thị lên giao diện spinner lựa chọn
        Call<List<ElectricPole>> getallep = jsonPlaceHolderApi.getAllEP();
        getallep.enqueue(new Callback<List<ElectricPole>>() {
            @Override
            public void onResponse(Call<List<ElectricPole>> call, Response<List<ElectricPole>> response) {
                list_ep = response.body();
                SpinEPAdapter spinEPAdapter = new SpinEPAdapter(getContext(), android.R.layout.simple_spinner_item, (ArrayList<ElectricPole>) list_ep);
                spinEPAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                msp_addphoto_idpole.setAdapter(spinEPAdapter);

            }

            @Override
            public void onFailure(Call<List<ElectricPole>> call, Throwable t) {

            }
        });

        //Lựa chọn radio group loại ảnh
        mrg_addphoto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkradio = radioGroup.getCheckedRadioButtonId();
                if (checkradio == R.id.rb_addphoto_true) {
                    crop = false;
                } else {
                    crop = true;
                }
            }
        });

        //Lựa chọn radio button loại ảnh
        mrb_addphoto_true.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                crop = false;
            }
        });

        //Lựa chọn radio button loại ảnh
        mrb_addphoto_false.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                crop = true;
            }
        });

        //Lựa chọn file ảnh từ thiết bị để upload lên hệ thống
        mbtn_addphoto_choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the ACTION_GET_CONTENT Intent
                pickFromGallery();
            }
        });

        //Sự kiện lựa chọn button "cancel"
        mbtn_addphoto_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new DataFlightManaFragment());
                ft_rep.commit();
            }
        });

        //Sự kiện lựa chọn button "Lưu" dữ liệu ảnh vào hệ thống
        //Lấy ra cá trường mà người dùng đã nhập và set vào model photo
        mbtn_addphoto_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_addphoto_title.getText().toString().equals("") ||
                        medt_addphoto_datecreaete.getText().toString().equals("") ||
                        medt_addphoto_dateimport.getText().toString().equals("") ||
                        medt_addphoto_des.getText().toString().equals("")) {
                    mtv_addphoto_status.setText("Bạn cần nhập đầy đủ thông tin Ảnh");
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lưu thông tin")
                            .setMessage("Bạn có muốn lưu thông tin không ?")

                            //Nếu lựa chọn "Yes"
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String title = medt_addphoto_title.getText().toString();
                                    String datecreate = medt_addphoto_datecreaete.getText().toString();
                                    String dateimport = medt_addphoto_dateimport.getText().toString();
                                    String des = medt_addphoto_des.getText().toString();
                                    String idpole = list_ep.get(msp_addphoto_idpole.getSelectedItemPosition()).get_id();
                                    String iddrone = list_drone.get(msp_addphoto_iddrone.getSelectedItemPosition()).get_id();
                                    String type = "";
                                    if (msp_addphoto_type.getSelectedItemPosition() == 0) {
                                        type = "insulator";
                                    }
                                    if (msp_addphoto_type.getSelectedItemPosition() == 1) {
                                        type = "pole";
                                    }
                                    if (msp_addphoto_type.getSelectedItemPosition() == 2) {
                                        type = "wire";
                                    }

                                    Call<String> addPhoto = jsonPlaceHolderApi_photo.addphoto(title, type, datecreate, dateimport, des, idpole, id_user, iddrone, crop, fileToUpload);
                                    addPhoto.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
//                                            System.out.println("Upload thành công: " + response.body());
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
//                                            System.out.println("Không upload được: " + t.getMessage());
                                        }
                                    });
                                    Toast.makeText(getContext(), "Upload ảnh thành công", Toast.LENGTH_SHORT).show();

                                    //Chuyển fragment sau khi upload dữ liệu lên thành công
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft_rep = fm.beginTransaction();
                                    ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                    ft_rep.replace(R.id.nav_host_fragment, new DataFlightManaFragment());
                                    ft_rep.commit();

                                }
                            })

                            //Nếu lựa chọn "No"
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //Chuyển sang fragment quản lý dữ liệu bay
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft_rep = fm.beginTransaction();
                                    ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                    ft_rep.replace(R.id.nav_host_fragment, new DataFlightManaFragment());
                                    ft_rep.commit();
                                }
                            })
                            .show();
                }
            }
        });


        return root;
    }
}
