package com.conestogasem3.invoicegenerator.ModelClass;

import java.io.Serializable;



public class Invoice_Model implements Serializable {

    String InvoiceID, InvoiceDate, InvoiceProductID, InvoiceCustomerID;

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getInvoiceProductID() {
        return InvoiceProductID;
    }

    public void setInvoiceProductID(String invoiceProductID) {
        InvoiceProductID = invoiceProductID;
    }

    public String getInvoiceCustomerID() {
        return InvoiceCustomerID;
    }

    public void setInvoiceCustomerID(String invoiceCustomerID) {
        InvoiceCustomerID = invoiceCustomerID;
    }
}
