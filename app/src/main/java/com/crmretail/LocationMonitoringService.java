package com.crmretail;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crmretail.shared.Updatedlatlong;
import com.crmretail.shared.UserShared;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by devdeeds.com on 27-09-2017.
 */

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    private MultipartEntity reqEntity;
    String responseString = null;
    private Updatedlatlong psh;
    private UserShared pshuser;
    private SharedPreferences prefs;
    private double currentLat, currentLng;


    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }


    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);


        if (location != null) {
            Log.d(TAG, "== location != null");
            currentLat = location.getLatitude();
            currentLng = location.getLongitude();
            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

            pshuser = new UserShared(getApplicationContext());
            psh = new Updatedlatlong(getApplicationContext());
            reqEntity = null;
            reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                reqEntity.addPart("user_id", new StringBody(pshuser.getId()));
                reqEntity.addPart("lati", new StringBody(String.valueOf(location.getLatitude())));
                reqEntity.addPart("longi", new StringBody(String.valueOf(location.getLongitude())));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (pshuser.getDutyStatus()){
								/*	if (String.valueOf(currentLat).equals(psh.getUserUpdatedLatitude())){
										Log.v("Hey still running..!!!", "Location Update??");
									}
									else {

									}*/
                Log.v("Hey still running..!!!", "After 2min Update??");
                Toast.makeText(getApplicationContext(), "Hey still running..!!!", Toast.LENGTH_SHORT).show();
                UpdateLatLogToServer aTask = new UpdateLatLogToServer();
                aTask.execute((Void) null);


            }
            else{
                Log.v("Hey still running..!!!", "But i am off duty??");



            }

        }

    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }





    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }


    private class UpdateLatLogToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        @Override
        protected void onCancelled() {

        }

        protected void onPostExecute(final Boolean success) {
            try {

                if (!responseString.equals("")) {
                   // String msage = "latitude: " + lat + "  longitude: " + lon + "   Userid: " + userid + " // " + responseString;
                  //  Toast.makeText(LocationUpdate.this, responseString, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
			/*progressDialog = ProgressDialog.show(PersonalRegistrationActivity.this,
					"",
					getString(R.string.progress_bar_loading_message),
					false);*/
            super.onPreExecute();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(PostInterface.BaseURL+"update-location");

            try {


                /*to print in log*/

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                reqEntity.writeTo(bytes);

                String content = bytes.toString();

                Log.e("MultiPartEntityRequest:", content);

                /*to print in log*/

                httppost.setEntity(reqEntity);
                httppost.setHeader("Authorization", "Bearer " + pshuser.getAccessToken());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());
                Log.e(TAG, "TIME " + currentDateTime);


                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.shared_updated_lat), String.valueOf(currentLat));
                editor.putString(getString(R.string.shared_updated_long), String.valueOf(currentLng));
                editor.putString(getString(R.string.shared_updated_time), currentDateTime);
                editor.commit();

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                    JSONObject object=new JSONObject(responseString);
                    String msg=object.getString("message");
                    Log.e(TAG, "MESSAGE FROM SERVER " + msg);

                    SharedPreferences.Editor editor1 = prefs.edit();
                    editor1.putString("mycustomMSg", msg+" "+currentDateTime);
                    editor1.commit();
                    //Toast.makeText(LocationUpdate.this,msg,Toast.LENGTH_SHORT).show();

				/*	for (int i = 0; i <locationData.size(); i++) {

						if (lastUpdatedPos==i){

							locationData.clear();
							Log.v("Last Postion Delete",String.valueOf(i));
						}

					}*/

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseString;


        }
    }


}