package com.conestogasem3.invoicegenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;
import com.conestogasem3.invoicegenerator.ModelClass.Customer_Model;
import com.conestogasem3.invoicegenerator.SqliteHelper.HelperClass;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class ListOfBuyer extends AppCompatActivity {

    Button btnAddNewRecord;
    HelperClass sQLiteHelper;

    LinearLayout parentCustomer;
    LinearLayout layoutDisplayCustomer;
    TextView tvNoRecordsFound;
    private String rowID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_buyer);

        getAllWidgets();
        sQLiteHelper = new HelperClass(this);
        bindWidgetsWithEvent();
        displayAllCustomerRecords();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            String customername = data.getStringExtra(Constants.CUSTOMERNAME);
            String customeremail = data.getStringExtra(Constants.EMAIL);
            String customergstin = data.getStringExtra(Constants.GST);
            String customeraddress = data.getStringExtra(Constants.ADDRESS);
            String customercity = data.getStringExtra(Constants.CITY);
            String customerstate = data.getStringExtra(Constants.STATE);
            String customerpin = data.getStringExtra(Constants.PIN);


            Customer_Model customerModel = new Customer_Model();
            customerModel.setCustomer_Name(customername);
            customerModel.setEmail(customeremail);
            customerModel.setGst(customergstin);
            customerModel.setAddress(customeraddress);
            customerModel.setCity(customercity);
            customerModel.setState(customerstate);
            customerModel.setPincode(customerpin);

            if (requestCode == Constants.ADD_CUSTOMER) {
                sQLiteHelper.insertCustomer(customerModel);
            } else if (requestCode == Constants.UPDATE_CUSTOMER) {
                customerModel.setIDcustomer(rowID);
                sQLiteHelper.updateCustomerRecord(customerModel);
            }

            displayAllCustomerRecords();
        }
    }

    private void getAllWidgets() {
        btnAddNewRecord = (Button) findViewById(R.id.btsavebuyyer);

        parentCustomer = (LinearLayout) findViewById(R.id.parentLayout);
        layoutDisplayCustomer = (LinearLayout) findViewById(R.id.layoutDisplayCustomer);

        tvNoRecordsFound = (TextView) findViewById(R.id.tvNoRecordsFound);
    }

    private void bindWidgetsWithEvent() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddCustomer();
            }
        });
    }

    private void onAddCustomer() {
        Intent intent = new Intent(this, BuyerActivity.class);
        intent.putExtra(Constants.DML_TYPE, Constants.INSERT);
        startActivityForResult(intent, Constants.ADD_CUSTOMER);
    }

    private void onUpdateCustomerRecord(String customername, String customeremail, String customergstin, String customeraddress,
                                        String customercity, String customerstate, String customerpin) {


        Intent intent = new Intent(this, BuyerActivity.class);
        intent.putExtra(Constants.CUSTOMERNAME, customername);
        intent.putExtra(Constants.EMAIL, customeremail);
        intent.putExtra(Constants.GST, customergstin);
        intent.putExtra(Constants.ADDRESS, customeraddress);
        intent.putExtra(Constants.CITY, customercity);
        intent.putExtra(Constants.STATE, customerstate);
        intent.putExtra(Constants.PIN, customerpin);

        intent.putExtra(Constants.DML_TYPE, Constants.UPDATE);
        startActivityForResult(intent, Constants.UPDATE_CUSTOMER);
    }

    private void displayAllCustomerRecords() {

        parentCustomer.removeAllViews();
        final ArrayList<Customer_Model> customers = sQLiteHelper.getAllCustomerRecords();


        if (customers.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            Customer_Model customerModel;
            for (int i = 0; i < customers.size(); i++) {

                customerModel = customers.get(i);

                final Holder holder = new Holder();
                final View customerview = LayoutInflater.from(this).inflate(R.layout.inflatebuyer, null);

                layoutDisplayCustomer = (LinearLayout) customerview.findViewById(R.id.llbuyer);

                holder.tvcname = (TextView) customerview.findViewById(R.id.buyername);
                holder.tvcadderess = (TextView) customerview.findViewById(R.id.buyeraddress);
                holder.tvccity = (TextView) customerview.findViewById(R.id.buyercity);
                holder.tvcpin = (TextView) customerview.findViewById(R.id.buyerpincode);


                holder.editcustomer = (ImageView) customerview.findViewById(R.id.iveditcustomer);
                holder.deletecustomer = (ImageView) customerview.findViewById(R.id.ivdeletecustomer);


                customerview.setTag(customerModel.getIDcustomer());


                holder.customername = customerModel.getCustomer_Name();
                holder.customeremail = customerModel.getEmail();
                holder.customergstin = customerModel.getGst();
                holder.customeraddress = customerModel.getAddress();
                holder.customercity = customerModel.getCity();
                holder.customerstate = customerModel.getState();
                holder.customerpin = customerModel.getPincode();

                holder.tvcname.setText(holder.customername);
                holder.tvcadderess.setText(holder.customeraddress);
                holder.tvccity.setText(holder.customercity + "-");
                holder.tvcpin.setText(holder.customerpin);


                final int position = i;

                final Customer_Model finalCustomerModel = customerModel;

                layoutDisplayCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ListOfBuyer.this, MainActivity.class);
//                        intent.putExtra("listofcustomer", customers);
//                        Log.i("itemoflist", String.valueOf(customers));
//                        String cp = String.valueOf(position);
//                        intent.putExtra("customer_position", cp);
                        String cid = finalCustomerModel.getIDcustomer();
                        intent.putExtra("customerid", cid);
                        intent.putExtra("customermodel", finalCustomerModel);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });


                holder.editcustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        rowID = customerview.getTag().toString();
                        onUpdateCustomerRecord(holder.customername, holder.customeremail, holder.customergstin, holder.customeraddress,
                                holder.customercity, holder.customerstate, holder.customerpin);
                    }
                });

                holder.deletecustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(ListOfBuyer.this);
                        deleteDialogOk.setTitle("Delete Product?");
                        deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //sQLiteHelper.deleteRecord(view.getTag().toString());
                                        Customer_Model customer_model = new Customer_Model();
                                        customer_model.setIDcustomer(customerview.getTag().toString());
                                        sQLiteHelper.deleteCustomerRecord(customer_model);
                                        displayAllCustomerRecords();
                                    }
                                }
                        );
                        deleteDialogOk.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        deleteDialogOk.show();
                    }
                });

                parentCustomer.addView(customerview);
            }
        } else {
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }

    static class Holder {
        String customername, customeremail, customergstin, customeraddress, customercity, customerstate, customerpin;
        TextView tvcname, tvcemail, tvcadderess, tvccity, tvcpin, tvcgstin, tvcstate;
        ImageView editcustomer, deletecustomer;
    }
}