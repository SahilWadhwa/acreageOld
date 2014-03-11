package com.deere.acreage;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class AcreageOptionActivity extends Activity {

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acreage_option);
        flag = getIntent().getStringExtra("flag");

    }

    public void walkOnBoundary(View view) {
        Intent intent;
//        if (flag.equalsIgnoreCase("TimeBased")) {
//            intent = new Intent(AcreageOptionActivity.this, TimeBasedActivity.class);
//        } else {
        intent = new Intent(AcreageOptionActivity.this, CalculateAreaActivity.class);
        intent.putExtra("flag", "AreaBased");
//        }
        startActivity(intent);

    }


    public void markOnMap(View view) {
        Intent intent;
        intent = new Intent(AcreageOptionActivity.this, MarkOnMaps.class);
        intent.putExtra("flag", "AreaBased");
        startActivity(intent);

    }

    public void timeBased(View view) {
        Intent intent = new Intent(AcreageOptionActivity.this, TimeBasedActivity.class);
        intent.putExtra("flag", "TimeBased");
        startActivity(intent);

    }
}
