package com.deere.acreage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MarkOnMaps extends Activity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {


    public static final String TOTAL_LOCATION_COUNT = "totalLocation";
    public static final String LAT = "LAT-";
    public static final String LONG = "LONG-";
    List<Integer> existingMarkers = new ArrayList<Integer>();
    LatLng locationBeingDragged = null;
    private LocationUtils locationUtils;
    private GoogleMap map;
    private Polygon polygon;
    private boolean isMap = true;
    private boolean showCurrentLoc = true;
    private double areaInSqMeter;
    private double areaInAcre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showCurrentLoc = true;
        super.onCreate(savedInstanceState);
        locationUtils = LocationUtils.getLocationUtilsInstance(this);
        setContentView(R.layout.mark_on_maps);
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setOnMapLongClickListener(this);
        map.setOnMarkerDragListener(this);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationUtils.getCurrentLatlng(), 17));
//        showCoordinatesOnMap();

    }

    public void toggleView(View view) {
        if (isMap == true) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            isMap = false;
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            isMap = true;
        }
    }

    public void doneWithMarking(View view)
    {
        Intent intent = new Intent(this,
                DetailsActivity.class);
        Bundle sendBundle = new Bundle();
        sendBundle.putDouble("Area", areaInSqMeter);
        sendBundle.putDouble("AcreArea", areaInAcre);
        sendBundle.putString("Flag", "AreaBased");
        intent.putExtras(sendBundle);
        onBackPressed();
        startActivity(intent);
    }

    public void deleteLastMarker(View view)
    {
        locationUtils.deleteLastMarker();
        showCoordinatesOnMap();

    }

    private void showCoordinatesOnMap() {


        if (locationUtils.servicesConnected()) {
            spotCurrentLocation(true);
        }

        if (null != polygon) {
            polygon.remove();
        }
        map.clear();
        existingMarkers = new ArrayList<Integer>();

        List<LatLng> userLocations = locationUtils.getUserLatLongs();
        if (!userLocations.isEmpty()) {
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.fillColor(0x500011FF).strokeColor(0x50444444).strokeWidth(2);
            polygon = map.addPolygon(polygonOptions.addAll(userLocations));

            TextView textView = (TextView) findViewById(R.id.txtMapArea);
             areaInSqMeter = AreaCalculator.calculateAreaOfGPSPolygonOnEarthInSquareMeters(locationUtils.getUserLocations());
             areaInAcre = areaInSqMeter * 0.000247;
            DecimalFormat df = new DecimalFormat("####0.00");
            textView.setText(df.format(areaInAcre) + " Acres" );
        } else {
            spotCurrentLocation(true);
        }

        for (LatLng loc : userLocations) {
            addMarkerIfAlreadyNotPresent(loc);
        }
        map.setMyLocationEnabled(true);

    }

    public void tagCurrentLocation(View view) {
        locationUtils.storeCurrentLocation();
        Location currentLocation = locationUtils.getCurrentLocation();
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        addMarkerIfAlreadyNotPresent(latLng);

        showCoordinatesOnMap();
    }

    public void spotCurrentLocation(Boolean zoomToLoc) {
        Location currentLocation = locationUtils.getCurrentLocation();

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        map.setMyLocationEnabled(true);

        if (zoomToLoc) {
            zoomToCurrentLoc(latLng);
        }

    }

    private void zoomToCurrentLoc(LatLng latLng) {
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        locationUtils.storeLocationInSharedContext(location);
        addMarkerIfAlreadyNotPresent(latLng);
        showCoordinatesOnMap();

    }

    private void addMarkerIfAlreadyNotPresent(LatLng latLng) {
        Integer latLngIndex = locationUtils.getLatLngIndex(latLng);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(200, 50, conf);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(25);

        canvas.drawText("hello", 0, 50, paint);


        if (!existingMarkers.contains(latLngIndex) && (latLngIndex != null)) {
            Marker marker = map.addMarker(new MarkerOptions().draggable(true).position(latLng).alpha(0.7f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            marker.setTitle(latLngIndex.toString());
            marker.showInfoWindow();
            existingMarkers.add(latLngIndex);

        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, map.getCameraPosition().zoom));

    }

    private void removeAllMarkers()
    {
//        if(!existingMarkers.isEmpty())
//        {
//            map.cl
//        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        locationBeingDragged = locationUtils.getLatLongsByIndex(Integer.parseInt(marker.getTitle()));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        locationUtils.replaceLocationValues(locationBeingDragged, marker.getPosition());
        showCoordinatesOnMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), map.getCameraPosition().zoom));
    }

    @Override
    public void onBackPressed() {
        SharedPreferences myLocationPref = getSharedPreferences("myLocationPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = myLocationPref.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }
}