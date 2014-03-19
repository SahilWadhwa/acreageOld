package com.deere.acreage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;


public class ShowAreaActivity extends Activity {
	double area;
	double areaAcre;
    public static final String PREFS_NAME = "GAANDHIPrefs";

	String hours;
	String minutes;
	String seconds;
	String startTime;
	String endTime;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showareascreen);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	  
		final TextView txtArea = (TextView) findViewById(R.id.TextAreaViewArea);
		area = (double) Double.parseDouble(getIntent().getStringExtra(
                "Area"));
		areaAcre = area / 4046.86;
		DecimalFormat df = new DecimalFormat("####0.00");
		txtArea.setText("Area (in Acres): " + df.format(areaAcre)
				+ "\nArea (in Sq. Meters): " + area);
		
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
	


	public void onAccept(View view)
	{
		
			Intent intent = new Intent(ShowAreaActivity.this,
					DetailsActivity.class);
			Bundle sendBundle = new Bundle();
		sendBundle.putDouble("Area", area);
		sendBundle.putDouble("AcreArea", areaAcre);
		sendBundle.putString("Flag", "AreaBased");
		intent.putExtras(sendBundle);
		startActivity(intent);
	}
		
	
	public void onReject(View view)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 		// set title
			alertDialogBuilder.setTitle("Warning!");
 		// set dialog message
			alertDialogBuilder
				.setMessage("Do you really want to reject?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						Intent intent = new Intent(ShowAreaActivity.this, ContactsActivity.class);
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
