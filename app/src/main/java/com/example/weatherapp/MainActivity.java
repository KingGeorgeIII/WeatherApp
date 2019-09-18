package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    Button btnShowCoord;
    EditText edtAddress;
    double lat;
    double lng;
    double wind;
    double precip;
    double humidity;
    double temp;
    //Royce Driving
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // George is Driving
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowCoord = (Button)findViewById(R.id.button);
        edtAddress = (EditText)findViewById(R.id.editText);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void searchButton(View view){
        // Royce is Driving
        String input;
        Log.d("Tagg", (input = edtAddress.getText().toString().replace(" ", "+")));
        new GetCoordinates().execute(input);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //George Driving
    @Override
    public void onMapReady(GoogleMap map) {
        // Royce
        GoogleMap mMap = map;
        LatLng Location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(Location).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));

    }


    private class GetCoordinates extends AsyncTask<String,Void,String> {
        // George is driving

        @Override
        protected String doInBackground(String... strings) {
            String inputLine;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s, +CA&key=AIzaSyB4p_hGQiWdC3ReDyzbYHlL2EvosOYT5aA",address);
             //    String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyB4p_hGQiWdC3ReDyzbYHlL2EvosOYT5aA");
                InputStreamReader mapReader = http.getHTTPData(url);
                BufferedReader reader = new BufferedReader(mapReader);
                StringBuilder sb = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    //Log.d("Tag",inputLine);
                    sb.append(inputLine);
                }

                try{
                    JSONObject jsonObject = new JSONObject(sb.toString());

                    lat = (double) ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lat");
                    lng = (double) ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lng");
                    /*lat = jsonObject.getDouble("lat");
                    lng = jsonObject.getDouble("lng"); */



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // SKYNET
                HttpDataHandler http2 = new HttpDataHandler();
                String url2 = String.format(
                        "https://api.darksky.net/forecast/3b3f9bd54420df84f59a0588bc3521d6/%f,%f", lat,lng);
                InputStreamReader weatherReader = http2.getHTTPData(url2);
                BufferedReader reader2 = new BufferedReader(weatherReader);
                StringBuilder strb = new StringBuilder();
                String in;
                while((in = reader2.readLine()) != null){
                    strb.append(in);
                }

                //Royce is Driving
                try{
                    JSONObject jsonObject = new JSONObject(strb.toString());

                    temp = (double) jsonObject.getJSONObject("currently").getDouble("temperature");
                    humidity = (double) jsonObject.getJSONObject("currently").getDouble("humidity");
                    wind = (double) jsonObject.getJSONObject("currently").getDouble("windSpeed");
                    precip = (double) jsonObject.getJSONObject("currently").getDouble("precipProbability");
                    /*lat = jsonObject.getDouble("lat");
                    lng = jsonObject.getDouble("lng"); */

                    Log.d("Tag", String.valueOf(temp) + " " + String.valueOf(humidity) +
                            " " + String.valueOf(wind) + " " + String.valueOf(precip));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String result = "Latitude: " + String.valueOf(lat) + "   Longitude: " + String.valueOf(lng) + "\nTemperature: " +
                        String.valueOf(temp) + "   Humidity: " + String.valueOf(humidity) +
                        "   Wind Speed: " + String.valueOf(wind) + "   Precipitation: " + String.valueOf(precip);
                return result;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        //George is Driving
        @Override
        protected void onPostExecute(String s) {
            TextView txt = (TextView) findViewById(R.id.textView2);
            txt.setText(s);
        }
    }



}

