package edu.unc.epeden.locationapp590;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MediaPlayer.OnPreparedListener {

    private GoogleApiClient c = null;
    LocationRequest location_request;
    Location location;
    Geocoder geocoder;
    MediaPlayer music_player = null;
    boolean listener_paused = false;
    String music_url;
    boolean atSite = false;

    ImageView oldwellMarker;
    ImageView polkplaceMarker;
    ImageView fredbrooksMarker;
    TextView longitudeTextview;
    TextView latitudeTextview;
    TextView addressTextview;
    TextView siteTextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s = "";

        /* Assign Textviews and Imageviews by id. Then set them to their default values. */
        siteTextview = (TextView) findViewById (R.id.site_textview);
        longitudeTextview = (TextView) findViewById (R.id.longitude_textview);
        latitudeTextview = (TextView) findViewById (R.id.latitude_textview);
        addressTextview = (TextView) findViewById (R.id.address_textview);
        s = "Longitude: ";
        longitudeTextview.setText(s);
        s = "Latitude: ";
        latitudeTextview.setText(s);
        s = "Address: ";
        addressTextview.setText(s);


        oldwellMarker = (ImageView)findViewById(R.id.oldwell_marker);
        polkplaceMarker = (ImageView)findViewById(R.id.polkplace_marker);
        fredbrooksMarker = (ImageView)findViewById(R.id.fredbrooks_marker);
        oldwellMarker.setVisibility(View.INVISIBLE);
        polkplaceMarker.setVisibility(View.INVISIBLE);
        fredbrooksMarker.setVisibility(View.INVISIBLE);


        c = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();


        /* Create new LocationRequest and set interval. */
        location_request = new LocationRequest();
        location_request.setInterval(15000);
        location_request.setFastestInterval(10000);
        location_request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        /* Initialize geocoder for translating a coordinate into an address (reverse geocoding). */
        geocoder = new Geocoder(this, Locale.getDefault());

    }
    public void onConnected(Bundle bundle) {
        String s = "";
        try {
            /* Set current Textviews to values corresponding to current location. */
            location = LocationServices.FusedLocationApi.getLastLocation(c);
            s = "Longitude: " + location.getLongitude();
            longitudeTextview.setText(s);
            s = "Latitude: " + location.getLatitude();
            latitudeTextview.setText(s);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                s = "Address: " + addresses.get(1).toString();
                addressTextview.setText(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s= "CONNECTED";
            siteTextview.setText(s);
            /* Start location updates. */
            LocationServices.FusedLocationApi.requestLocationUpdates(c, location_request, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    public void onPrepared(MediaPlayer mp) {
        if(mp != null) {
            mp.start();
        }
    }
    public void onLocationChanged(Location location) {
        String s = "";
        if (!listener_paused) {
            try {
                location = LocationServices.FusedLocationApi.getLastLocation(c);
                s = "Longitude: " + location.getLongitude();
                longitudeTextview.setText(s);
                s = "Latitude: " + location.getLatitude();
                latitudeTextview.setText(s);
                /* Start location updates. */
                LocationServices.FusedLocationApi.requestLocationUpdates(c, location_request, this);

                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    s = "Address: " + addresses.get(0).getAddressLine(0);
                    addressTextview.setText(s);

                    /* Check if current coordinates is within radius of one of the desired sites. */
                    if(((location.getLatitude() > 35.91158 && location.getLatitude() < 35.91235)&&(location.getLongitude() > -79.0514 && location.getLongitude() < -79.05107)) || (addresses.get(0).getAddressLine(0).contains("263 E Cameron")||addresses.get(0).getAddressLine(0).contains("250 E Cameron") )){
                        music_url = "http://storage.mp3.cc/download/9803804/RjVqZVRYRHpBNEZyTjhBWmY1OEd4MUdIUUU3SXd4N21YK1RGeENhdDJFNFh4WHg5L1B4eGtWcE5oazlQSmtLWXRmWUtqOVU4Y3JRTTNlczhkbmM1UzVtNy9YdUloZFErVFRzMXVYcnpKMVllYjV6bU9xKzVGeVdBdU9sSHhHR2w/Maroon_5-Sugar_Sahar_(mp3.cc).mp3";//old well
                        oldwellMarker.setVisibility(View.VISIBLE);
                        s = "You're at the Old Well.";
                        siteTextview.setText(s);
                        enteredSite();
                    } else if(((location.getLatitude() > 35.91055 && location.getLatitude() < 35.91098)&&(location.getLongitude() > -79.049 && location.getLongitude() < -79.051)) || (addresses.get(0).getAddressLine(0).contains("180 E Cameron Ave"))){
                        music_url = "http://dl.songsmp3.com/fileDownload/Songs/128/1223.mp3";
                        polkplaceMarker.setVisibility(View.VISIBLE);
                        s = "You're at Polk Place.";
                        siteTextview.setText(s);
                        enteredSite();
                    } else if(((location.getLatitude() > 35.90948 && location.getLatitude() < 35.90977)&&(location.getLongitude() > -79.05325 && location.getLongitude() < -79.05277)) || (addresses.get(0).getAddressLine(0).contains("201 S Columbia"))) {
                        music_url = "http://dl.songsmp3.com/fileDownload/Songs/128/25141.mp3";
                        fredbrooksMarker.setVisibility(View.VISIBLE);
                        s = "You're at the Fred Brooks building.";
                        siteTextview.setText(s);
                        enteredSite();
                    } else {
                        music_player.reset();
                        oldwellMarker.setVisibility(View.INVISIBLE);
                        fredbrooksMarker.setVisibility(View.INVISIBLE);
                        polkplaceMarker.setVisibility(View.INVISIBLE);
                        siteTextview.setText("");
                        atSite = false;
                    }


                    /* Check if current geoaddress is one of the desired sites. */
//                    if(addresses.get(0).getAddressLine(0).contains("263 E Cameron")||addresses.get(0).getAddressLine(0).contains("250 E Cameron") ){
//                        music_url = "http://picosong.com/cdn/911ba4774eb74f289b40c5cd4187302e.mp3";
//                        oldwellMarker.setVisibility(View.VISIBLE);
//                        siteTextview.setText("You're at the Old Well.");
//                        enteredSite();
//                    } else if(addresses.get(0).getAddressLine(0).contains("180 E Cameron Ave")){
//                        music_url = "http://dl.songsmp3.com/fileDownload/Songs/128/1223.mp3";
//                        polkplaceMarker.setVisibility(View.VISIBLE);
//                        siteTextview.setText("You're at Polk Place.");
//                        enteredSite();
//                    } else if(addresses.get(0).getAddressLine(0).contains("201 S Columbia") || addresses.get(0).getAddressLine(0).contains("264-298 S Columbia")) {
//                        music_url = "http://dl.songsmp3.com/fileDownload/Songs/128/25141.mp3";
//                        fredbrooksMarker.setVisibility(View.VISIBLE);
//                        siteTextview.setText("You're at Fred Brooks building.");
//                        enteredSite();
//                    } else {
//                        music_player.reset();
//                        oldwellMarker.setVisibility(View.INVISIBLE);
//                        fredbrooksMarker.setVisibility(View.INVISIBLE);
//                        polkplaceMarker.setVisibility(View.INVISIBLE);
//                        siteTextview.setText("");
//                        atSite = false;
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void enteredSite() {
        try {
            if(!atSite) {
                if (music_player != null) {
                    music_player.release();
                    music_player = null;
                }
                music_player = new MediaPlayer();
                music_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                music_player.setDataSource(music_url);
                music_player.setOnPreparedListener(this);
                music_player.prepareAsync();
                atSite = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void onStart(){
        c.connect();
        super.onStart();
    }
    protected void onStop(){
        c.disconnect();
        super.onStop();
    }

    public void onPause(){
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(c, this);
        listener_paused = true;
        music_player.stop();
    }
    public void onResume() {
        super.onResume();
        listener_paused = false;
        if (location != null) {
            c.connect();
        }

    }


    public void onConnectionSuspended(int i){


    }
    public void onConnectionFailed(ConnectionResult connectionResult){

    }


}
