
package com.deere.acreage;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.util.SettingsHelper;
import com.zebra.util.UIHelper;

import java.io.File;

public class ImagePrintActivity extends Activity {

    private EditText macAddressEditText;
  
    private EditText printStoragePath;
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
   
    private static final String PREFS_NAME = "GAANDHIPrefs";
    private UIHelper helper = new UIHelper(this);

    private static File file =  new File(Environment.getExternalStorageDirectory() + "/gaandhireceipt.png");
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       extras = getIntent().getExtras();     
        
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null) {
            if(adapter.getState() == BluetoothAdapter.STATE_ON) {
               // adapter.disable();
            } else if (adapter.getState() == BluetoothAdapter.STATE_OFF){
                adapter.enable();
            } else {
                //State.INTERMEDIATE_STATE;
            } 
         //   adapter.startDiscovery();
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
            startActivity(discoverableIntent);
        }
        setContentView(R.layout.image_print_demo);
   

        macAddressEditText = (EditText) this.findViewById(R.id.macInput);
      //  String mac = settings.getString(bluetoothAddressKey, "");
        macAddressEditText.setText("00:22:58:39:C7:78");

    }

    private String getMacAddressFieldText() {
        return macAddressEditText.getText().toString();
    }
 public void onConfirm(View view)
 {
	
			SmsManager smsManager = SmsManager.getDefault();
			/*PendingIntent pi = PendingIntent.getActivity(this, 0,
		            new Intent(this, ReceiptActivity.class), 0);  
			Bundle sendBundle = new Bundle();
			sendBundle.putDouble("area", area);
			sendBundle.putDouble("areaAcre", areaAcre);
			sendBundle.putString("operation", listener.getSelection());
			sendBundle.putDouble("rate", rate);
			sendBundle.putDouble("total", totalCost);
			pi.putExtras(sendBundle);*/

	       double area= extras.getDouble("area");
	        double areaAcre=extras.getDouble("areaAcre");
			String op= extras.getString("operation");
			double rate=extras.getDouble("rate");
			double total=extras.getDouble("total");
			String flag = extras.getString("flag");
			String time = extras.getString("time");
			 String startTime = extras.getString("start time");
			 String endTime = extras.getString("end time");
			 
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			String ownerNumber=settings.getString("OwnerMob", "none");
			String custName=settings.getString("CustName", "none");
			String custNumber=settings.getString("CustNumber", "none");
			 try {	
			if(custNumber!=null || custName!="")
			{
				if(flag.equalsIgnoreCase("TimeBased"))
					smsManager.sendTextMessage(custNumber, null, "hi "+custName+"\nOpeartion Done:"+op+"\nTotal Time:"+time+"\nStart Time: "+startTime+"\n End Time: "+endTime+"\nTotal Cost:"+total+"\n   from GAANDHI", null, null);
				else if(flag.equalsIgnoreCase("AreaBased"))
					smsManager.sendTextMessage(custNumber, null, "hi "+custName+"\nOpeartion Done:"+op+"\nTotal Area(in acre):"+areaAcre+"\nTotal Area(in sq.met.)"+area+"\nTotal Cost:"+total+"\n   from GAANDHI", null, null);
			}
			 }
			 catch (Exception e) {
					Toast.makeText(getApplicationContext(), "SMS to customer faild, please try again later!",
                            Toast.LENGTH_LONG).show();
					e.printStackTrace();
				  }
			 try
			 {
				if(ownerNumber!=null || ownerNumber!="")
				{
					if(flag.equalsIgnoreCase("TimeBased"))
						smsManager.sendTextMessage(ownerNumber, null, "hi "+custName+"\nOpeartion Done:"+op+"\nTotal Time:"+time+"\nStart Time: "+startTime+"\n End Time: "+endTime+"\nTotal Cost:"+total+"\n   from GAANDHI", null, null);
					else if(flag.equalsIgnoreCase("AreaBased"))
						smsManager.sendTextMessage(ownerNumber, null, "hi "+custName+"\nOpeartion Done:"+op+"\nTotal Area(in acre):"+areaAcre+"\nTotal Area(in sq.met.)"+area+"\nTotal Cost:"+total+"\n   from GAANDHI", null, null);
			
				}
		  } catch (Exception e) {
			Toast.makeText(getApplicationContext(), "SMS to owner faild, please try again later!",
                    Toast.LENGTH_LONG).show();
			e.printStackTrace();
		  }

//	 System.out.println("************file..........."+file.getAbsolutePath());
	 Button btnPrint = (Button) findViewById(R.id.button_confirm);
	 btnPrint.setEnabled(false);	 
	 printPhotoFromExternal(BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory() + "/gaandhireceipt.png").getAbsolutePath()));
	// file.delete();
	 
	 Intent myIntent = new Intent(ImagePrintActivity.this, ContactsActivity.class);
	 ImagePrintActivity.this.startActivity(myIntent);
 }
 
    private void printPhotoFromExternal(final Bitmap bitmap) {
    	
    	
        new Thread(new Runnable() {
            public void run() {
                try {
                	
                    getAndSaveSettings();
                    Looper.prepare();
                    helper.showLoadingDialog("Sending image to printer");
                    ZebraPrinterConnection connection = getZebraPrinterConn();
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                    printer.getGraphicsUtil().printImage(bitmap, 0, 0, 550, 682, false);

                    connection.close();

                    if (file != null) {
                        file.delete();
                        file = null;
                    }
                } catch (ZebraPrinterConnectionException e) {
                	helper.showErrorDialogOnGuiThread(e.getMessage());
                } catch (ZebraPrinterLanguageUnknownException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                }  finally {
                //   bitmap.recycle();
                    helper.dismissLoadingDialog();
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }

    private ZebraPrinterConnection getZebraPrinterConn() {
        return new BluetoothPrinterConnection(getMacAddressFieldText());
    }

    private void getAndSaveSettings() {
        SettingsHelper.saveBluetoothAddress(ImagePrintActivity.this, getMacAddressFieldText());
        
    }
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
}
}
