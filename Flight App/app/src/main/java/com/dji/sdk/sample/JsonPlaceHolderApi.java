package com.dji.sdk.sample;

import com.dji.sdk.sample.model.Drone;
import com.dji.sdk.sample.model.ElectricPole;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Incident;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.model.Mission;
import com.dji.sdk.sample.model.PoleAndPointMission;
import com.dji.sdk.sample.model.TemplatePoleMission;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The interface Json place holder api.
 */
public interface JsonPlaceHolderApi {

    /**
     * Gets all emp.
     *
     * @param code the code
     * @return the all emp
     */
//Lấy thông tin của tất cả người dùng
    @GET("employees/getall/")
    Call<List<Employee>> getAllEmp(@Query("code") String code);

    /**
     * Gets empby id.
     *
     * @param id   the id
     * @param code the code
     * @return the empby id
     */
//Lấy thông tin người dùng theo id
    @GET("employees/{id}")
    Call<Employee> getEmpbyID(@Path("id") String id, @Query("code") String code);

    /**
     * Gets empby username.
     *
     * @param username the username
     * @return the empby username
     */
//Lấy thông tin người dùng bằng username
    @GET("employees/username/")
    Call<Employee> getEmpbyUsername(@Query("username") String username);

    /**
     * Gets empby phone.
     *
     * @param phone the phone
     * @param code  the code
     * @return the empby phone
     */
//Lấy thông tin người dùng bằng số điện thoại
    @GET("employees/phone/")
    Call<Employee> getEmpbyPhone(@Query("phone") String phone, @Query("code") String code);

    /**
     * Send mail call.
     *
     * @param phone the phone
     * @return the call
     */
//Gửi mail cho người dùng để lấy lại mật khẩu
    @GET("employees/resetpwd")
    Call<String> sendMail(@Query("phone") String phone);

    /**
     * Update user call.
     *
     * @param id       the id
     * @param employee the employee
     * @param code     the code
     * @return the call
     */
//Chỉnh sửa thông tin người dùng
    @PUT("employees/update/{id}")
    Call<String> updateUser(@Path("id") String id, @Body Employee employee, @Query("code") String code);

    /**
     * Delete user call.
     *
     * @param id   the id
     * @param code the code
     * @return the call
     */
//Xóa thông tin người dùng
    @DELETE("employees/delete/{id}")
    Call<String> deleteUser(@Path("id") String id, @Query("code") String code);

    /**
     * Add user call.
     *
     * @param employee the employee
     * @param code     the code
     * @return the call
     */
//Thêm thông tin người dùng
    @POST("employees/add")
    Call<String> addUser(@Body Employee employee, @Query("code") String code);

    /**
     * Gets empby role.
     *
     * @param role the role
     * @param code the code
     * @return the empby role
     */
//Lấy danh sách người dùng theo nhiệm vụ (quyền của người dùng đó)
    @GET("employees/role/")
    Call<List<Employee>> getEmpbyRole(@Query("role") String role, @Query("code") String code);

    /**
     * Gets all ep.
     *
     * @return the all ep
     */
//Lấy tất cả thông tin của các cột điện
    @GET("electricpoles/")
    Call<List<ElectricPole>> getAllEP();

    /**
     * Gets e pby id.
     *
     * @param ide the ide
     * @return the e pby id
     */
//Lấy thông tin của cột điện theo id của cột điện
    @GET("electricpoles/{id}")
    Call<ElectricPole> getEPbyID(@Path("id") String ide);

    /**
     * Delete ep call.
     *
     * @param id the id
     * @return the call
     */
//Xóa cột điện
    @DELETE("electricpoles/{id}")
    Call<String> deleteEP(@Path("id") String id);

    /**
     * Update ep call.
     *
     * @param id           the id
     * @param electricPole the electric pole
     * @return the call
     */
//Chỉnh sửa thông tin của cột điện
    @PUT("electricpoles/{id}")
    Call<String> updateEP(@Path("id") String id, @Body ElectricPole electricPole);

    /**
     * Gets drone.
     *
     * @return the drone
     */
//Lấy danh sách thông tin của tất cả các Drone trong hệ thống
    @GET("drones/")
    Call<List<Drone>> getallDrone();

    /**
     * Countphoto call.
     *
     * @return the call
     */
//Đếm số lượng ảnh trong hệ thống
    @GET("photos/countall")
    Call<Integer> countphoto();

    /**
     * Countvideo call.
     *
     * @return the call
     */
//Đếm số lượng video trong hệ thống
    @GET("videos/countall")
    Call<Integer> countvideo();

    /**
     * Listimagecheck call.
     *
     * @param idpole the idpole
     * @param date   the date
     * @param crop   the crop
     * @return the call
     */
//Check đưa về danh sách id của các ảnh với thông tin đầu vào về cột điện, ngày và toàn thể hay bộ phận
    @GET("photos/listimagecheck")
    Call<List<String>> listimagecheck(@Query("idpole") String idpole,@Query("date") String date,@Query("crop") boolean crop);

    /**
     * Add inci call.
     *
     * @param incident the incident
     * @return the call
     */
//Thêm mới sự cố
    @POST("incidents")
    Call<String> addInci(@Body Incident incident);

    /**
     * Deleteinci call.
     *
     * @param id the id
     * @return the call
     */
//Xóa sự cố
    @DELETE("incidents/{id}")
    Call<String> deleteinci(@Path("id") String id);

    /**
     * Gets .
     *
     * @return the
     */
//Lấy thông tin của tất cả các sự cố trong hệ thống
    @GET("incidents")
    Call<List<Incident>> getallinci();

    /**
     * Confirminciall call.
     *
     * @return the call
     */
//Phê duyệt tất cả các sự cố được phát hiện tự động
    @GET("incidents/confirmall")
    Call<String> confirminciall();

    /**
     * Gets .
     *
     * @param status the status
     * @return the
     */
//Lấy thông tin của tất cả sự cố có cùng status
    @GET("incidents/status/{status}")
    Call<List<Incident>> getallinciwithstatus(@Path("status") String status);

    /**
     * Gets .
     *
     * @param id the id
     * @return the
     */
//Lấy sự cố theo id của sự cố
    @GET("incidents/{id}")
    Call<Incident> getincibyid(@Path("id") String id);

    /**
     * Modiffyincident call.
     *
     * @param id       the id
     * @param incident the incident
     * @return the call
     */
//Chỉnh sửa thông tin sự cố
    @PUT("incidents/{id}")
    Call<String> modiffyincident(@Path("id") String id, @Body Incident incident);

    /**
     * Addphoto call.
     *
     * @param title      the title
     * @param type       the type
     * @param dateCreate the date create
     * @param dateImport the date import
     * @param des        the des
     * @param idpole     the idpole
     * @param iduser     the iduser
     * @param iddrone    the iddrone
     * @param crop       the crop
     * @param image      the image
     * @return the call
     */
//Thêm ảnh mới vào hệ thống
    @POST("photos/add")
    @Multipart
    Call<String> addphoto(@Query("title") String title, @Query("type") String type, @Query("dateCreate") String dateCreate,
                          @Query("dateImport") String dateImport, @Query("des") String des,
                          @Query("idpole") String idpole, @Query("iduser") String iduser,
                          @Query("iddrone") String iddrone, @Query("crop") boolean crop, @Part MultipartBody.Part image);

    /**
     * Autoinci call.
     *
     * @return the call
     */
//Tự đông phát hiện sự cố
    @GET("photos/autoinci")
    Call<List<String>> autoinci();

    /**
     * Gets url.
     *
     * @param url the url
     * @return the url
     */
//Lấy thông tin của một links với url
    @GET("links/url/")
    Call<Link> getlinkbyUrl(@Query("url") String url);

    /**
     * Gets .
     *
     * @param code the code
     * @return the
     */
//lấy danh sách hành trình bay
    @GET("mission/getall/")
    Call<List<Mission>> getallmission(@Query("code") String code);

    //lấy danh sách hành trình bay theo cột điện hoặc điểm
    @GET("poleandpointmission/getall/")
    Call<List<PoleAndPointMission>> getallpoleandpointmission(@Query("code") String code);

    //lấy danh sách mẫu hành trình bay
    @GET("templatepolemission/getall/")
    Call<List<TemplatePoleMission>> getalltemplatepolemission(@Query("code") String code);
}
