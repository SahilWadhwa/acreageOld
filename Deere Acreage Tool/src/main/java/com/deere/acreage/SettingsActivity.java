package com.deere.acreage;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsActivity extends Activity {
    SpinnerItemClickListener listener=null;
    public static final String PREFS_NAME = "GAANDHIPrefs";
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spinner = (Spinner) findViewById(R.id.spinnerOperationSettings);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.operation_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        listener = new SpinnerItemClickListener();
        spinner.setOnItemSelectedListener(listener);



        populate( spinner);
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

    public void populate(Spinner spinner)
    {
        String selection = spinner.getSelectedItem().toString();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        EditText txtOwnerMob = (EditText) findViewById(R.id.textOwnerNumber);
        EditText txtOpRate = (EditText) findViewById(R.id.textAcreRate);
        EditText txtPin = (EditText) findViewById(R.id.textPin);
        EditText txtOpRateHr = (EditText) findViewById(R.id.textHourRate);

        txtOwnerMob.setText(settings.getString("OwnerMob", "None"));
        txtOpRate.setText(""+(settings.getFloat(selection, 0)));
        txtOpRateHr.setText(""+(settings.getFloat(selection+"_time", 0)));
        txtPin.setText(settings.getString("PIN","none"));
    }
    class SpinnerItemClickListener implements OnItemSelectedListener {

        String selection;
        int pos;
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            selection=parent.getItemAtPosition(pos).toString();
            this.pos=pos;
            System.out.println("******************************Inside selector..........."+pos+"   "+selection);
            populate(spinner);
        }

        public void onNothingSelected(AdapterView<?> arg0) {


        }

        public String getSelection()
        {
            return selection;
        }
        public int getPos()
        {
            System.out.println("positions..........................."+pos);
            return pos;
        }

    }
    public void onOkSettings(View view)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        EditText txtOwnerMob = (EditText) findViewById(R.id.textOwnerNumber);
        EditText txtOpRate = (EditText) findViewById(R.id.textAcreRate);
        EditText txtOpRateHr = (EditText) findViewById(R.id.textHourRate);
        EditText txtPin = (EditText) findViewById(R.id.textPin);

        Editor editor = settings.edit();
        editor.putString("OwnerMob", txtOwnerMob.getText().toString());
        editor.putFloat(spinner.getSelectedItem().toString(), Float.parseFloat(txtOpRate.getText().toString()));
        editor.putFloat(spinner.getSelectedItem().toString()+"_time", Float.parseFloat(txtOpRateHr.getText().toString()));
        editor.putString("PIN", txtPin.getText().toString());
        editor.commit();

        Toast.makeText(getApplicationContext(),"Changes Saved Successfully!",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
        startActivity(intent);
    }
    public void onCancelSettings(View view)
    {
        Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }
}
