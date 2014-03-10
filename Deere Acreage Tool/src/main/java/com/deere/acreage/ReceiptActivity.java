package com.deere.acreage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

public class ReceiptActivity extends Activity {
	 double area;
	 double areaAcre;
	 String op;
	 double rate;
	 double total;
	 String flag;
	 String time;
	 String startTime;
	 String endTime;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.receiptlayout);
	        Bundle extras = getIntent().getExtras();
	        
	        area= extras.getDouble("area");
	        areaAcre=extras.getDouble("areaAcre");
			op= extras.getString("operation");
			rate=extras.getDouble("rate");
			total=extras.getDouble("total");
			flag = extras.getString("flag");
			time = extras.getString("time");
			
			String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			
			Options options = new BitmapFactory.Options();
			options.inScaled = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bmap= null;
			if(flag.equalsIgnoreCase("AreaBased"))
			{
				bmap = BitmapFactory.decodeResource(getResources(), R.drawable.receipt_hindi_area2, options).copy(Bitmap.Config.ARGB_8888, true);
			}
			else if(flag.equalsIgnoreCase("TimeBased"))
			{
				bmap = BitmapFactory.decodeResource(getResources(), R.drawable.receipt_hindi_time, options).copy(Bitmap.Config.ARGB_8888, true);
				startTime = getIntent().getStringExtra("start time");
				endTime = getIntent().getStringExtra("end time");
			}
	        Canvas g = new Canvas(bmap);
	        Paint p = new Paint();
	        p.setColor(Color.BLACK);
	        p.setAntiAlias(true);
	        p.setTextSize(18);
	        
	        if(flag.equalsIgnoreCase("AreaBased"))
			{
	        	g.drawText(mydate, 100, 130, p);
		        g.drawText("112", 110, 180, p); 
		        g.drawText(doubleToStr(area), 190,230 , p);
		        g.drawText(doubleToStr(areaAcre), 180,284, p);
		        g.drawText(op, 100,336 , p);
		        g.drawText(doubleToStr(rate), 100,386 , p);
		        g.drawText(doubleToStr(total), 100, 435, p); 
			}
	        else if(flag.equalsIgnoreCase("TimeBased"))
			{
	        	g.drawText(mydate, 100, 110, p);
		        g.drawText("112", 110, 170, p); 
	        	 	g.drawText(time, 130,225 , p);
			        g.drawText(op, 100,280 , p);
			        g.drawText(doubleToStr(rate), 100,335 , p);
			        g.drawText(doubleToStr(total), 100, 380, p); 
			}
	        System.out.println("*************image......................."+bmap);
	        
	        ImageView imgView =(ImageView)findViewById(R.id.receiptView);
	        imgView.setImageBitmap(bmap);

	        FileOutputStream fos = null;
			 System.out.println("******************!!!!!!!!!!!!!!!!!!"+ Environment.getExternalStorageState());

	        try {

	                fos = new FileOutputStream( Environment.getExternalStorageDirectory() + "/gaandhireceipt.png" );
	

	                if ( fos != null )
	                {
	                	    bmap.compress(Bitmap.CompressFormat.JPEG, 90, fos );
	                		fos.flush();
	                        fos.close();
	                        System.out.println("************************file written*********************");
	                        
	                }
	                else
	                {
	                	System.out.println("************************file not written*********************");
	                }

	        } catch( Exception e )
	                {
	        	System.out.println("************************fos exception*********************"+e.getMessage());
	                }
	        
	        
	        
	       
	    }
	  
	  public void onPrint(View view)
	  {
		 
		  	 Button btnPrint = (Button) findViewById(R.id.button_print);
			 btnPrint.setEnabled(false);	 
			 Intent intent = new Intent(ReceiptActivity.this, ImagePrintActivity.class);
			 Bundle sendBundle = new Bundle();
	
				if(flag.equalsIgnoreCase("AreaBased"))
				{

					sendBundle.putDouble("area", area);
					sendBundle.putDouble("areaAcre", areaAcre);
					
				}
				else if (flag.equalsIgnoreCase("TimeBased"))
				{
			
					sendBundle.putString("time", time);
				}
				System.out.println("************************************************Start time::::: "+startTime);
				System.out.println("************************************************End time::::: "+endTime);
				sendBundle.putDouble("total", total);
				sendBundle.putString("operation", op);
				sendBundle.putDouble("rate", rate);
				sendBundle.putString("start time", startTime);
				sendBundle.putString("end time", endTime);
				sendBundle.putString("flag", flag);
				intent.putExtras(sendBundle);
			  startActivity(intent);
	  } 
	  @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	  public String doubleToStr(double inValue){
		  DecimalFormat threeDec = new DecimalFormat("0.000");
		  String shortString = (threeDec.format(inValue));
		  return shortString;
		  }


}
