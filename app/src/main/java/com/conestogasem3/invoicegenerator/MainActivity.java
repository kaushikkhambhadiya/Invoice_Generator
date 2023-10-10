package com.conestogasem3.invoicegenerator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;
import com.conestogasem3.invoicegenerator.ModelClass.Customer_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Product_Model;
import com.conestogasem3.invoicegenerator.SqliteHelper.HelperClass;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("#.##");

    TextView title;
    ImageView back;
    EditText selectdate, invoice_number;
    Calendar myCalendar;
    //Supplier
    private int SUPPLIERCODE = 1;
    TextView firmname, address, city, pincode, gstin, state, email;
    ImageView editsupdetail;

    //Buyer Acitivty Names
    LinearLayout addBuyer, addllbuyer;
    ArrayList<Customer_Model> listCustomer = new ArrayList<Customer_Model>();

    Customer_Model customerModel;
    String RawSelectCustomer;

    //Product Activity Names
    LinearLayout addproduct, addllproduct;
    ArrayList<Product_Model> listProduct = new ArrayList<Product_Model>();
    ;
    Product_Model productModel;
    String RawSelectProduct;
    ArrayList<String> IdProductlist = new ArrayList<String>();

    //Inflate Bill with ViewStub
    ViewStub stub;
    View inflated;

    //Generate Invoice Button
    Button btcreateinvoice;

    //HelperClass to get Values from Id
    HelperClass sQLiteHelper;

    //Poping Up Product Details
    PopupWindow mpopup;

    //invoice id
    String invoiceno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllWidgetIds();

        stub = (ViewStub) findViewById(R.id.bill);
        inflated = stub.inflate();
        inflated.setVisibility(View.GONE);

        title.setText(getResources().getString(R.string.app_name));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkForRequest();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        selectdate.setText(dateFormat.format(new Date()));
        invoice_number.setText("1");

        invoice_number.setSelection(invoice_number.getText().length());

        invoice_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice_number.setCursorVisible(true);
                invoice_number.setSelection(invoice_number.getText().length());
            }
        });

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        selectdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        editsupdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Supplier_Details.class);
                startActivityForResult(intent, SUPPLIERCODE);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Name", "");
        String emaill = preferences.getString("Email", "");
        String gst = preferences.getString("Gst", "");
        String addrress = preferences.getString("Address", "");
        String cityy = preferences.getString("City", "");
        String statee = preferences.getString("State", "");
        String pin = preferences.getString("Pin", "");

        firmname.setText(name);
        email.setText(emaill);
        gstin.setText(gst);
        address.setText(addrress);
        city.setText(cityy + "-");
        state.setText(statee);
        pincode.setText(pin);

        addBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOfBuyer.class);
                startActivityForResult(intent, Constants.PASS_CUSTOMERS);
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOfProduct.class);
                startActivityForResult(intent, Constants.PASS_PRODUCTS);
            }
        });

        btcreateinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listProduct.size() > 0 && listCustomer.size() > 0) {


                    Intent intent = new Intent(MainActivity.this, GenerateInvoicePdf.class);

                    intent.putExtra("supname", firmname.getText().toString());
                    intent.putExtra("supaddress", address.getText().toString());
                    intent.putExtra("supcity", city.getText().toString());
                    intent.putExtra("suppincode", pincode.getText().toString());
                    intent.putExtra("supgstin", gstin.getText().toString());
                    intent.putExtra("supstate", state.getText().toString());
                    intent.putExtra("supemail", email.getText().toString());

                    intent.putExtra("dateofinvoice", selectdate.getText().toString());
                    intent.putExtra("noinvoice", invoice_number.getText().toString());
//                intent.putExtra("invoiceid",invoiceno);

                    intent.putExtra("finalcustomer", customerModel);
                    intent.putExtra("finalproductlist", listProduct);
                    startActivity(intent);

                    onButtonClick();

                } else {
                    if (listProduct.size() <= 0) {
                        Toast.makeText(MainActivity.this, "Please insert atleast product", Toast.LENGTH_SHORT).show();
                    }  if (listCustomer.size() <= 0){
                        Toast.makeText(MainActivity.this, "Please insert the Buyer", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void getAllWidgetIds() {

        title = (TextView) findViewById(R.id.maintitle);
        back = (ImageView) findViewById(R.id.back);

        //Date and Invoice Number
        selectdate = (EditText) findViewById(R.id.date);
        invoice_number = (EditText) findViewById(R.id.invoicenum);

        //Suplier Details
        firmname = (TextView) findViewById(R.id.firm);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        gstin = (TextView) findViewById(R.id.gstin);
        state = (TextView) findViewById(R.id.state);
        editsupdetail = (ImageView) findViewById(R.id.ivedit);

        //Buyer Details
        addBuyer = (LinearLayout) findViewById(R.id.lladdcustomer);
        addllbuyer = (LinearLayout) findViewById(R.id.addparentLayout);

        //Product Details
        addproduct = (LinearLayout) findViewById(R.id.lladdproduct);
        addllproduct = (LinearLayout) findViewById(R.id.addparentproductLayout);

        //Generate Invoice
        btcreateinvoice = (Button) findViewById(R.id.btaddinvoice);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SUPPLIERCODE && resultCode == RESULT_OK && data != null) {
            firmname.setText(data.getStringExtra("sfirm"));
            email.setText(data.getStringExtra("semail"));
            gstin.setText(data.getStringExtra("sgst"));
            address.setText(data.getStringExtra("saddress"));
            city.setText(data.getStringExtra("scity") + "-");
            state.setText(data.getStringExtra("sstate"));
            pincode.setText(data.getStringExtra("spin"));

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Name", data.getStringExtra("sfirm"));
            editor.putString("Email", data.getStringExtra("semail"));
            editor.putString("Gst", data.getStringExtra("sgst"));
            editor.putString("Address", data.getStringExtra("saddress"));
            editor.putString("City", data.getStringExtra("scity"));
            editor.putString("State", data.getStringExtra("sstate"));
            editor.putString("Pin", data.getStringExtra("spin"));
            editor.apply();
        }

        if (requestCode == Constants.PASS_CUSTOMERS && resultCode == RESULT_OK && data != null) {

            customerModel = (Customer_Model) data.getSerializableExtra("customermodel");
            Log.i("customers", String.valueOf(customerModel));
            listCustomer.add(customerModel);
            RawSelectCustomer = data.getStringExtra("customerid");

            SelectedBuyers();
        }

        if (requestCode == Constants.PASS_PRODUCTS && resultCode == RESULT_OK && data != null) {

            productModel = (Product_Model) data.getSerializableExtra("model");
            Log.i("model", String.valueOf(productModel));
            listProduct.add(productModel);
            Log.i("result_array", String.valueOf(listProduct));
            RawSelectProduct = data.getStringExtra("selectID");
            IdProductlist.add(RawSelectProduct);
            SelectedProducts();
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        selectdate.setText(sdf.format(myCalendar.getTime()));
    }


    private void SelectedBuyers() {

        addllbuyer.removeAllViews();
        if (listCustomer.size() > 0) {
            final Holder holder = new Holder();
            final View customerview = LayoutInflater.from(this).inflate(R.layout.listitembuyer, null);
            holder.tvcname = (TextView) customerview.findViewById(R.id.buyername);
            holder.tvcemail = (TextView) customerview.findViewById(R.id.buyeremail);
            holder.tvcadderess = (TextView) customerview.findViewById(R.id.buyeraddress);
            holder.tvccity = (TextView) customerview.findViewById(R.id.buyercity);
            holder.tvcpin = (TextView) customerview.findViewById(R.id.buyerpincode);
            holder.tvcgstin = (TextView) customerview.findViewById(R.id.buyergstin);
            holder.tvcstate = (TextView) customerview.findViewById(R.id.buyerstate);

            holder.deletecustomer = (ImageView) customerview.findViewById(R.id.ivdeletecustomer);

            holder.customername = customerModel.getCustomer_Name();
            holder.customeremail = customerModel.getEmail();
            holder.customergstin = customerModel.getGst();
            holder.customeraddress = customerModel.getAddress();
            holder.customercity = customerModel.getCity();
            holder.customerstate = customerModel.getState();
            holder.customerpin = customerModel.getPincode();

            holder.tvcname.setText(holder.customername);
            holder.tvcemail.setText(holder.customeremail);
            holder.tvcadderess.setText(holder.customeraddress);
            holder.tvccity.setText(holder.customercity + "-");
            holder.tvcpin.setText(holder.customerpin);
            holder.tvcgstin.setText(holder.customergstin);
            holder.tvcstate.setText(holder.customerstate);

            holder.deletecustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(MainActivity.this);
                    deleteDialogOk.setTitle("Delete Product?");
                    deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addllbuyer.removeView(customerview);
                                    listCustomer.clear();
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

            addllbuyer.addView(customerview);

        }
    }

    private void SelectedProducts() {

        addllproduct.removeAllViews();
        if (listProduct.size() > 0) {

            inflated.setVisibility(View.VISIBLE);

            final ArrayList<Double> totalamount = new ArrayList<Double>();
            final ArrayList<Double> totalcgst = new ArrayList<Double>();
            final ArrayList<Double> totalsgst = new ArrayList<Double>();
            final ArrayList<Double> totaligst = new ArrayList<Double>();
            final ArrayList<Double> totalcess = new ArrayList<Double>();
            final ArrayList<Double> totaldiscount = new ArrayList<Double>();
            final ArrayList<Double> totalprice = new ArrayList<Double>();
            final ArrayList<Double> totaltax = new ArrayList<Double>();

            for (int i = 0; i < listProduct.size(); i++) {

                productModel = listProduct.get(i);
                final Holder holder = new Holder();
                final View view = LayoutInflater.from(this).inflate(R.layout.listitemproduct, null);

                holder.llproduct = (LinearLayout) view.findViewById(R.id.inflateParentView);
                holder.tvproduct = (TextView) view.findViewById(R.id.productname);
                holder.tvquantity = (TextView) view.findViewById(R.id.quantity);
                holder.tvwithtaxamount = (TextView) view.findViewById(R.id.withtaxprice);
                holder.delete = (ImageView) view.findViewById(R.id.deleteitem);

                //Bill Inflate at the end of listproduct
                holder.tvtax = (TextView) inflated.findViewById(R.id.tvfinaltaxamount);
                holder.tvamount = (TextView) inflated.findViewById(R.id.tvfinalamount);
                holder.tvdiscount = (TextView) inflated.findViewById(R.id.tvfinaldiscount);
                holder.tvcgst = (TextView) inflated.findViewById(R.id.tvfinalcgst);
                holder.tvsgst = (TextView) inflated.findViewById(R.id.tvfinalsgst);
                holder.tvigst = (TextView) inflated.findViewById(R.id.tvfinaligst);
                holder.tvcess = (TextView) inflated.findViewById(R.id.tvfinalcess);
                holder.tvtotal = (TextView) inflated.findViewById(R.id.tvfinaltotal);

                view.setTag(productModel.getID());
                holder.sproduct = productModel.getProduct_name();
                holder.shsn = productModel.getHsn();
                holder.squantity = productModel.getQuantity();
                holder.sunit = productModel.getUnit();
                holder.sunitprice = productModel.getUnit_price();
                holder.sdiscount = productModel.getDisperc();
                holder.scgst = productModel.getCgstper();
                holder.ssgst = productModel.getSgstper();
                holder.sigst = productModel.getIgstper();
                holder.scess = productModel.getCessper();

                //set Values in Stub
                holder.tvproduct.setText(holder.sproduct);
                holder.tvquantity.setText(holder.squantity + holder.sunit + "x" + holder.sunitprice);

                if (holder.squantity == null) {
                    holder.squantity = "1";
                } else if (holder.sunitprice == null) {
                    holder.sunitprice = "0";
                } else if (holder.sdiscount == null) {
                    holder.sdiscount = "0";
                } else if (holder.scgst == null) {
                    holder.scgst = "0";
                } else if (holder.ssgst == null) {
                    holder.ssgst = "0";
                } else if (holder.sigst == null) {
                    holder.sigst = "0";
                } else if (holder.scess == null) {
                    holder.scess = "0";
                }

                String amount = holder.squantity + "*" + holder.sunitprice;
                Double damount = new ExtendedDoubleEvaluator().evaluate(amount);
                totalamount.add(damount);
                Log.i("totalam", String.valueOf(totalamount));
                double sumamount = 0;
                for (Double d : totalamount)
                    sumamount += d;

                String discount = df.format(damount) + "*" + holder.sdiscount + "/" + "100";
                Double ddiscount = new ExtendedDoubleEvaluator().evaluate(discount);
                totaldiscount.add(ddiscount);
                double sumdiscount = 0;
                for (Double d : totaldiscount)
                    sumdiscount += d;

                String cgst = df.format(damount) + "*" + holder.scgst + "/" + "100";
                Double dcgst = new ExtendedDoubleEvaluator().evaluate(cgst);
                totalcgst.add(dcgst);
                double sumcgst = 0;
                for (Double d : totalcgst)
                    sumcgst += d;

                String sgst = df.format(damount) + "*" + holder.ssgst + "/" + "100";
                Double dsgst = new ExtendedDoubleEvaluator().evaluate(sgst);
                totalsgst.add(dsgst);
                double sumsgst = 0;
                for (Double d : totalsgst)
                    sumsgst += d;

                String igst = df.format(damount) + "*" + holder.sigst + "/" + "100";
                Double digst = new ExtendedDoubleEvaluator().evaluate(igst);
                totaligst.add(digst);
                double sumigst = 0;
                for (Double d : totaligst)
                    sumigst += d;

                String cess = df.format(damount) + "*" + holder.scess + "/" + "100";
                Double dcess = new ExtendedDoubleEvaluator().evaluate(cess);
                totalcess.add(dcess);
                double sumcess = 0;
                for (Double d : totalcess)
                    sumcess += d;

                Double tax = dcgst + dsgst + digst + dcess;
                totaltax.add(tax);
                double sumtax = 0;
                for (Double d : totaltax)
                    sumtax += d;

                Double total = damount + tax - ddiscount;
                totalprice.add(total);
                double sumtotal = 0;
                for (Double d : totalprice)
                    sumtotal += d;


                holder.tvamount.setText("$" + df.format(sumamount));
                holder.tvdiscount.setText("$" + df.format(sumdiscount));
                holder.tvcgst.setText("$" + df.format(sumcgst));
                holder.tvsgst.setText("$" + df.format(sumsgst));
                holder.tvigst.setText("$" + df.format(sumigst));
                holder.tvcess.setText("$" + df.format(sumcess));
                holder.tvtax.setText("$" + df.format(sumtax));
                holder.tvtotal.setText("$" + df.format(sumtotal));
                holder.tvwithtaxamount.setText("$" + df.format(total));

                //Item Position
                final Integer itemPosition = i;

                holder.llproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // inflating popup layout
                        View popUpView = getLayoutInflater().inflate(R.layout.sort_popup_layout, null);
                        // Creation of popup
                        mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                        // Displaying popup
                        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                        TextView pname = (TextView) popUpView.findViewById(R.id.productname);
                        TextView pquantity = (TextView) popUpView.findViewById(R.id.quantity);
                        TextView prate = (TextView) popUpView.findViewById(R.id.rate);
                        TextView pamount = (TextView) popUpView.findViewById(R.id.amountprice);
                        TextView ptax = (TextView) popUpView.findViewById(R.id.taxprice);
                        TextView pcgstper = (TextView) popUpView.findViewById(R.id.cgstpercentage);
                        TextView pcgstprice = (TextView) popUpView.findViewById(R.id.cgstprice);
                        TextView psgstper = (TextView) popUpView.findViewById(R.id.sgstpercentage);
                        TextView psgstprice = (TextView) popUpView.findViewById(R.id.sgstprice);
                        TextView pigstper = (TextView) popUpView.findViewById(R.id.igstpercentage);
                        TextView pigstprice = (TextView) popUpView.findViewById(R.id.igstprice);
                        TextView pcessper = (TextView) popUpView.findViewById(R.id.cesspercentage);
                        TextView pcessprice = (TextView) popUpView.findViewById(R.id.cessprice);
                        TextView pdiscountper = (TextView) popUpView.findViewById(R.id.discountpercentage);
                        TextView pdiscountprice = (TextView) popUpView.findViewById(R.id.discountprice);
                        TextView ptotalprice = (TextView) popUpView.findViewById(R.id.totalprice);
                        Button btnCancel = (Button) popUpView.findViewById(R.id.close);

                        pname.setText(listProduct.get(itemPosition).getProduct_name());
                        pquantity.setText(listProduct.get(itemPosition).getQuantity() + listProduct.get(itemPosition).getUnit());
                        prate.setText("$" + listProduct.get(itemPosition).getUnit_price());
                        pcgstper.setText(listProduct.get(itemPosition).getCgstper() + "%");
                        psgstper.setText(listProduct.get(itemPosition).getSgstper() + "%");
                        pigstper.setText(listProduct.get(itemPosition).getIgstper() + "%");
                        pcessper.setText(listProduct.get(itemPosition).getCessper() + "%");
                        pdiscountper.setText(listProduct.get(itemPosition).getDisperc() + "%");

                        String amount = listProduct.get(itemPosition).getQuantity() + "*" + listProduct.get(itemPosition).getUnit_price();
                        Double damount = new ExtendedDoubleEvaluator().evaluate(amount);

                        String discount = df.format(damount) + "*" + listProduct.get(itemPosition).getDisperc() + "/" + "100";
                        Double ddiscount = new ExtendedDoubleEvaluator().evaluate(discount);

                        String cgst = df.format(damount) + "*" + listProduct.get(itemPosition).getCgstper() + "/" + "100";
                        Double dcgst = new ExtendedDoubleEvaluator().evaluate(cgst);

                        String sgst = df.format(damount) + "*" + listProduct.get(itemPosition).getSgstper() + "/" + "100";
                        Double dsgst = new ExtendedDoubleEvaluator().evaluate(sgst);

                        String igst = df.format(damount) + "*" + listProduct.get(itemPosition).getIgstper() + "/" + "100";
                        Double digst = new ExtendedDoubleEvaluator().evaluate(igst);

                        String cess = df.format(damount) + "*" + listProduct.get(itemPosition).getCessper() + "/" + "100";
                        Double dcess = new ExtendedDoubleEvaluator().evaluate(cess);

                        Double tax = dcgst + dsgst + digst + dcess;

                        Double total = damount + tax - ddiscount;


                        pamount.setText("$" + df.format(damount));
                        pdiscountprice.setText("$" + df.format(ddiscount));
                        pcgstprice.setText("$" + df.format(dcgst));
                        psgstprice.setText("$" + df.format(dsgst));
                        pigstprice.setText("$" + df.format(digst));
                        pcessprice.setText("$" + df.format(dcess));
                        ptax.setText("$" + df.format(tax));
                        ptotalprice.setText("$" + df.format(total));

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpopup.dismiss();
                            }
                        });

                    }
                });


                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(MainActivity.this);
                        deleteDialogOk.setTitle("Delete Product?");
                        deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                      for (int itemPosition = listProduct.size()-1; itemPosition >= 0; itemPosition--) {
                                addllproduct.removeView(view);
                                listProduct.remove(itemPosition.intValue());
                                IdProductlist.remove(itemPosition.intValue());
                                Log.i("newlist", String.valueOf(listProduct));

                                totalamount.remove(itemPosition);
                                double sumamount = 0;
                                for (Double d : totalamount)
                                    sumamount += d;

                                totaldiscount.remove(itemPosition);
                                double sumdiscount = 0;
                                for (Double d : totaldiscount)
                                    sumdiscount += d;

                                totalcgst.remove(itemPosition);
                                double sumcgst = 0;
                                for (Double d : totalcgst)
                                    sumcgst += d;

                                totalsgst.remove(itemPosition);
                                double sumsgst = 0;
                                for (Double d : totalsgst)
                                    sumsgst += d;

                                totaligst.remove(itemPosition);
                                double sumigst = 0;
                                for (Double d : totaligst)
                                    sumigst += d;


                                totalcess.remove(itemPosition);
                                double sumcess = 0;
                                for (Double d : totalcess)
                                    sumcess += d;


                                totaltax.remove(itemPosition);
                                double sumtax = 0;
                                for (Double d : totaltax)
                                    sumtax += d;


                                totalprice.remove(itemPosition);
                                double sumtotal = 0;
                                for (Double d : totalprice)
                                    sumtotal += d;


                                holder.tvamount.setText("$" + df.format(sumamount));
                                holder.tvdiscount.setText("$" + df.format(sumdiscount));
                                holder.tvcgst.setText("$" + df.format(sumcgst));
                                holder.tvsgst.setText("$" + df.format(sumsgst));
                                holder.tvigst.setText("$" + df.format(sumigst));
                                holder.tvcess.setText("$" + df.format(sumcess));
                                holder.tvtax.setText("$" + df.format(sumtax));
                                holder.tvtotal.setText("$" + df.format(sumtotal));

                                SelectedProducts();

                                if (listProduct.size() <= 0) {
                                    inflated.setVisibility(View.GONE);
                                }
                            }
                        });

                        deleteDialogOk.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        deleteDialogOk.show();
                    }
                });

                addllproduct.addView(view);
            }
        } else {
            Toast.makeText(this, "Please Add At Least One Product", Toast.LENGTH_SHORT).show();
        }
    }


    private void onButtonClick() {
        Intent intent = new Intent();

        if (RawSelectCustomer == null || IdProductlist.size() == 0) {
            Toast.makeText(MainActivity.this, "Add Buyer and Product First to Generate Invoice", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(Constants.INPRODUCT, IdProductlist);
            intent.putExtra(Constants.INCUSTOMER, RawSelectCustomer);
            intent.putExtra(Constants.INDATE, selectdate.getText().toString());
            intent.putExtra("invoicenumber", invoice_number.getText().toString());

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void checkForRequest() {
        String request = getIntent().getExtras().get(Constants.DML_TYPE).toString();
        if (request.equals(Constants.UPDATE)) {
            btcreateinvoice.setText(Constants.UPDATE);
            invoiceno = getIntent().getExtras().get(Constants.IDINVOICE).toString();
            String invoicedate = getIntent().getExtras().get(Constants.INDATE).toString();
            String invoicecustomerid = getIntent().getExtras().get(Constants.INCUSTOMER).toString();
            String invoiceproductid = getIntent().getExtras().get(Constants.INPRODUCT).toString();


//            //Send invoice Number to Generate Pdf
//            InvoiceNumber = invoiceno;

            //Set Values to Update
            sQLiteHelper = new HelperClass(this);
            invoice_number.setText(invoiceno);
            selectdate.setText(invoicedate);

            customerModel = sQLiteHelper.getcustomerunit(invoicecustomerid);
            listCustomer.add(customerModel);
            RawSelectCustomer = invoicecustomerid;

            //Split String for using Sql query
//            ArrayList<String> productids = new ArrayList<>(Arrays.asList(invoiceproductid.split("\\s*,\\s*")));

            ArrayList<String> productids = new ArrayList<>(Arrays.asList(invoiceproductid.split(",")));
            Log.i("StringtoArray", String.valueOf(productids));

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String name = preferences.getString("Name", "");
            String emaill = preferences.getString("Email", "");
            String gst = preferences.getString("Gst", "");
            String addrress = preferences.getString("Address", "");
            String cityy = preferences.getString("City", "");
            String statee = preferences.getString("State", "");
            String pin = preferences.getString("Pin", "");

            firmname.setText(name);
            email.setText(emaill);
            gstin.setText(gst);
            address.setText(addrress);
            city.setText(cityy + "-");
            state.setText(statee);
            pincode.setText(pin);

            IdProductlist = productids;
            for (int i = 0; i < productids.size(); i++) {
                productModel = sQLiteHelper.getproductunit(productids.get(i));
                Log.i("productmodel", String.valueOf(productModel));
                listProduct.add(productModel);
                Log.i("list", String.valueOf(listProduct));
            }
            Log.i("list", String.valueOf(listCustomer));
            Log.i("list", String.valueOf(listProduct));
            SelectedBuyers();
            SelectedProducts();
        } else {
            btcreateinvoice.setText("Generate Invoice");
        }
    }

    private class Holder {

        //Customer Detail On MainActivity
        String customername, customeremail, customergstin, customeraddress, customercity, customerstate, customerpin;
        TextView tvcname, tvcemail, tvcadderess, tvccity, tvcpin, tvcgstin, tvcstate;
        ImageView deletecustomer;

        //Product Detail On MainActivity
        String sproduct, shsn, squantity, sunit, sunitprice, sdiscount, scgst, ssgst, sigst, scess;
        TextView tvproduct, tvquantity, tvamount, tvtax, tvdiscount, tvcgst, tvsgst, tvigst, tvcess, tvtotal, tvwithtaxamount;
        ImageView delete;
        LinearLayout llproduct;
    }
}
