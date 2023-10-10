package com.conestogasem3.invoicegenerator.SqliteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.conestogasem3.invoicegenerator.ModelClass.Customer_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Invoice_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Product_Model;

import java.util.ArrayList;



public class HelperClass extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
//    public static final String DATABASE_NAME = "/mnt/sdcard/GSTinvoice.db";
    public static final String DATABASE_NAME = "/data/data/com.conestogasem3.invoicegenerator/GSTinvoice.db";

    //Product Table
    private static final String TABLE_NAME = "PRODUCT";
    private static final String COLUMN_ID = "PR_ID";
    private static final String COLUMN_PRODUCT_NAME = "PRODUCT_NAME";
    private static final String COLUMN_HSN = "HSN";
    private static final String COLUMN_QUANTITY = "QUANTITY";
    private static final String COLUMN_UNIT = "UNIT";
    private static final String COLUMN_UNIT_PRICE = "UNIT_PRICE";
    private static final String COLUMN_DISSCOUNT = "DISSCOUNT";
    private static final String COLUMN_CGST = "CGST";
    private static final String COLUMN_SGST = "SGST";
    private static final String COLUMN_IGST = "IGST";
    private static final String COLUMN_CESS = "CESS";

    //Customer Table
    private static final String CUSTOMER_TABLE = "CUSTOMER";
    private static final String CUSTOMER_ID = "CS_ID";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String CUSTPMER_EMAIL = "CUSTPMER_EMAIL";
    private static final String CUSTOMER_GST = "CUSTOMER_GST";
    private static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    private static final String CUSTOMER_CITY = "CUSTOMER_CITY";
    private static final String CUSTOMER_STATE = "CUSTOMER_STATE";
    private static final String CUSTOMER_PIN = "CUSTOMER_PIN";

    //Invoice Table
    private static final String INVOICE_TABLE = "INVOICE";
    private static final String INVOICE_ID = "ID";
    private static final String INVOICE_DATE = "DATE";
    private static final String IN_PRODUCTID = "PRODUCT_ID";
    private static final String IN_CUSTOMERID = "CUSTOMER_ID";

    private SQLiteDatabase database;

    public HelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        try {

            db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRODUCT_NAME + " VARCHAR, " + COLUMN_HSN + " VARCHAR," + COLUMN_QUANTITY + " VARCHAR,"
                    + COLUMN_UNIT + " VARCHAR," + COLUMN_UNIT_PRICE + " VARCHAR," + COLUMN_DISSCOUNT + " VARCHAR," +
                    COLUMN_CGST + " VARCHAR," + COLUMN_SGST + " VARCHAR," + COLUMN_IGST + " VARCHAR," + COLUMN_CESS + " VARCHAR);");

            db.execSQL("create table " + CUSTOMER_TABLE + " ( " + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CUSTOMER_NAME + " VARCHAR, " + CUSTPMER_EMAIL + " VARCHAR," + CUSTOMER_GST + " VARCHAR,"
                    + CUSTOMER_ADDRESS + " VARCHAR," + CUSTOMER_CITY + " VARCHAR," + CUSTOMER_STATE + " VARCHAR," +
                    CUSTOMER_PIN + " VARCHAR);");

            db.execSQL("create table " + INVOICE_TABLE + " ( " +
                    INVOICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    INVOICE_DATE + " VARCHAR, " +
                    IN_PRODUCTID + " VARCHAR, " +
                    IN_CUSTOMERID + " VARCHAR);"
            );

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("error", String.valueOf(e));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INVOICE_TABLE);
        onCreate(db);
    }

    public void insertRecord(Product_Model productModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, productModel.getProduct_name());
        contentValues.put(COLUMN_HSN, productModel.getHsn());
        contentValues.put(COLUMN_QUANTITY, productModel.getQuantity());
        contentValues.put(COLUMN_UNIT, productModel.getUnit());
        contentValues.put(COLUMN_UNIT_PRICE, productModel.getUnit_price());
        contentValues.put(COLUMN_DISSCOUNT, productModel.getDisperc());
        contentValues.put(COLUMN_CGST, productModel.getCgstper());
        contentValues.put(COLUMN_SGST, productModel.getSgstper());
        contentValues.put(COLUMN_IGST, productModel.getIgstper());
        contentValues.put(COLUMN_CESS, productModel.getCessper());

        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public void insertCustomer(Customer_Model customerModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_NAME, customerModel.getCustomer_Name());
        contentValues.put(CUSTPMER_EMAIL, customerModel.getEmail());
        contentValues.put(CUSTOMER_GST, customerModel.getGst());
        contentValues.put(CUSTOMER_ADDRESS, customerModel.getAddress());
        contentValues.put(CUSTOMER_CITY, customerModel.getCity());
        contentValues.put(CUSTOMER_STATE, customerModel.getState());
        contentValues.put(CUSTOMER_PIN, customerModel.getPincode());

        database.insert(CUSTOMER_TABLE, null, contentValues);
        database.close();
    }

    public void insertInvoice(Invoice_Model invoice_model) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INVOICE_DATE, invoice_model.getInvoiceDate());
        contentValues.put(IN_PRODUCTID, invoice_model.getInvoiceProductID());
        contentValues.put(IN_CUSTOMERID, invoice_model.getInvoiceCustomerID());
        database.insert(INVOICE_TABLE, null, contentValues);
        database.close();
    }

    public ArrayList<Product_Model> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Product_Model> products = new ArrayList<Product_Model>();
        Product_Model productModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                productModel = new Product_Model();
                productModel.setID(cursor.getString(0));
                productModel.setProduct_name(cursor.getString(1));
                productModel.setHsn(cursor.getString(2));
                productModel.setQuantity(cursor.getString(3));
                productModel.setUnit(cursor.getString(4));
                productModel.setUnit_price(cursor.getString(5));
                productModel.setDisperc(cursor.getString(6));
                productModel.setCgstper(cursor.getString(7));
                productModel.setSgstper(cursor.getString(8));
                productModel.setIgstper(cursor.getString(9));
                productModel.setCessper(cursor.getString(10));
                products.add(productModel);
            }
        }
        cursor.close();
        database.close();

        return products;
    }

    public ArrayList<Customer_Model> getAllCustomerRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(CUSTOMER_TABLE, null, null, null, null, null, null);

        ArrayList<Customer_Model> customers = new ArrayList<Customer_Model>();
        Customer_Model customerModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                customerModel = new Customer_Model();
                customerModel.setIDcustomer(cursor.getString(0));
                customerModel.setCustomer_Name(cursor.getString(1));
                customerModel.setEmail(cursor.getString(2));
                customerModel.setGst(cursor.getString(3));
                customerModel.setAddress(cursor.getString(4));
                customerModel.setCity(cursor.getString(5));
                customerModel.setState(cursor.getString(6));
                customerModel.setPincode(cursor.getString(7));
                customers.add(customerModel);
            }
        }
        cursor.close();
        database.close();

        return customers;
    }

    public ArrayList<Invoice_Model> getAllInvoiceRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(INVOICE_TABLE, null, null, null, null, null, null);

        ArrayList<Invoice_Model> invoices = new ArrayList<Invoice_Model>();
        Invoice_Model invoice_model;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                invoice_model = new Invoice_Model();
                invoice_model.setInvoiceID(cursor.getString(0));
                invoice_model.setInvoiceDate(cursor.getString(1));
                invoice_model.setInvoiceProductID(cursor.getString(2));
                invoice_model.setInvoiceCustomerID(cursor.getString(3));
                invoices.add(invoice_model);
            }
        }
        cursor.close();
        database.close();

        return invoices;
    }

    public void updateRecord(Product_Model productModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, productModel.getProduct_name());
        contentValues.put(COLUMN_HSN, productModel.getHsn());
        contentValues.put(COLUMN_QUANTITY, productModel.getQuantity());
        contentValues.put(COLUMN_UNIT, productModel.getUnit());
        contentValues.put(COLUMN_UNIT_PRICE, productModel.getUnit_price());
        contentValues.put(COLUMN_DISSCOUNT, productModel.getDisperc());
        contentValues.put(COLUMN_CGST, productModel.getCgstper());
        contentValues.put(COLUMN_SGST, productModel.getSgstper());
        contentValues.put(COLUMN_IGST, productModel.getIgstper());
        contentValues.put(COLUMN_CESS, productModel.getCessper());
        database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{productModel.getID()});
        database.close();
    }


    public void updateCustomerRecord(Customer_Model customerModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_NAME, customerModel.getCustomer_Name());
        contentValues.put(CUSTPMER_EMAIL, customerModel.getEmail());
        contentValues.put(CUSTOMER_GST, customerModel.getGst());
        contentValues.put(CUSTOMER_ADDRESS, customerModel.getAddress());
        contentValues.put(CUSTOMER_CITY, customerModel.getCity());
        contentValues.put(CUSTOMER_STATE, customerModel.getState());
        contentValues.put(CUSTOMER_PIN, customerModel.getPincode());

        database.update(CUSTOMER_TABLE, contentValues, CUSTOMER_ID + " = ?", new String[]{customerModel.getIDcustomer()});
        database.close();
    }

    public void updateInvoiceRecord(Invoice_Model invoice_model) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INVOICE_DATE, invoice_model.getInvoiceDate());
        contentValues.put(IN_PRODUCTID, invoice_model.getInvoiceProductID());
        contentValues.put(IN_CUSTOMERID, invoice_model.getInvoiceCustomerID());

        database.update(INVOICE_TABLE, contentValues, INVOICE_ID + " = ?", new String[]{invoice_model.getInvoiceID()});
        database.close();
    }

    public void deleteRecord(Product_Model productModel) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{productModel.getID()});
        database.close();
    }

    public void deleteCustomerRecord(Customer_Model customerModel) {
        database = this.getReadableDatabase();
        database.delete(CUSTOMER_TABLE, CUSTOMER_ID + " = ?", new String[]{customerModel.getIDcustomer()});
        database.close();
    }


    public void deleteInvoiceRecord(Invoice_Model invoice_model) {
        database = this.getReadableDatabase();
        database.delete(INVOICE_TABLE, INVOICE_ID + " = ?", new String[]{invoice_model.getInvoiceID()});
        database.close();
    }


    public Customer_Model getcustomerunit(String customerid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE + " WHERE "
                + CUSTOMER_ID + " = " + customerid;

        Log.d("qwry", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Customer_Model customerModel = new Customer_Model();
        customerModel.setIDcustomer(c.getString(c.getColumnIndex(CUSTOMER_ID)));
        customerModel.setCustomer_Name((c.getString(c.getColumnIndex(CUSTOMER_NAME))));
        customerModel.setEmail((c.getString(c.getColumnIndex(CUSTPMER_EMAIL))));
        customerModel.setGst((c.getString(c.getColumnIndex(CUSTOMER_GST))));
        customerModel.setAddress((c.getString(c.getColumnIndex(CUSTOMER_ADDRESS))));
        customerModel.setCity((c.getString(c.getColumnIndex(CUSTOMER_CITY))));
        customerModel.setState((c.getString(c.getColumnIndex(CUSTOMER_STATE))));
        customerModel.setPincode((c.getString(c.getColumnIndex(CUSTOMER_PIN))));


        return customerModel;
    }

    public Product_Model getproductunit(String productid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + productid;

        Log.d("qwry", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        Product_Model productModel = new Product_Model();
        productModel.setID(c.getString(c.getColumnIndex(COLUMN_ID)));
        productModel.setProduct_name((c.getString(c.getColumnIndex(COLUMN_PRODUCT_NAME))));
        productModel.setHsn((c.getString(c.getColumnIndex(COLUMN_HSN))));
        productModel.setQuantity((c.getString(c.getColumnIndex(COLUMN_QUANTITY))));
        productModel.setUnit((c.getString(c.getColumnIndex(COLUMN_UNIT))));
        productModel.setUnit_price((c.getString(c.getColumnIndex(COLUMN_UNIT_PRICE))));
        productModel.setDisperc((c.getString(c.getColumnIndex(COLUMN_DISSCOUNT))));
        productModel.setCgstper((c.getString(c.getColumnIndex(COLUMN_CGST))));
        productModel.setSgstper((c.getString(c.getColumnIndex(COLUMN_SGST))));
        productModel.setIgstper((c.getString(c.getColumnIndex(COLUMN_IGST))));
        productModel.setCessper((c.getString(c.getColumnIndex(COLUMN_CESS))));

        return productModel;
    }

    public Invoice_Model getInvoiceunit(String invoiceid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + INVOICE_TABLE + " WHERE "
                + INVOICE_ID + " = " + invoiceid;

        Log.d("qwry", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        Invoice_Model invoice_model = new Invoice_Model();
        invoice_model.setInvoiceID(c.getString(c.getColumnIndex(INVOICE_ID)));
        invoice_model.setInvoiceDate((c.getString(c.getColumnIndex(INVOICE_DATE))));
        invoice_model.setInvoiceProductID((c.getString(c.getColumnIndex(IN_PRODUCTID))));
        invoice_model.setInvoiceCustomerID((c.getString(c.getColumnIndex(IN_CUSTOMERID))));

        return invoice_model;
    }

}
