package com.conestogasem3.invoicegenerator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Supplier_Details extends Activity {

    Button savesup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier__details);

        savesup = (Button) findViewById(R.id.btsavesupp);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Name", "");
        String email = preferences.getString("Email", "");
        String gst = preferences.getString("Gst", "");
        String address = preferences.getString("Address", "");
        String city = preferences.getString("City", "");
        String state = preferences.getString("State", "");
        String pin = preferences.getString("Pin", "");

        if (!name.equalsIgnoreCase("")) {
            /* Edit the value here*/
            ((EditText) findViewById(R.id.etfirmname)).setText(name);
            ((EditText) findViewById(R.id.etemail)).setText(email);
            ((EditText) findViewById(R.id.etgstin)).setText(gst);
            ((EditText) findViewById(R.id.etaddress)).setText(address);
            ((EditText) findViewById(R.id.etcity)).setText(city);
            ((EditText) findViewById(R.id.etstate)).setText(state);
            ((EditText) findViewById(R.id.etpincode)).setText(pin);

            ((EditText) findViewById(R.id.etfirmname)).setSelection(((EditText) findViewById(R.id.etfirmname)).getText().length());
            ((EditText) findViewById(R.id.etemail)).setSelection(((EditText) findViewById(R.id.etemail)).getText().length());
            ((EditText) findViewById(R.id.etgstin)).setSelection(((EditText) findViewById(R.id.etgstin)).getText().length());
            ((EditText) findViewById(R.id.etaddress)).setSelection(((EditText) findViewById(R.id.etaddress)).getText().length());
            ((EditText) findViewById(R.id.etcity)).setSelection(((EditText) findViewById(R.id.etcity)).getText().length());
            ((EditText) findViewById(R.id.etstate)).setSelection(((EditText) findViewById(R.id.etstate)).getText().length());
            ((EditText) findViewById(R.id.etpincode)).setSelection(((EditText) findViewById(R.id.etpincode)).getText().length());

        }


        savesup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("sfirm", ((EditText) findViewById(R.id.etfirmname)).getText().toString());
                intent.putExtra("semail", ((EditText) findViewById(R.id.etemail)).getText().toString());
                intent.putExtra("sgst", ((EditText) findViewById(R.id.etgstin)).getText().toString());
                intent.putExtra("saddress", ((EditText) findViewById(R.id.etaddress)).getText().toString());
                intent.putExtra("scity", ((EditText) findViewById(R.id.etcity)).getText().toString());
                intent.putExtra("sstate", ((EditText) findViewById(R.id.etstate)).getText().toString());
                intent.putExtra("spin", ((EditText) findViewById(R.id.etpincode)).getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }
}
