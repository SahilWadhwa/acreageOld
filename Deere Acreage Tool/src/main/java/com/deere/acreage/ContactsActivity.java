package com.deere.acreage;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import android.provider.ContactsContract.CommonDataKinds.Email;

public class ContactsActivity  extends Activity{
    private static final int CONTACT_PICKER_RESULT = 1001;
    public static final String PREFS_NAME = "GAANDHIPrefs";
    EditText txtName;
    EditText txtNumber;
    String flag;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        flag = getIntent().getStringExtra("flag");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                .setMessage("Do you really want to cancel this operation?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(txtName!=null)
                            txtName.setText("");
                        if(txtNumber!=null)
                            txtNumber.setText("");
                        Intent intent = new Intent(ContactsActivity.this, HomeActivity.class);
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
        if(flag.equalsIgnoreCase("TimeBased"))
        {
            intent = new Intent(ContactsActivity.this, TimeBasedActivity.class);
        }
        else
        {
            intent = new Intent(ContactsActivity.this, CalculateAreaActivity.class);
        }
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }

}
