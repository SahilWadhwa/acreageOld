package com.deere.acreage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeBasedActivity extends Activity {

	private long secs;
	private long mins;
	private long hrs;
	private String seconds;
	private String minutes;
	private String hours;
	private Handler mHandler = new Handler();
	private long startTime;
	private long startTimeStamp;
	private long endTimeStamp;
	private long elapsedTime;
	private final int REFRESH_RATE = 100;
	private boolean stopped = false;
	private SimpleDateFormat dateFormat;
	final Context context = this;
	
	final Runnable startTimer = new Runnable() {
		   public void run() {
			   elapsedTime = System.currentTimeMillis() - startTime;
			   updateTimer(elapsedTime);
			   mHandler.postDelayed(this,REFRESH_RATE);
			}
		};
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.timerscreen);
	dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss");
	hideStopButton();
}
private void showStopButton(){
    ((Button)findViewById(R.id.btnTimerStart)).setVisibility(View.GONE);
    ((Button)findViewById(R.id.btnTimerStop)).setVisibility(View.VISIBLE);
}

private void hideStopButton(){
    ((Button)findViewById(R.id.btnTimerStart)).setVisibility(View.VISIBLE);
    ((Button)findViewById(R.id.btnTimerStop)).setVisibility(View.GONE);
}

public void onStartTimer(View v)
{
	((Button)findViewById(R.id.btnOkTime)).setEnabled(true);
	showStopButton();
	showStopButton();
	if(stopped){
		startTime = System.currentTimeMillis() - elapsedTime;
	}
	else{
		startTime = System.currentTimeMillis();
		startTimeStamp = startTime;
	}
	mHandler.removeCallbacks(startTimer);
    mHandler.postDelayed(startTimer, 0);

}

public void onStopTimer(View v)
{
	hideStopButton();
	mHandler.removeCallbacks(startTimer);
	stopped = true;

}
private void updateTimer (float time)
{
	secs = (long)(time/1000);
	mins = (long)((time/1000)/60);
	hrs = (long)(((time/1000)/60)/60);

	/* Convert the seconds to String
	 * and format to ensure it has
	 * a leading zero when required
	 */
	secs = secs % 60;
	seconds= String.valueOf(secs);
	if(secs == 0){
		seconds = "00";
	}
	if(secs <10 && secs > 0){
		seconds = "0"+seconds;
	}

	/* Convert the minutes to String and format the String */

	mins = mins % 60;
	minutes= String.valueOf(mins);
	if(mins == 0){
		minutes = "00";
	}
	if(mins <10 && mins > 0){
		minutes = "0"+minutes;
	}

	/* Convert the hours to String and format the String */

	hours= String.valueOf(hrs);
	if(hrs == 0){
		hours = "00";
	}
	if(hrs <10 && hrs > 0){
		hours = "0"+hours;
	}
	((TextView)findViewById(R.id.txtViewTimer)).setText(hours + ":" + minutes + ":" + seconds);
	
}
public void onOkTime(View v) {
	endTimeStamp = System.currentTimeMillis();
	
	Intent myIntent = new Intent(TimeBasedActivity.this, DetailsActivity.class);
	myIntent.putExtra("start time", dateFormat.format(new Date(startTimeStamp)));
	myIntent.putExtra("end time", dateFormat.format(new Date(endTimeStamp)));
	 myIntent.putExtra("hours",hours);
	 myIntent.putExtra("seconds",seconds);
	 myIntent.putExtra("minutes",minutes);
	 myIntent.putExtra("Flag","TimeBased");
	 TimeBasedActivity.this.startActivity(myIntent);
}

public void onCancelTime(View v) {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle("Warning!");
		// set dialog message
		alertDialogBuilder
			.setMessage("Do you really want to cancel this operation?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
			
					Intent intent = new Intent(TimeBasedActivity.this, HomeActivity.class);
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
