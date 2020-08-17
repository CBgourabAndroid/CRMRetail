package com.crmretail

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.crmretail.modelClass.*
import com.crmretail.modelClass.attandence.AttendanceResponce
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface PostInterface {





    companion object {
        const val BaseURL = "http://api.shakambharigroup.in/api/v1/"


        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        fun format_date(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("EEE, dd MMM yyyy")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }


        fun format_date4(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("dd")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_date5(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("MM")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_date6(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("yyyy")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }


        fun format_date3(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("MMM")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun getCalculatedDate(dateFormat: String?, days: Int): String? {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat(dateFormat)
            cal.add(Calendar.DAY_OF_YEAR, days)
            return s.format(Date(cal.getTimeInMillis()))
        }

        @SuppressLint("SimpleDateFormat")
        fun checkDate(startDate: String?, endDate: String?): Boolean {
            var b = false
            val format2 = SimpleDateFormat("hh:mm")
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val odf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cud = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
                val cld = SimpleDateFormat("yyyy-MM-dd").parse(endDate)
                val strD = inputFormat.format(cud)
                val endD = inputFormat.format(cld)
                b = if (inputFormat.parse(endD).before(inputFormat.parse(strD))) {
                    false // If start date is before end date.
                } else {
                    true // If start date is after the end date.
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return b
        }


        @SuppressLint("SimpleDateFormat")
        fun checkTime(startDate: String?, endDate: String?): Boolean {
            var b = false
            val format2 = SimpleDateFormat("hh:mm")
            try {
                val inputFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
                val odf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cud = SimpleDateFormat("HH:mm:ss").parse(startDate)
                val cld = SimpleDateFormat("HH:mm:ss").parse(endDate)
                val strD = inputFormat.format(cud)
                val endD = inputFormat.format(cld)
                b = if (inputFormat.parse(endD).before(inputFormat.parse(strD))) {
                    false // If start date is before end date.
                } else {
                    true // If start date is after the end date.
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return b
        }

    }

    @FormUrlEncoded
    @POST("login")
    fun loginCall( @Field("email") email: String,
                   @Field("password") password: String,
                   @Field("app_type") app_type: String,
                   @Field("user_type") user_type: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<LoginResponse>



    @FormUrlEncoded
    @POST("logout")
    fun logoutCall(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<GeneralResponce>

    /*@FormUrlEncoded
    @POST("duty-start")
    fun startDuty( @Header("Authorization") String token,
                   @Field("user_id") user_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String
                   ): Call<LoginResponse>*/
    @FormUrlEncoded
    @POST("duty-activity")
    fun startDuty(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("lati") lati: String,
        @Field("longi") longi: String,
        @Field("duty_type") duty_type: String,
        @Field("customerId")customerId:String
    ): Call<GeneralResponce>


    @FormUrlEncoded
    @POST("outstanding")
    fun outstanding(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<OutstandingResponce>


    @FormUrlEncoded
    @POST("payments")
    fun payments(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<PaymentResponce>


    @FormUrlEncoded
    @POST("ledger")
    fun ledger(
        @Header("Authorization") token: String,
        @Field("customer_id") customerId: String
    ): Call<LedgerResponse>

    @FormUrlEncoded
    @POST("all-customer")
    fun getcustomer(
        @Header("Authorization") token: String,
        @Field("user_id") customerId: String
    ): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("not-closed")
    fun getFeedbacklist(
        @Header("Authorization") token: String,
        @Field("user_id") customerId: String
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("not-closed-by-id")
    fun getFeedbackDetails(
        @Header("Authorization") token: String,
        @Field("registration_id") customerId: String
    ): Call<FeedbackDetailsResponse>


    @FormUrlEncoded
    @POST("billing")
    fun billing(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<BillResponce>


    @FormUrlEncoded
    @POST("duties")
    fun dutyplaner(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<DutyResponce>

    @FormUrlEncoded
    @POST("holidays")
    fun holidays(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ): Call<HolydayResponse>


    @FormUrlEncoded
    @POST("leave-apply")
    fun applyLeave(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("fromDate") fromDate: String,
        @Field("toDate") toDate: String,
        @Field("reason") reason: String

    ): Call<GeneralResponce2>

    @FormUrlEncoded
    @POST("attendance")
    fun attendence(
        @Header("Authorization") token: String,
        @Field("id") user_id: String
    ): Call<AttendanceResponce>


    @FormUrlEncoded
    @POST("get-user-location")
    fun getLocationList(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<LocationResponce>



    @FormUrlEncoded
    @POST("save-discussion")
    fun discussionCall(@Header("Authorization") token: String,
                   @Field("discussion") discussion: String,
                   @Field("reg_id") reg_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<GeneralResponce2>




/*

    @FormUrlEncoded
    @POST("registration_v2.php")
    fun signUpCall(
        @Field("emailid") emailid: String,
        @Field("mobilenumber") mobilenumber: String,
        @Field("userpassword") userpassword: String,
        @Field("username") username: String,
        @Field("familyname")familyname:String,
        @Field("dob")dob:String,
        @Field("gender") gender: String
    ): Call<SignUpResponce>

    @GET("forgetpassword.php?")
    fun forgotPass( @Query("emailid") emailid: String): Call<SignUpResponce>

    @FormUrlEncoded
    @POST("loginauth_v2.php")
    fun getOTP( @Field("otp") otp: String): Call<AuthResponce>


    @GET("speciality_v2.php")
    fun getSpeciality(): Call<SpecialityResponce>

    @FormUrlEncoded
    @POST("physician_speciality_v2.php")
    fun GetSpecialitycode(
        @Field("specialitycode") specialitycode: String

    ): Call<DoctorResponce>
*/



/*




    @FormUrlEncoded
    @POST("password_recovery")
    fun forgotCall(
        @Field("email") email: String

    ): Call<ForgotResponce>



    @Multipart
    @POST("verification_file_upload")
    fun uploadImage(@Part("userfile") file1: RequestBody,
                    @Part("user_id") user_id: String,
                    @Part("userfile2") file2:RequestBody
    ): Call<ImageResponce>


    @Multipart
    @POST("verification_file_upload")
    fun uploadFile(
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part("user_id") user_id: String
    ): Call<ValidationResponce>

    @Multipart
    @POST("retro_upload")
    fun uploadMulFile(
        @Part file1: MultipartBody.Part
       // @Part file2: MultipartBody.Part,
       // @Part("user_id") user_id: String
    ): Call<RetroTest>


    @GET("get_all_cabs")
    fun doGetListResources(): Call<SearchResponce>


  //  @GET("get_all_cabs_new")
   // fun GetListResources(): Call<CablistResponce>


    @FormUrlEncoded
    @POST("get_all_cabs_new")
    fun GetListResources(
        @Field("device_start_location")locationID: String
    ): Call<CablistResponce>



    @FormUrlEncoded
    @POST("booking")
    fun bookingCall(
        @Field("user_id") userid: String,
        @Field("bike_id") bid: String,
        @Field("package_id") pid: String,
        @Field("hour") hours: String,
        @Field("payment_amount") pAmount: String,
        @Field("start_date") sdate: String,
        @Field("end_date") edate: String,
        @Field("start_position") sloc: String,
        @Field("end_position") eloc: String,
        @Field("payment_status") pStatus: String,
        @Field("payment_mode") pMode: String,
        @Field("ride_status") rStatus: String,
        @Field("start_location") startloc: String,
        @Field("end_location") endloc: String

    ): Call<BookingResponce>

    @FormUrlEncoded
    @POST("booking_history")
    fun getBookingHistory(
        @Field("user_id")userid: String
    ): Call<BookingHistoryResponce>


    @FormUrlEncoded
    @POST("change_password")
    fun getChangePass(
        @Field("user_id")user_id: String,
        @Field("old_password")oldpass: String,
        @Field("new_password")newpass: String
    ): Call<SignupResponce>


    @FormUrlEncoded
    @POST("checkuser")
    fun getApproveStatus(
        @Field("user_id")user_id: String
    ): Call<ApproveResponce>






    @FormUrlEncoded
    @POST("insert_checklist")
    fun getCheckList(
        @Field("user_id")user_id: String,
        @Field("booking_id")booking_id: String,
        @Field("bike_id")bike_id: String,
        @Field("odometer_reading")odometer_reading: String,
        @Field("headlight")headlight: String,
        @Field("instrument_console")instrument_console: String,
        @Field("self_start")self_start: String,
        @Field("number_plate")number_plate: String,
        @Field("wiser")wiser: String,
        @Field("mud_guard")mud_guard: String,
        @Field("other_damage")other_damage: String,

        @Field("left_panel")left_panel: String,
        @Field("left_indicator_front")left_indicator_front: String,
        @Field("left_indicator_back")left_indicator_back: String,
        @Field("left_rear_view_mirror")left_rear_view_mirror: String,
        @Field("damage_lrft_side")damage_lrft_side: String,

        @Field("right_panel")right_panel: String,
        @Field("right_indicator_front")right_indicator_front: String,
        @Field("right_indicator_back")right_indicator_back: String,
        @Field("right_rear_view_mirror")right_rear_view_mirror: String,
        @Field("damage_right_side")damage_right_side: String,

        @Field("tail_light")tail_light: String,
        @Field("mud_guard_back")mud_guard_back: String,
        @Field("number_plate_back")number_plate_back: String,
        @Field("backside_damage")backside_damage: String,
        @Field("other_bike_issues")other_bike_issues: String
    ): Call<SignupResponce>




    @GET("get_all_pickup_and_drop_location")
    fun getAllLocation(): Call<LocationResponce>
*/






}