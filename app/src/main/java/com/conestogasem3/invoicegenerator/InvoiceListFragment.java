package com.conestogasem3.invoicegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;
import com.conestogasem3.invoicegenerator.ModelClass.Customer_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Invoice_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Product_Model;
import com.conestogasem3.invoicegenerator.SqliteHelper.HelperClass;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.core.app.ActivityCompat;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceListFragment extends Fragment {

    private static final String TAG = "Invoice_Acitivty";
    //Invoice Widgets
    Button btnAddNewRecord;
    LinearLayout parentLayout;
    LinearLayout layoutDisplayPeople;
    TextView tvNoRecordsFound;

    HelperClass sQLiteHelper;
    private String rowID = null;

    //Invoice Customer
    Customer_Model customerModel;

    //Invoice Products
    Product_Model productModel;

    //Invoice model
    Invoice_Model invoiceModel;

    boolean doubleBackToExitPressedOnce = false;

    public InvoiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice_list, container, false);


        btnAddNewRecord = (Button) view.findViewById(R.id.btsaveinvoice);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayoutInvoice);
        layoutDisplayPeople = (LinearLayout) view.findViewById(R.id.layoutDisplayInvoice);
        tvNoRecordsFound = (TextView) view.findViewById(R.id.tvNoRecordsFound);

        sQLiteHelper = new HelperClass(getActivity());
        bindWidgetsWithEvent();

        boolean GRANTED = isStoragePermissionGranted();

        if (GRANTED) {
            displayAllRecords();
        } else {
            isStoragePermissionGranted();
        }


        return view;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> productidlist = new ArrayList<>();
            String date = data.getStringExtra(Constants.INDATE);
            String invoice_number = data.getStringExtra("invoicenumber");
            String customer = data.getStringExtra(Constants.INCUSTOMER);
            productidlist = data.getStringArrayListExtra(Constants.INPRODUCT);
            Log.i("idlistproduct", String.valueOf(productidlist));

//            //for QUERY use in database(format surrounded by single quote)
//            String ProductidlistQuote = productidlist.toString().replace("[", "'").replace("]", "'")
//                    .replace(", ", "','");

            String ProductidlistQuote = productidlist.toString().replace("[", "").replace("]", "")
                    .replace(", ", ",");

            Invoice_Model contact = new Invoice_Model();
            contact.setInvoiceDate(date);
            contact.setInvoiceProductID(ProductidlistQuote);
            contact.setInvoiceCustomerID(customer);

            if (requestCode == Constants.ADD_RECORD) {
                sQLiteHelper.insertInvoice(contact);
            } else if (requestCode == Constants.UPDATE_RECORD) {
                contact.setInvoiceID(rowID);
                sQLiteHelper.updateInvoiceRecord(contact);
            }
            displayAllRecords();
        }
    }


    private void bindWidgetsWithEvent() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRecord();
            }
        });
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        LinearLayout inflateParentView;
        ArrayList<Invoice_Model> invoices = sQLiteHelper.getAllInvoiceRecords();

        if (invoices.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);

            for (int i = 0; i < invoices.size(); i++) {
                invoiceModel = invoices.get(i);

                final Holder holder = new Holder();
                final View view = LayoutInflater.from(getActivity()).inflate(R.layout.invoicelistitem, null);
                inflateParentView = (LinearLayout) view.findViewById(R.id.llinvoice);
                holder.invoicenum = (TextView) view.findViewById(R.id.invoicenum);
                holder.invoicedate = (TextView) view.findViewById(R.id.invoicedate);
//                holder.invoiceproductid = (TextView) view.findViewById(R.id.productid);
                holder.invoicecustomerid = (TextView) view.findViewById(R.id.invoicecustomerid);
                holder.deleteinvoice = (ImageView) view.findViewById(R.id.deleteinvoice);
                holder.Viewinvoice = (ImageView) view.findViewById(R.id.Viewinvoice);

                view.setTag(invoiceModel.getInvoiceID());

                holder.invoicenumber = invoiceModel.getInvoiceID();
                holder.date = invoiceModel.getInvoiceDate();
                holder.customerid = invoiceModel.getInvoiceCustomerID();
                holder.productid = invoiceModel.getInvoiceProductID();

                //get Customer from customerId
                if (holder.customerid == null) {
                    holder.customerid = "0";
                }
                customerModel = sQLiteHelper.getcustomerunit(holder.customerid);
                Log.i("buyerDetails", String.valueOf(customerModel));

                holder.invoicenum.setText("#" + holder.invoicenumber);
                holder.invoicedate.setText(holder.date);
                holder.invoicecustomerid.setText(customerModel.getCustomer_Name());
//                holder.invoiceproductid.setText(holder.productid);

                inflateParentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowID = view.getTag().toString();
                        onUpdateRecord(holder.invoicenumber, holder.date, holder.customerid, holder.productid);
                    }
                });

                final Invoice_Model finalContactModel = invoiceModel;
                holder.Viewinvoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), GenerateInvoicePdf.class);
                        intent.putExtra("viewclicked", 1);
                        intent.putExtra("invoicemodel", (Serializable) finalContactModel);
                        startActivity(intent);
                    }
                });

                holder.deleteinvoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(getActivity());
                        deleteDialogOk.setTitle("Delete Contact?");
                        deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //sQLiteHelper.deleteRecord(view.getTag().toString());
                                        Invoice_Model contact = new Invoice_Model();
                                        contact.setInvoiceID(view.getTag().toString());
                                        sQLiteHelper.deleteInvoiceRecord(contact);
                                        displayAllRecords();
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
                parentLayout.addView(view);
            }
        } else {
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }

    private void onUpdateRecord(String invoicenumber, String date, String customerid, String productid) {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(Constants.IDINVOICE, invoicenumber);
        intent.putExtra(Constants.INDATE, date);
        intent.putExtra(Constants.INCUSTOMER, customerid);
        intent.putExtra(Constants.INPRODUCT, productid);
        intent.putExtra(Constants.DML_TYPE, Constants.UPDATE);
        startActivityForResult(intent, Constants.UPDATE_RECORD);
    }

    private void onAddRecord() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(Constants.DML_TYPE, Constants.INSERT);
        startActivityForResult(intent, Constants.ADD_RECORD);
    }


    private class Holder {
        TextView invoicenum, invoicedate, invoicecustomerid, invoiceproductid;
        String invoicenumber, date, customerid, productid;
        ImageView deleteinvoice, Viewinvoice;
    }


}
