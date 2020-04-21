package com.example.mapnotes;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager ;
    LocationListener locationListener ;
    int id ;

    public void centerMapOnLocation(Location location, String title) {
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        final Intent intent = getIntent() ;
        id = intent.getIntExtra("id",-1) ;

        if( id == -1 ){

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE) ;
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location, "Your are here");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            } ;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

        } else {
            Location location = new Location(LocationManager.GPS_PROVIDER) ;
            location.setLatitude(mapNoteFragment.locations.get(id).latitude);
            location.setLongitude(mapNoteFragment.locations.get(id).longitude);

            centerMapOnLocation(location, mapNoteFragment.places.get(id) );

        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                mapNoteFragment.mapNotesTitle.add("Memorable Place") ;
                mapNoteFragment.arrayAdapter.notifyDataSetChanged();
                mapNoteFragment.mapNotesContent.add("") ;
                mapNoteFragment.locations.add(latLng) ;
                int noteId = mapNoteFragment.locations.size()-1 ;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                String address = "";

                try {

                    List<Address>  foundAddress = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

                    if (foundAddress != null && foundAddress.size() > 0) {
                        if (foundAddress.get(0).getThoroughfare() != null) {
                            if (foundAddress.get(0).getSubThoroughfare() != null) {
                                address += foundAddress.get(0).getSubThoroughfare() + " ";
                            }
                            address += foundAddress.get(0).getThoroughfare();
                        }
                        if( foundAddress.get(0).getLocality() != null )
                            address += ", " + foundAddress.get(0).getLocality() ;
                        if( foundAddress.get(0).getAdminArea() != null )
                            address += ", " + foundAddress.get(0).getAdminArea() ;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mapNoteFragment.places.add(address) ;

                try {
                    String str = ObjectSerializer.serialize(mapNoteFragment.mapNotesTitle) ;
                    MainActivity.sharedPreferences.edit().putString( "mnTitle", str ).apply();
                    str = ObjectSerializer.serialize( mapNoteFragment.mapNotesContent) ;
                    MainActivity.sharedPreferences.edit().putString("mnContent",str).apply();
                    str = ObjectSerializer.serialize(mapNoteFragment.places) ;
                    MainActivity.sharedPreferences.edit().putString("places",str).apply();

                    ArrayList<String> lat = new ArrayList<>();
                    ArrayList<String> lon = new ArrayList<>();

                    for (LatLng coord : mapNoteFragment.locations) {
                        lat.add(Double.toString(coord.latitude));
                        lon.add(Double.toString(coord.longitude));
                    }

                    str = ObjectSerializer.serialize(lat) ;
                    MainActivity.sharedPreferences.edit().putString("lats",str).apply();
                    str = ObjectSerializer.serialize(lon) ;
                    MainActivity.sharedPreferences.edit().putString("lons",str).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent1 = new Intent(getApplicationContext(), MapNotesEditor.class) ;
                intent1.putExtra("noteId",noteId) ;
                startActivity(intent1);
            }
        });



    }


}

















