package com.conestogasem3.invoicegenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;
import com.conestogasem3.invoicegenerator.ModelClass.Product_Model;
import com.conestogasem3.invoicegenerator.SqliteHelper.HelperClass;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class ListOfProduct extends AppCompatActivity {

    HelperClass sQLiteHelper;
    LinearLayout parentLayout;
    LinearLayout layoutDisplayProduct;
    private String rowID = null;
    TextView tvNoRecordsFound;
    Button btnAddNewRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_product);



        getAllWidgets();
        sQLiteHelper = new HelperClass(this);
        bindWidgetsWithEvent();
        displayAllRecords();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            String name = data.getStringExtra(Constants.NAME);
            String hsn = data.getStringExtra(Constants.HSN);
            String quantity = data.getStringExtra(Constants.QUANTITY);
            String unit = data.getStringExtra(Constants.UNIT);
            String unitprice = data.getStringExtra(Constants.UNITPRICE);
            String discount = data.getStringExtra(Constants.DISCOUNT);
            String cgst = data.getStringExtra(Constants.CGST);
            String sgst = data.getStringExtra(Constants.SGST);
            String igst = data.getStringExtra(Constants.IGST);
            String cess = data.getStringExtra(Constants.CESS);


            if (name == null) {
                name = "";
            }
            if (hsn == null) {
                hsn = "";
            }
            if (quantity == null) {
                quantity = "";
            }
            if (unit == null) {
                unit = "";
            }
            if (unitprice == null) {
                unitprice = "";
            }
            if (discount == null) {
                discount = "";
            }
            if (cgst == null) {
                cgst = "";
            }
            if (sgst == null) {
                sgst = "";
            }
            if (igst == null) {
                igst = "";
            }
            if (cess == null) {
                cess = "";
            }

            Product_Model contact = new Product_Model();
            contact.setProduct_name(name);
            contact.setHsn(hsn);
            contact.setQuantity(quantity);
            contact.setUnit(unit);
            contact.setUnit_price(unitprice);
            contact.setDisperc(discount);
            contact.setCgstper(cgst);
            contact.setSgstper(sgst);
            contact.setIgstper(igst);
            contact.setCessper(cess);


            if (requestCode == Constants.ADD_RECORD) {
                sQLiteHelper.insertRecord(contact);
            } else if (requestCode == Constants.UPDATE_RECORD) {
                contact.setID(rowID);
                sQLiteHelper.updateRecord(contact);
            }
            displayAllRecords();
        }

    }

    private void getAllWidgets() {

        //Add Product into LL
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        layoutDisplayProduct = (LinearLayout) findViewById(R.id.layoutDisplayProduct);
        tvNoRecordsFound = (TextView) findViewById(R.id.tvNoRecordsFound);
        btnAddNewRecord = (Button) findViewById(R.id.btsaveproduct);
    }

    private void bindWidgetsWithEvent() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRecord();
            }
        });
    }

    private void onAddRecord() {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Constants.DML_TYPE, Constants.INSERT);
        startActivityForResult(intent, Constants.ADD_RECORD);
    }

    private void onUpdateRecord(String productname, String hsn, String quantity, String unit, String unitprice, String discount,
                                String cgst, String sgst, String igst, String cess) {

        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Constants.NAME, productname);
        intent.putExtra(Constants.HSN, hsn);
        intent.putExtra(Constants.QUANTITY, quantity);
        intent.putExtra(Constants.UNIT, unit);
        intent.putExtra(Constants.UNITPRICE, unitprice);
        intent.putExtra(Constants.DISCOUNT, discount);
        intent.putExtra(Constants.CGST, cgst);
        intent.putExtra(Constants.SGST, sgst);
        intent.putExtra(Constants.IGST, igst);
        intent.putExtra(Constants.CESS, cess);
        intent.putExtra(Constants.DML_TYPE, Constants.UPDATE);
        startActivityForResult(intent, Constants.UPDATE_RECORD);
    }

    private void displayAllRecords() {

        LinearLayout inflateParentView;
        parentLayout.removeAllViews();
        final DecimalFormat df = new DecimalFormat("#.##");
        final ArrayList<Product_Model> products = sQLiteHelper.getAllRecords();
        if (products.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            Product_Model contactModel;
            for (int i = 0; i < products.size(); i++) {

                contactModel = products.get(i);

                final Holder holder = new Holder();
                final View view = LayoutInflater.from(this).inflate(R.layout.final_product_details, null);
                inflateParentView = (LinearLayout) view.findViewById(R.id.inflateParentView);
                holder.tvproduct = (TextView) view.findViewById(R.id.productname);
                holder.tvquantity = (TextView) view.findViewById(R.id.quantity);
                holder.tvwithtaxamount = (TextView) view.findViewById(R.id.withtaxprice);
                holder.edit = (ImageView) view.findViewById(R.id.edititem);
                holder.delete = (ImageView) view.findViewById(R.id.deleteitem);

                view.setTag(contactModel.getID());
                holder.sproduct = contactModel.getProduct_name();
                holder.shsn = contactModel.getHsn();
                holder.squantity = contactModel.getQuantity();
                holder.sunit = contactModel.getUnit();
                holder.sunitprice = contactModel.getUnit_price();
                holder.sdiscount = contactModel.getDisperc();
                holder.scgst = contactModel.getCgstper();
                holder.ssgst = contactModel.getSgstper();
                holder.sigst = contactModel.getIgstper();
                holder.scess = contactModel.getCessper();


                if (holder.sproduct == null) {
                    holder.sproduct = "";
                } else if (holder.shsn == null) {
                    holder.shsn = "";
                } else if (holder.squantity == null) {
                    holder.squantity = "1";
                } else if (holder.sunit == null) {
                    holder.sunit = "";
                } else if (holder.sunitprice == null) {
                    holder.sunitprice = "0";
                } else if (holder.sdiscount == null) {
                    holder.sdiscount = "0";
                } else if (holder.scgst == null) {
                    holder.scgst = "0";
                } else if (holder.ssgst == null) {
                    holder.ssgst = "";
                } else if (holder.sigst == null) {
                    holder.sigst = "0";
                } else if (holder.scess == null) {
                    holder.scess = "0";
                }

                holder.tvproduct.setText(holder.sproduct);
                holder.tvquantity.setText(holder.squantity + holder.sunit + "x" + holder.sunitprice);

                String amount = holder.squantity + "*" + holder.sunitprice;
                final Double damount = new ExtendedDoubleEvaluator().evaluate(amount);

                String discount = df.format(damount) + "*" + holder.sdiscount + "/" + "100";
                final Double ddiscount = new ExtendedDoubleEvaluator().evaluate(discount);

                String cgst = df.format(damount) + "*" + holder.scgst + "/" + "100";
                final Double dcgst = new ExtendedDoubleEvaluator().evaluate(cgst);

                String sgst = df.format(damount) + "*" + holder.ssgst + "/" + "100";
                final Double dsgst = new ExtendedDoubleEvaluator().evaluate(sgst);

                String igst = df.format(damount) + "*" + holder.sigst + "/" + "100";
                final Double digst = new ExtendedDoubleEvaluator().evaluate(igst);

                String cess = df.format(damount) + "*" + holder.scess + "/" + "100";
                final Double dcess = new ExtendedDoubleEvaluator().evaluate(cess);

                final Double tax = dcgst + dsgst + digst + dcess;

                final Double total = damount + tax - ddiscount;
                holder.tvwithtaxamount.setText("$" + df.format(total));


                final int position = i;


                inflateParentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListOfProduct.this, MainActivity.class);
                        Log.i("itempos", String.valueOf(position));
//                        intent.putExtra("listofproduct",products);
//                        intent.putExtra("product_position",position);
                        Product_Model model = new Product_Model();
                        model = products.get(position);
                        intent.putExtra("model", model);
                        String sid = model.getID();
                        intent.putExtra("selectID", sid);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });

                final CharSequence[] items = {Constants.UPDATE, Constants.DELETE};

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowID = view.getTag().toString();
                        onUpdateRecord(holder.sproduct, holder.shsn, holder.squantity, holder.sunit, holder.sunitprice,
                                holder.sdiscount, holder.scgst, holder.ssgst, holder.sigst, holder.scess);
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(ListOfProduct.this);
                        deleteDialogOk.setTitle("Delete Product?");
                        deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Product_Model contact = new Product_Model();
                                        contact.setID(view.getTag().toString());
                                        sQLiteHelper.deleteRecord(contact);
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

    static class Holder {

        String sproduct, shsn, squantity, sunit, sunitprice, sdiscount, scgst, ssgst, sigst, scess;
        TextView tvproduct, tvquantity, tvwithtaxamount;
        ImageView edit, delete;

    }
}
