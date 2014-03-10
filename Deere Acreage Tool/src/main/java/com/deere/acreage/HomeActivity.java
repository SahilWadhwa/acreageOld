package com.deere.acreage;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.*;
import android.os.Build;
import android.widget.*;

public class HomeActivity extends Activity {

    public static final String PREFS_NAME = "GAANDHIPrefs";
    Point p;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        float check= settings.getFloat("Ploughing",0);
        if(check==0)
        {
            editPreferences(settings);
        }

        setContentView(R.layout.activity_home);
        Button btn_settings = (Button) findViewById(R.id.btnSettings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Open popup window
                if (p != null)
                    showPopup(HomeActivity.this, p);
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height =  display.getHeight();
        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = (width/6);
        p.y =   (height/5);
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height =  display.getHeight();
        int popupWidth =height/2;
        int popupHeight = height/2;
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);
        //popup.setTouchable(true);
        popup.setBackgroundDrawable(null);
        popup.setOutsideTouchable(false);
        // Displaying the popup at the specified location
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x , p.y );
        // Getting a reference to Close button, and close the popup when clicked.
        Button btnOk = (Button) layout.findViewById(R.id.btnOkPin);
        final EditText txtPin =(EditText) layout.findViewById(R.id.textPin);
        final Point p1=p;
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                if(txtPin.getText()!=null && txtPin.getText().toString().equalsIgnoreCase(settings.getString("PIN","none")))
                {

                    popup.dismiss();
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid PIN.. try again!!",
                            Toast.LENGTH_LONG).show();
                    popup.dismiss();
                    showPopup(HomeActivity.this, p1);
                }

            }
        });
        Button btnCancel = (Button) layout.findViewById(R.id.btnCancelPin);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popup.dismiss();

            }
        });
    }
    public void onTimeBased(View view)
    {
        Intent myIntent = new Intent(this, ContactsActivity.class);
        myIntent.putExtra("flag", "TimeBased");
        HomeActivity.this.startActivity(myIntent);
    }
    public void onAreaBased(View view)
    {
        Intent myIntent = new Intent(this, ContactsActivity.class);
        myIntent.putExtra("flag", "AreaBased");
        HomeActivity.this.startActivity(myIntent);
    }
    public void editPreferences(SharedPreferences settings)
    {
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("PIN","1234");
        editor.putString("OwnerMob", "9096652973");
        editor.putFloat("Ploughing", 750);
        editor.putFloat("Cultivating", 500);
        editor.putFloat("Harrowing", 500);
        editor.putFloat("Puddling", 1200);
        editor.putFloat("Spraying", 150);
        editor.putFloat("Harvesting", 1000);
        editor.putFloat("Dry Rotary Tillage", 1000);
        editor.putFloat("Ploughing_time", 75);
        editor.putFloat("Cultivating_time", 50);
        editor.putFloat("Harrowing_time", 50);
        editor.putFloat("Puddling_time", 120);
        editor.putFloat("Spraying_time", 15);
        editor.putFloat("Harvesting_time", 100);
        editor.putFloat("Dry Rotary Tillage_time", 100);
        editor.commit();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
