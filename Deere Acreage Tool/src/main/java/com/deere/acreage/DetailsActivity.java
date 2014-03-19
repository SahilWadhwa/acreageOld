package com.deere.acreage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.DecimalFormat;

public class DetailsActivity extends Activity {
	double area;
	double areaAcre;
	SpinnerItemClickListener listener=null;
	public static final String PREFS_NAME = "GAANDHIPrefs";
	Spinner spinner;
	String flag;
	String hours;
	String minutes;
	String seconds;
	String startTime;
	String endTime;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailscreen);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    spinner = (Spinner) findViewById(R.id.parameters_spinner);

		final TextView txtArea = (TextView) findViewById(R.id.TextViewArea);
		
		flag = getIntent().getStringExtra("Flag");

		if(flag.equalsIgnoreCase("AreaBased"))
		{
		area = getIntent().getDoubleExtra("Area", 0);
		areaAcre = getIntent().getDoubleExtra("AcreArea", 0);
		DecimalFormat df = new DecimalFormat("####0.00");
		txtArea.setText("Area (in Acres): " + df.format(areaAcre)
				+ "\nArea (in Sq. Meters): " + area);
		}
		else if (flag.equalsIgnoreCase("TimeBased"))
		{
			startTime = getIntent().getStringExtra("start time");
			endTime = getIntent().getStringExtra("end time");
			hours = getIntent().getStringExtra("hours");
			minutes = getIntent().getStringExtra("minutes");
			seconds = getIntent().getStringExtra("seconds");
			txtArea.setText("Total Time:  " +hours+":"+minutes+":"+seconds );
		}
		// txtArea.setText("Area in Acres: "+df.format(areaAcre));
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.operation_array,
                android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		listener = new SpinnerItemClickListener();
        spinner.setOnItemSelectedListener(listener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void onSendSms(View view)
	{
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage("9096652973", null, "hi from GAANDHI...", null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
		  } catch (Exception e) {
			Toast.makeText(getApplicationContext(), "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
			e.printStackTrace();
		  }
	}

	
	public class SpinnerItemClickListener implements OnItemSelectedListener {

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
	
	public void populate(Spinner spinner)
	{
		String selection = spinner.getSelectedItem().toString();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		EditText txtOpRate = (EditText) findViewById(R.id.textOperationcost);
		if(flag.equalsIgnoreCase("AreaBased"))
			txtOpRate.setText(""+(settings.getFloat(selection, 0)));
		else if (flag.equalsIgnoreCase("TimeBased"))
			txtOpRate.setText(""+(settings.getFloat(selection+"_time", 0)));
	}
	
	public void onAccept(View view)
	{
		EditText txtRate = (EditText)findViewById(R.id.textOperationcost);
	
		try
		{
			
			float r = (float) Float.parseFloat(txtRate.getText().toString());
			Intent intent = new Intent(DetailsActivity.this,
					ReceiptActivity.class);
			Bundle sendBundle = new Bundle();
			double rate = (double)r;
			double totalCost=0;
		if(flag.equalsIgnoreCase("AreaBased"))
		{
			totalCost= areaAcre*rate;
			sendBundle.putDouble("area", area);
			sendBundle.putDouble("areaAcre", areaAcre);
			
		}
		else if (flag.equalsIgnoreCase("TimeBased"))
		{
			totalCost = (Integer.parseInt(hours)* r)+(Integer.parseInt(minutes)*(r/60));
			sendBundle.putString("time", hours+":"+minutes+":"+seconds);
			sendBundle.putString("start time", startTime);
			sendBundle.putString("end time", endTime);
		}
		sendBundle.putDouble("total", totalCost);
		sendBundle.putString("operation", listener.getSelection());
		sendBundle.putDouble("rate", rate);
		sendBundle.putString("flag", flag);
		intent.putExtras(sendBundle);
		startActivity(intent);
		}
		catch(Exception a)
		{
			a.printStackTrace();
			System.out.println("*************************enter rate properly......");
		}
	}
	public void onReject(View view)
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
				
						Intent intent = new Intent(DetailsActivity.this, ContactsActivity.class);
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
	 @Override
		public void onBackPressed() {
			
		}
	 
}