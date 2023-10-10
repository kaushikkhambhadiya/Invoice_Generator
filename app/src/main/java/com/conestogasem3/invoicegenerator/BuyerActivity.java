package com.conestogasem3.invoicegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;

import androidx.appcompat.app.AppCompatActivity;


public class BuyerActivity extends AppCompatActivity {

    EditText Cname, Cemail, Cgstin, Caddress, Ccity, Cstate, Cpincode;
    Button savecustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        getAllWidgets();
        bindWidgetsWithEvent();
        checkForRequest();
    }

    private void bindWidgetsWithEvent() {
        savecustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
    }

    private void checkForRequest() {
        String request = getIntent().getExtras().get(Constants.DML_TYPE).toString();
        if (request.equals(Constants.UPDATE)) {
            savecustomer.setText(Constants.UPDATE);

            Cname.setText(getIntent().getExtras().get(Constants.CUSTOMERNAME).toString());
            Cemail.setText(getIntent().getExtras().get(Constants.EMAIL).toString());
            Cgstin.setText(getIntent().getExtras().get(Constants.GST).toString());
            Caddress.setText(getIntent().getExtras().get(Constants.ADDRESS).toString());
            Ccity.setText(getIntent().getExtras().get(Constants.CITY).toString());
            Cstate.setText(getIntent().getExtras().get(Constants.STATE).toString());
            Cpincode.setText(getIntent().getExtras().get(Constants.PIN).toString());

        } else {
            savecustomer.setText(Constants.INSERT);
        }
    }

    private void onButtonClick() {
        if (Cname.getText().toString().equals("") || Caddress.getText().toString().equals("")) {

            if (Cname.getText().toString().equals("")) {
                Cname.setError("Please Enter Item");
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
            } else if (Caddress.getText().toString().equals("")) {
                Caddress.setError("Please Enter Item");
                Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.CUSTOMERNAME, Cname.getText().toString());
            intent.putExtra(Constants.EMAIL, Cemail.getText().toString());
            intent.putExtra(Constants.GST, Cgstin.getText().toString());
            intent.putExtra(Constants.ADDRESS, Caddress.getText().toString());
            intent.putExtra(Constants.CITY, Ccity.getText().toString());
            intent.putExtra(Constants.STATE, Cstate.getText().toString());
            String st =  Cstate.getText().toString();
            Log.i("state",st);
            intent.putExtra(Constants.PIN, Cpincode.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void getAllWidgets() {
        Cname = (EditText) findViewById(R.id.etcompanyname);
        Cemail = (EditText) findViewById(R.id.etemail);
        Cgstin = (EditText) findViewById(R.id.etgstin);
        Caddress = (EditText) findViewById(R.id.etaddress);
        Ccity = (EditText) findViewById(R.id.etcompanycity);
        Cstate = (EditText) findViewById(R.id.etstate);
        Cpincode = (EditText) findViewById(R.id.etpincode);

        savecustomer = (Button) findViewById(R.id.btsavebuyyer);
    }
}
