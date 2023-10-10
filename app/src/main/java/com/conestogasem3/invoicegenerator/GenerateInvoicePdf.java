package com.conestogasem3.invoicegenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.conestogasem3.invoicegenerator.ModelClass.Customer_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Invoice_Model;
import com.conestogasem3.invoicegenerator.ModelClass.Product_Model;
import com.conestogasem3.invoicegenerator.SqliteHelper.HelperClass;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class GenerateInvoicePdf extends AppCompatActivity {

    //Widgets
    PDFView pdfView;
    TextView titleofactivity;
    ImageView back, share;

    //Suplier Details
    String firmname, address, city, pincode, gstin, state, email;
    String dateofinvoice, noinvoice;

    //Customer Details
    Customer_Model customer_model;

    //Product Details
    ArrayList<Product_Model> product_ArrayList = new ArrayList<>();

    //Pdf Generation
    String nameofpdf;
    double sumamount, sumdiscount, sumcgst, sumsgst, sumigst, sumcess, sumtotal, sumquantity, sumtax;
    ArrayList<Double> totalamount = new ArrayList<Double>();
    ArrayList<Double> totalcgst = new ArrayList<Double>();
    ArrayList<Double> totalsgst = new ArrayList<Double>();
    ArrayList<Double> totaligst = new ArrayList<Double>();
    ArrayList<Double> totalcess = new ArrayList<Double>();
    ArrayList<Double> totaldiscount = new ArrayList<Double>();
    ArrayList<Double> totalprice = new ArrayList<Double>();
    ArrayList<Double> totaltax = new ArrayList<Double>();
    ArrayList<Double> totalquantity = new ArrayList<Double>();

    //Pdf Font for Rupee Symbol
    public static final String FONT1 = "assets/PlayfairDisplay-Regular.ttf";

    //View Clicked
    int viewclicked;
    String invoiceid;

    ArrayList<String> productids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatepdf);

        getAllWidget();

        viewclicked = getIntent().getExtras().getInt("viewclicked");
        firmname = getIntent().getExtras().getString("supname");
        address = getIntent().getExtras().getString("supaddress");
        city = getIntent().getExtras().getString("supcity");
        pincode = getIntent().getExtras().getString("suppincode");
        gstin = getIntent().getExtras().getString("supgstin");
        state = getIntent().getExtras().getString("supstate");
        email = getIntent().getExtras().getString("supemail");

        dateofinvoice = getIntent().getExtras().getString("dateofinvoice");
        noinvoice = getIntent().getExtras().getString("noinvoice");
        invoiceid = getIntent().getExtras().getString("invoiceid");

        customer_model = (Customer_Model) getIntent().getSerializableExtra("finalcustomer");
        product_ArrayList = (ArrayList<Product_Model>) getIntent().getExtras().getSerializable("finalproductlist");
        if (viewclicked != 1) {
            for (int i = 0; i < product_ArrayList.size(); i++) {
                String id = product_ArrayList.get(i).getID();
                productids.add(id);
            }
        }
        Log.i("list", String.valueOf(product_ArrayList));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Force to get Pdf Uri From Storage
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (viewclicked == 1) {
            Invoice_Model invoicemodel = (Invoice_Model) getIntent().getSerializableExtra("invoicemodel");
            HelperClass sQLiteHelper;
            sQLiteHelper = new HelperClass(this);
            customer_model = sQLiteHelper.getcustomerunit(invoicemodel.getInvoiceCustomerID());
            String n = invoicemodel.getInvoiceProductID();
            nameofpdf = customer_model.getCustomer_Name() + customer_model.getGst() + n;
            Log.i("nn",n);
            viewPdf(nameofpdf + ".pdf", getResources().getString(R.string.app_name));

        }else {
            createPdf();
        }

        share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name);

                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                    final File file = new File(dir, nameofpdf + ".pdf");
                    Uri uri = Uri.fromFile(file);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    share.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    startActivity(Intent.createChooser(share, "Share File"));
                }
            });

    }

    private void createPdf() {

        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name);

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            String ProductidlistQuote = productids.toString().replace("[", "").replace("]", "")
                    .replace(", ", ",");
                nameofpdf = customer_model.getCustomer_Name() + customer_model.getGst() + ProductidlistQuote; //+ "("+System.currentTimeMillis()+")"
                Log.i("name",nameofpdf);



            final File file = new File(dir, nameofpdf + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Uri uri = Uri.fromFile(file);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    share.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    startActivity(Intent.createChooser(share, "Share File"));
                }
            });

            PdfWriter.getInstance(document, fOut);

            document.open();

            Font paraFont = new Font(Font.FontFamily.COURIER);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
            Chunk c = new Chunk("Invoice                                                              ", f);
            c.setBackground(BaseColor.WHITE);
            Paragraph p = new Paragraph(c);
            p.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(p);
            Paragraph p2 = new Paragraph("Name             : " + firmname);
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(paraFont);
            document.add(p2);
            Paragraph p3 = new Paragraph("Address         : " + address + "," + city + "-" + pincode + "," + state);
            p3.setAlignment(Paragraph.ALIGN_LEFT);
            p3.setFont(paraFont);
            document.add(p3);
            Paragraph p4 = new Paragraph("GSTIN           : " + gstin);
            p4.setAlignment(Paragraph.ALIGN_LEFT);
            p4.setFont(paraFont);
            document.add(p4);
            Paragraph p18 = new Paragraph("Email id          : " + email);
            p18.setAlignment(Paragraph.ALIGN_LEFT);
            p18.setFont(paraFont);
            p18.setSpacingAfter(20f);
            document.add(p18);
            LineSeparator objectName = new LineSeparator();
            objectName.setLineColor(BaseColor.DARK_GRAY);
            document.add(objectName);
            Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
            Chunk c1 = new Chunk("Bill To                                               ", f1);
            c1.setBackground(BaseColor.WHITE);
            Paragraph p5 = new Paragraph(c1);
            p5.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(p5);
            Chunk glue = new Chunk(new VerticalPositionMark());
//            "Company Name : "
            Paragraph p14 = new Paragraph("Company Name : " + customer_model.getCustomer_Name());
            p14.add(new Chunk(glue));
            p14.add("Invoice Number:"+noinvoice);
            document.add(p14);
            Chunk glue1 = new Chunk(new VerticalPositionMark());
//            "Address              :"
            Paragraph p15 = new Paragraph("Address :" + customer_model.getAddress());
            p15.add(new Chunk(glue));
            p15.add("Invoice Date  :"+dateofinvoice);
            document.add(p15);
//            "City                     : "
            Paragraph p16 = new Paragraph("City : " + customer_model.getCity()+ "," + customer_model.getState() + "-" + customer_model.getPincode());
            p16.add(new Chunk(glue));
            p16.add("GSTIN    :" +customer_model.getGst());
            document.add(p16);
//            "Email                  : "
            Paragraph p17 = new Paragraph("Email : " + customer_model.getEmail());
            document.add(p17);


            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingAfter(0f);
            table.setSpacingBefore(30f);

            // font
            Font FNT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
            Font RF = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            //Creating Header Cells
            PdfPCell cell1 = new PdfPCell(new Paragraph("Sr No", FNT));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell2 = new PdfPCell(new Paragraph("Product", FNT));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell3 = new PdfPCell(new Paragraph("HSN", FNT));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell4 = new PdfPCell(new Paragraph("QTY", FNT));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell6 = new PdfPCell(new Paragraph("Rate", FNT));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell7 = new PdfPCell(new Paragraph("Amount", FNT));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell14 = new PdfPCell(new Paragraph("Total", FNT));
            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell6);
            table.addCell(cell7);
            table.addCell(cell14);

            DecimalFormat df = new DecimalFormat("#.##");

            for (int i = 0; i < product_ArrayList.size(); i++) {

                String amount = product_ArrayList.get(i).getQuantity() + "*" + product_ArrayList.get(i).getUnit_price();
                Double damount = new ExtendedDoubleEvaluator().evaluate(amount);
                totalamount.add(damount);
                sumamount = 0;
                Log.i("totalam", String.valueOf(totalamount));
                for (Double d : totalamount)
                    sumamount += d;

                String discount = df.format(damount) + "*" + product_ArrayList.get(i).getDisperc() + "/" + "100";
                Double ddiscount = new ExtendedDoubleEvaluator().evaluate(discount);
                totaldiscount.add(ddiscount);
                sumdiscount = 0;
                for (Double d : totaldiscount)
                    sumdiscount += d;

                String cgst = df.format(damount) + "*" + product_ArrayList.get(i).getCgstper() + "/" + "100";
                Double dcgst = new ExtendedDoubleEvaluator().evaluate(cgst);
                totalcgst.add(dcgst);
                sumcgst = 0;
                for (Double d : totalcgst)
                    sumcgst += d;

                String sgst = df.format(damount) + "*" + product_ArrayList.get(i).getSgstper() + "/" + "100";
                Double dsgst = new ExtendedDoubleEvaluator().evaluate(sgst);
                totalsgst.add(dsgst);
                sumsgst = 0;
                for (Double d : totalsgst)
                    sumsgst += d;

                String igst = df.format(damount) + "*" + product_ArrayList.get(i).getIgstper() + "/" + "100";
                Double digst = new ExtendedDoubleEvaluator().evaluate(igst);
                totaligst.add(digst);
                sumigst = 0;
                for (Double d : totaligst)
                    sumigst += d;

                String cess = df.format(damount) + "*" + product_ArrayList.get(i).getCessper() + "/" + "100";
                Double dcess = new ExtendedDoubleEvaluator().evaluate(cess);
                totalcess.add(dcess);
                sumcess = 0;
                for (Double d : totalcess)
                    sumcess += d;

                Double tax = dcgst + dsgst + digst + dcess;
                totaltax.add(tax);
                sumtax = 0;
                for (Double d : totaltax)
                    sumtax += d;

                final Double total = damount + tax - ddiscount;
                totalprice.add(total);
                sumtotal = 0;
                for (Double d : totalprice)
                    sumtotal += d;

                String quantity = product_ArrayList.get(i).getQuantity();
                totalquantity.add(Double.valueOf(quantity));
                sumquantity = 0;
                for (Double d : totalquantity)
                    sumquantity += d;


                PdfPCell cell11 = new PdfPCell(new Paragraph("" + (i + 1)));
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell11);
                PdfPCell cell22 = new PdfPCell(new Paragraph(product_ArrayList.get(i).getProduct_name()));
                cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell22);
                PdfPCell cell33 = new PdfPCell(new Paragraph(product_ArrayList.get(i).getHsn()));
                cell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell33);
                PdfPCell cell44 = new PdfPCell(new Paragraph(product_ArrayList.get(i).getQuantity() + product_ArrayList.get(i).getUnit()));
                cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell44);
                PdfPCell cell55 = new PdfPCell(new Paragraph(product_ArrayList.get(i).getUnit_price() + " $"));
                cell55.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell55);
                PdfPCell cell66 = new PdfPCell(new Paragraph(df.format(damount) + " $"));
                cell66.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell66);

                PdfPCell cell77 = new PdfPCell(new Paragraph(df.format(total) + " $"));
                cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell77);

            }

            document.add(table);

            PdfPTable table1 = new PdfPTable(7);
            table1.setWidthPercentage(100);

//            Font Ftotal = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Paragraph p1 = new Paragraph("Totals", FNT);
            PdfPCell cell21 = new PdfPCell(p1);
            cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell21.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//            cell21.setVerticalAlignment(Element.ALIGN_MIDDLE);
            PdfPCell cell22 = new PdfPCell(new Paragraph("", FNT));
            cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell26 = new PdfPCell(new Paragraph("", FNT));
            cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell23 = new PdfPCell(new Paragraph(df.format(sumquantity), FNT));
            cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell24 = new PdfPCell(new Paragraph("", FNT));
            cell24.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell25 = new PdfPCell(new Paragraph(df.format(sumamount) + " $", FNT));
            cell25.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell212 = new PdfPCell(new Paragraph(df.format(sumtotal) + " $", FNT));
            cell212.setHorizontalAlignment(Element.ALIGN_CENTER);

            table1.addCell(cell21);
            table1.addCell(cell22);
            table1.addCell(cell26);
            table1.addCell(cell23);
            table1.addCell(cell24);
            table1.addCell(cell25);

            table1.addCell(cell212);

            document.add(table1);



            Font FNT1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK);
            PdfPTable table3 = new PdfPTable(2);
            table3.setWidthPercentage(40);
//            table3.setSpacingAfter(0f);
            table3.setSpacingBefore(10f);
           table3.setHorizontalAlignment(Element.ALIGN_RIGHT);
           PdfPCell cell31 = new PdfPCell(new Paragraph("SubTotal:",FNT1));
//           cell31.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell31);
            PdfPCell cell32 = new PdfPCell(new Paragraph(df.format(sumamount)+" $",FNT1));
//            cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell32);
            PdfPCell cell33 = new PdfPCell(new Paragraph("CGST:",FNT));
//            cell33.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell33);
            PdfPCell cell34 = new PdfPCell(new Paragraph(df.format(sumcgst)+" $",FNT));
//            cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell34);
            PdfPCell cell35 = new PdfPCell(new Paragraph("SGST:",FNT));
//            cell35.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell35);
            PdfPCell cell36 = new PdfPCell(new Paragraph(df.format(sumsgst)+" $",FNT));
//            cell36.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell36);
            PdfPCell cell37 = new PdfPCell(new Paragraph("IGST:",FNT));
//            cell37.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell37);
            PdfPCell cell38 = new PdfPCell(new Paragraph(df.format(sumigst)+" $",FNT));
//            cell38.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell38);
            PdfPCell cell39 = new PdfPCell(new Paragraph("CESS:",FNT));
//            cell39.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell39);
            PdfPCell cell40 = new PdfPCell(new Paragraph(df.format(sumcess)+" $",FNT));
//            cell40.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell40);
            PdfPCell cell41 = new PdfPCell(new Paragraph("DISCOUNT:",FNT));
//            cell41.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell41);
            PdfPCell cell42 = new PdfPCell(new Paragraph(df.format(sumdiscount)+" $",FNT));
//            cell42.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell42);
            PdfPCell cell43 = new PdfPCell(new Paragraph("Total Tax:",FNT));
//            cell43.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell43);
            PdfPCell cell44 = new PdfPCell(new Paragraph(df.format(sumtax)+" $",FNT));
//            cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell44);
            PdfPCell cell45 = new PdfPCell(new Paragraph("Total Amount:",FNT1));
//            cell45.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell45);
            PdfPCell cell46 = new PdfPCell(new Paragraph(df.format(sumtotal)+" $",FNT1));
//            cell46.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell46);
            document.add(table3);

            document.close();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            document.close();
        }


        viewPdf(nameofpdf + ".pdf", getResources().getString(R.string.app_name));

    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);
        pdfView.fromUri(path)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }

    private void getAllWidget() {
        pdfView = (PDFView) findViewById(R.id.pdfview);
        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        titleofactivity = (TextView) findViewById(R.id.titleofactivity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

