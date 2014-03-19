package com.deere.acreage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import android.widget.Toast;


public class CalculateAreaActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Location lastLoc;
    /*final Button btnStart = (Button) findViewById(R.id.btnStart);
    final Button btnStop = (Button) findViewById(R.id.btnStop);
    final Button btnReceipt = (Button) findViewById(R.id.btnReceipt);*/

    TextView txtLat;
    TextView txtLong;
    List<Location> locations = new ArrayList<Location>();

    LocationManager mlocManager;
    LocationListener mlocListener;
    Boolean flagStart = false;
    File file;
    OutputStream fo;
    String newLine = "\n";
    SimpleDateFormat dateFormat;
    final Context context = this;

    //   Boolean flagRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcarea);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss");


//        Location lastKnownLocation = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            System.out.println("*************************************GPS is Enabled in your devide");
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        //LocationProvider provider =locationManager.getProvider(LocationManager.GPS_PROVIDER);
        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        //mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnStop = (Button) findViewById(R.id.btnStop);
        //final Button btnRecord = (Button) findViewById(R.id.btnRecord);
        //final Button btnReceipt = (Button) findViewById(R.id.btnReceipt);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);


        txtLat = (TextView) findViewById(R.id.TextViewLatVal);
        txtLong = (TextView) findViewById(R.id.TextViewLongVal);
        // txtLocation = (TextView) findViewById(R.id.locationView);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                flagStart = true;
                try {
                    file = new File(Environment.getExternalStorageDirectory() + "/GAANDHILocationsfile.txt");
                    fo = new FileOutputStream(file, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("***********************************************************************file exception................................................................");
                    e.printStackTrace();
                }
                locations.clear();
                System.out.println("start button clicked !!");
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                //	btnRecord.setEnabled(false);

                //Location lastLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    			/*System.out.println(lastLoc.getLatitude());
    			System.out.println(lastLoc.getLongitude());
    			System.out.println("-------------------------");
       		*/
            }
        });

	
		/*Criteria criteria = new Criteria(); 
		criteria.setAccuracy(Criteria.ACCURACY_HIGH);   
		lm.getBestProvider(criteria, true); 
*/

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mlocManager.removeUpdates(mlocListener);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    /* Class MyLocationListener starts here */
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
//			if (isBetterLocation(lastLoc, loc)) {
//				lastLoc = loc;
//			}

            lastLoc = loc;
            txtLat.setText("" + lastLoc.getLatitude());
            txtLong.setText("" + lastLoc.getLongitude());
            System.out.println("**************************Inside onLocationChanged:: Location=" + lastLoc);
		        /*Toast toast1=Toast.makeText( getApplicationContext(), "File written..", Toast.LENGTH_SHORT );
	   		 	toast1.show();*/
            //filewriter.close();

            if (flagStart == true) {

                try {
                    fo.write(("Latitude:  " + ((Double) (loc.getLatitude())).toString()).getBytes());
                    fo.write((" Longitude:  " + ((Double) (loc.getLongitude())).toString()).getBytes());
                    fo.write((" Time:  " + (dateFormat.format(loc.getTime())).toString()).getBytes());
                    fo.write((" Provider:  " + (loc.getProvider())).getBytes());
                    fo.write((" Accuracy:  " + ((Float) (loc.getAccuracy()))).toString().getBytes());
                    fo.write("\n".getBytes());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                locations.add(lastLoc);

            }
			/*Toast toast=Toast.makeText( getApplicationContext(), "Inside onLocationChanged", Toast.LENGTH_SHORT );
			toast.show();*/
        }

        public void onProviderDisabled(String provider) {
            //Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
        }

        public void onProviderEnabled(String provider) {
            //Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        private static final int TWO_MINUTES = 1000 * 60 * 2;

        /**
         * Determines whether one Location reading is better than the current Location fix  *
         *
         * @param location The new Location that you want to evaluate
         *                 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
         */

        protected boolean isBetterLocation(Location location, Location currentBestLocation) {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true;
            }
            // Check whether the new location fix is newer or older
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;
            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true;
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false;
            }

            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;
            // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
            // Determine location quality using a combination of timeliness and accuracy

            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }

            return false;
        }

        /**
         * Checks whether two providers are the same
         */
        private boolean isSameProvider(String provider1, String provider2) {
            if (provider1 == null) {
                return provider2 == null;
            }
            return provider1.equals(provider2);
        }

    }

    /* Class MyLocationListener ends here */
    public void calculateArea(View v) {
        //ArrayList<Location> locations1 = new ArrayList<Location>();
		/* GeoPoint gp1 = new GeoPoint(1851627, 73928391);
		 GeoPoint gp2 = new GeoPoint(18516456, 73928729);
		 GeoPoint gp3 = new GeoPoint(18516911, 73927753);
		 GeoPoint gp4 = new GeoPoint(18517176, 73927954);
		 */
		 
/*		Location loc1 = new Location(LocationManager.GPS_PROVIDER);
		 loc1.setLatitude(18.51627);
		 loc1.setLongitude(73.928391);
		 Location loc2 = new Location(LocationManager.GPS_PROVIDER);
		 loc2.setLatitude(18.516456);
		 loc2.setLongitude(73.928729);
		 Location loc3 = new Location(LocationManager.GPS_PROVIDER);
		 loc3.setLatitude(18.517176);
		 loc3.setLongitude(73.927954);
		 Location loc4 = new Location(LocationManager.GPS_PROVIDER);
		 loc4.setLatitude(18.516911);
		 loc4.setLongitude(73.927753);*/
		/*
	 Location loc1 = new Location(LocationManager.GPS_PROVIDER);
		 loc1.setLatitude(18.51153658267058);
		 loc1.setLongitude(73.77851526441646);
		 Location loc2 = new Location(LocationManager.GPS_PROVIDER);
		 loc2.setLatitude(18.511559473546615);
		 loc2.setLongitude(73.77874861660075);
		 Location loc3 = new Location(LocationManager.GPS_PROVIDER);
		 loc3.setLatitude(18.511208479777704);
		 loc3.setLongitude(73.77880762519908);
		 Location loc4 = new Location(LocationManager.GPS_PROVIDER);
		 loc4.setLatitude(18.51118304541864);
		 loc4.setLongitude(73.77855549755168);
		 Location loc5 = new Location(LocationManager.GPS_PROVIDER);
		 loc5.setLatitude(18.51153658267058);
		 loc5.setLongitude(73.77851526441646);
		 */
		/* Location loc1 = new Location(LocationManager.GPS_PROVIDER);
		 loc1.setLatitude(18.511694275309996);
		 loc1.setLongitude(73.77751480041297);
		 Location loc2 = new Location(LocationManager.GPS_PROVIDER);
		 loc2.setLatitude(18.511709535880293);
		 loc2.setLongitude(73.77809147535118);
		 Location loc3 = new Location(LocationManager.GPS_PROVIDER);
		 loc3.setLatitude(18.511559473546615);
		 loc3.setLongitude(73.7784374803141);
		 Location loc4 = new Location(LocationManager.GPS_PROVIDER);
		 loc4.setLatitude(18.51119067572676);
		 loc4.setLongitude(73.77850185333045);
		 Location loc5 = new Location(LocationManager.GPS_PROVIDER);
		 loc5.setLatitude(18.51115761105581);
		 loc5.setLongitude(73.77815316615852);
		 Location loc6 = new Location(LocationManager.GPS_PROVIDER);
		 loc6.setLatitude(18.511124546378465);
		 loc6.setLongitude(73.7776810973719);
		 Location loc7 = new Location(LocationManager.GPS_PROVIDER);
		 loc7.setLatitude(18.511694275309996);
		 loc7.setLongitude(73.77751480041297);*/
		 
		 /*Location loc1 = new Location(LocationManager.GPS_PROVIDER);
		 loc1.setLatitude(18.51105333012866);
		 loc1.setLongitude(73.77783666549476);
		 Location loc2 = new Location(LocationManager.GPS_PROVIDER);
		 loc2.setLatitude(18.51107367763164);
		 loc2.setLongitude(73.77805392442497);
		 Location loc3 = new Location(LocationManager.GPS_PROVIDER);
		 loc3.setLatitude(18.51086257217066);
		 loc3.setLongitude(73.77828727660926);
		 Location loc4 = new Location(LocationManager.GPS_PROVIDER);
		 loc4.setLatitude(18.51066927055632);
		 loc4.setLongitude(73.77856086192878);
		 Location loc5 = new Location(LocationManager.GPS_PROVIDER);
		 loc5.setLatitude(18.510259775625787);
		 loc5.setLongitude(73.77860645948203);
		 Location loc6 = new Location(LocationManager.GPS_PROVIDER);
		 loc6.setLatitude(18.510239428026043);
		 loc6.setLongitude(73.77836237846168);
		 Location loc7 = new Location(LocationManager.GPS_PROVIDER);
		 loc7.setLatitude(18.51059042378251);
		 loc7.setLongitude(73.77805124221595);
		 Location loc8 = new Location(LocationManager.GPS_PROVIDER);
		 loc8.setLatitude(18.510175841761264);
		 loc8.setLongitude(73.77815584836753);
		 Location loc9 = new Location(LocationManager.GPS_PROVIDER);
		 loc9.setLatitude(18.510127516184216);
		 loc9.setLongitude(73.77737264333518);
		 Location loc10 = new Location(LocationManager.GPS_PROVIDER);
		 loc10.setLatitude(18.51052938109397);
		 loc10.setLongitude(73.77721171079429);
		 Location loc11 = new Location(LocationManager.GPS_PROVIDER);
		 loc11.setLatitude(18.510623488563045);
		 loc11.setLongitude(73.77763281760963);
		 Location loc12 = new Location(LocationManager.GPS_PROVIDER);
		 loc12.setLatitude(18.510702335321625);
		 loc12.setLongitude(73.7777347415522);
		 Location loc13 = new Location(LocationManager.GPS_PROVIDER);
		 loc13.setLatitude(18.510966853214057);
		 loc13.setLongitude(73.77764891086372);
		 Location loc14 = new Location(LocationManager.GPS_PROVIDER);
		 loc14.setLatitude(18.51105333012866);
		 loc14.setLongitude(73.77783666549476);
		
		 locations.add(loc1);
		 locations.add(loc2);
		 locations.add(loc3);
		 locations.add(loc4);
		 locations.add(loc5);
		 locations.add(loc6);
		 locations.add(loc7);
		 locations.add(loc8);
		 locations.add(loc9);
		 locations.add(loc10);
		 locations.add(loc11);
		 locations.add(loc12);
		 locations.add(loc13);
		 locations.add(loc14);*/


        Toast toast = Toast.makeText(getApplicationContext(), "No. Of Locations: " + locations.size(), Toast.LENGTH_SHORT);
        toast.show();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!length:: " + locations.size());
        double area = CalcArea.calcArea(locations);
        System.out.println("*************************Area:: " + area);
        // txtLocation = (TextView) findViewById(R.id.locationView);
		 /*
		  * Following activities are to rollback the changes and make the screen reset to its original state
		  */
        txtLat = (TextView) findViewById(R.id.TextViewLatVal);
        txtLong = (TextView) findViewById(R.id.TextViewLongVal);

        txtLat.setText("00.0000");
        txtLong.setText("00.0000");
        mlocManager.removeUpdates(mlocListener);
        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setEnabled(true);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(false);

        DecimalFormat df = new DecimalFormat("####0.00");
		/* txtLat.setText(""+ loc1.getLatitude());
		 txtLong.setText(""+ loc1.getLongitude());*/
        // txtLocation.setText("Area is : "+df.format(area)+" sq. meters");
        for (Location location : locations) {
            System.out.println("**********");
            System.out.println("---------lat:" + location.getLatitude());
            System.out.println("---------lon:" + location.getLongitude());
        }
        locations.clear();
        try {
            fo.write(("\n Area:::: " + df.format(area) + "\n").getBytes());
            fo.write("***********************************************************************************\n\n".getBytes());
            fo.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Intent myIntent = new Intent(this, ShowAreaActivity.class);
        myIntent.putExtra("Area", df.format(area));

        CalculateAreaActivity.this.startActivity(myIntent);
        // startActivityForResult(myIntent, 0);
    }

    public void printReceipt(View v) {

        Intent myIntent = new Intent(CalculateAreaActivity.this, DetailsActivity.class);
        CalculateAreaActivity.this.startActivity(myIntent);

    }

    public void record(View v) {
        locations.add(lastLoc);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(true);
    }

    @Override
    public void onBackPressed() {

    }

    public void onCancelArea(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Warning!");
        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to cancel this operation?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent myIntent = new Intent(CalculateAreaActivity.this, ContactsActivity.class);
                        CalculateAreaActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
} 

