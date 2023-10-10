package com.conestogasem3.invoicegenerator.ModelClass;


import java.io.Serializable;


public class Product_Model implements Serializable {

    private String ID, product_name, hsn, quantity, unit, unit_price, disperc, cgstper, sgstper, igstper, cessper;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getDisperc() {
        return disperc;
    }

    public void setDisperc(String disperc) {
        this.disperc = disperc;
    }

    public String getCgstper() {
        return cgstper;
    }

    public void setCgstper(String cgstper) {
        this.cgstper = cgstper;
    }

    public String getSgstper() {
        return sgstper;
    }

    public void setSgstper(String sgstper) {
        this.sgstper = sgstper;
    }

    public String getIgstper() {
        return igstper;
    }

    public void setIgstper(String igstper) {
        this.igstper = igstper;
    }

    public String getCessper() {
        return cessper;
    }

    public void setCessper(String cessper) {
        this.cessper = cessper;
    }
}
