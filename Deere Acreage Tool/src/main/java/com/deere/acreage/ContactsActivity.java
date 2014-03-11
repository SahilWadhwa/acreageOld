package com.deere.acreage;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;

import android.view.*;
import android.widget.*;

import android.provider.ContactsContract.CommonDataKinds.Email;

public class ContactsActivity  extends Activity{
    private static final int CONTACT_PICKER_RESULT = 1001;
    EditText txtName;
    EditText txtNumber;
    //String flag;
    final Context context = this;
    public static final String PREFS_NAME = "GAANDHIPrefs";
    Point p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        float check= settings.getFloat("Ploughing",0);
        if(check==0)
        {
            editPreferences(settings);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //flag = getIntent().getStringExtra("flag");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Button btn_settings = (Button) findViewById(R.id.btnSettings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Open popup window
                if (p != null)
                    showPopup(ContactsActivity.this, p);
            }
        });
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
                    Intent intent = new Intent(ContactsActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid PIN.. try again!!",
                            Toast.LENGTH_LONG).show();
                    popup.dismiss();
                    showPopup(ContactsActivity.this, p1);
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    public void onChooseContact(View view)
    {
			/*Intent intent = new Intent(Intent.ACTION_PICK, Contacts.Phones.CONTENT_URI);
			startActivityForResult(intent, CONTACT_PICKER_RESULT);   */
        Intent intent = new Intent(Intent.ACTION_PICK,Contacts.Phones.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results

                    txtName = (EditText) findViewById(R.id.textCustName);
                    txtNumber = (EditText) findViewById(R.id.textCustNumber);


                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            txtNumber.setText(cNumber);
                            System.out.println("***********************************number is:"+cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        txtName.setText(name);
                        System.out.println("********************************************************number is:"+name);
                    }
                    break;
            }
        } else {

            Toast.makeText(getApplicationContext(), "Error while choosing contact... try again!!",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void onCancelContact(View view)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Warning!");
        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to exit from this operation ?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(txtName!=null)
                            txtName.setText("");
                        if(txtNumber!=null)
                            txtNumber.setText("");
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void onOkContact(View view)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Editor editor=settings.edit();
        String custNumber =((EditText)findViewById(R.id.textCustNumber)).getText().toString();
        String custName =((EditText)findViewById(R.id.textCustName)).getText().toString();
        editor.putString("CustName",custName);
        editor.putString("CustNumber",custNumber);
        editor.commit();
        Intent intent;

        intent = new Intent(ContactsActivity.this, AcreageOptionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       onCancelContact(null);

    }

}
