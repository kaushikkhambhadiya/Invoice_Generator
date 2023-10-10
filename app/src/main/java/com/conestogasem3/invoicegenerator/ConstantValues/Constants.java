package com.conestogasem3.invoicegenerator.ConstantValues;


public class Constants {
    public static final int ADD_RECORD = 0;
    public static final int UPDATE_RECORD = 1;
    public static final int ADD_CUSTOMER = 3;
    public static final int UPDATE_CUSTOMER = 4;
    public static final int PASS_CUSTOMERS = 5;
    public static final int PASS_PRODUCTS = 6;
    public static final String DML_TYPE = "DML_TYPE";
    public static final String UPDATE = "Update";
    public static final String INSERT = "Insert";
    public static final String DELETE = "Delete";

    //Product
    public static final String ID = "ID";
    public static final String NAME = "name";
    public static final String HSN = "hsn";
    public static final String QUANTITY = "quantity";
    public static final String UNIT = "unit";
    public static final String UNITPRICE = "unitprice";
    public static final String DISCOUNT = "discount";
    public static final String CGST = "cgst";
    public static final String SGST = "sgst";
    public static final String IGST = "igst";
    public static final String CESS = "cess";
    public static final String AMOUNT = "amount";
    public static final String SAVE = "saveproduct";

    //Customer
    public static final String IDCUSTOMER = "IDCustomer";
    public static final String CUSTOMERNAME = "Customername";
    public static final String EMAIL = "Email";
    public static final String GST = "Gst";
    public static final String ADDRESS = "Address";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String PIN = "Pincode";

    //Invoice
    public static final String IDINVOICE = "IDInvoice";
    public static final String INDATE = "Date";
    public static final String INPRODUCT = "ProductId";
    public static final String INCUSTOMER = "CustomerId";


}
