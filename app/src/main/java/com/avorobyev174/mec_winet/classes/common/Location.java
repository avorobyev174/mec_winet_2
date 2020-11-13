package com.avorobyev174.mec_winet.classes.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class Location {
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Location(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {

            }
        };
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity.getApplicationContext(), "Доступ к локации не предоставлен", Toast.LENGTH_SHORT).show();
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 1, locationListener);
        }
    }


}
