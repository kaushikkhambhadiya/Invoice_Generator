package com.conestogasem3.invoicegenerator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conestogasem3.invoicegenerator.ConstantValues.Constants;

import java.text.DecimalFormat;


public class ProductActivity extends Activity {

    EditText product_name, hsn, quantity, unit, unit_price, discount, cgst, sgst, igst, cess;
    TextView amount_price, taxable_amount, cgst_price, sgst_price, igst_price, cess_price, total_price, discount_price;
    Button saveproduct;
    Double sgstresult, cgstresult, igstresult, cessresult, result, lessresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final DecimalFormat df = new DecimalFormat("#.##");

        getAllWidgets();
        bindWidgetsWithEvent();
        checkForRequest();

        unit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isInputValid(unit_price)) {
                    String expresion = quantity.getText().toString() + "*" + s.toString();
                    result = new ExtendedDoubleEvaluator().evaluate(expresion);
                    amount_price.setText("$" + df.format(result));

                    if (result == null) {
                        result = 0.0;
                    }
                    if (lessresult == null) {
                        lessresult = 0.0;
                    }
                    if (sgstresult == null) {
                        sgstresult = 0.0;
                    }
                    if (igstresult == null) {
                        igstresult = 0.0;
                    }
                    if (cgstresult == null) {
                        cgstresult = 0.0;
                    }
                    if (cessresult == null) {
                        cessresult = 0.0;
                    }

                    Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                    taxable_amount.setText("$" + df.format(taxamount));

                    Double total = result + taxamount - lessresult;
                    total_price.setText("$" + df.format(total));


                } else {
                    amount_price.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isInputValid(discount)) {
                    String expresion = ((df.format(result)) + "*" + s.toString()) + "/" + "100";
                    lessresult = new ExtendedDoubleEvaluator().evaluate(expresion);
                    discount_price.setText("$" + df.format(lessresult));

                    if (result == null) {
                        result = 0.0;
                    }
                    if (lessresult == null) {
                        lessresult = 0.0;
                    }
                    if (sgstresult == null) {
                        sgstresult = 0.0;
                    }
                    if (igstresult == null) {
                        igstresult = 0.0;
                    }
                    if (cgstresult == null) {
                        cgstresult = 0.0;
                    }
                    if (cessresult == null) {
                        cessresult = 0.0;
                    }

                    Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                    taxable_amount.setText("$" + df.format(taxamount));

                    Double total = result + taxamount - lessresult;
                    total_price.setText("$" + df.format(total));
                } else {
                    discount_price.setText("0.0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cgst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isInputValid(cgst)) {
                    String expresion = ((df.format(result)) + "*" + s.toString()) + "/" + "100";
                    cgstresult = new ExtendedDoubleEvaluator().evaluate(expresion);
                    cgst_price.setText("$" + df.format(cgstresult));

                    if (result == null) {
                        result = 0.0;
                    }
                    if (lessresult == null) {
                        lessresult = 0.0;
                    }
                    if (sgstresult == null) {
                        sgstresult = 0.0;
                    }
                    if (igstresult == null) {
                        igstresult = 0.0;
                    }
                    if (cgstresult == null) {
                        cgstresult = 0.0;
                    }
                    if (cessresult == null) {
                        cessresult = 0.0;
                    }

                    Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                    taxable_amount.setText("$" + df.format(taxamount));

                    Double total = result + taxamount - lessresult;
                    total_price.setText("$" + df.format(total));
                } else {
                    cgst_price.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sgst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isInputValid(sgst)) {
                    String expresion = ((df.format(result)) + "*" + s.toString()) + "/" + "100";
                    sgstresult = new ExtendedDoubleEvaluator().evaluate(expresion);
                    sgst_price.setText("$" + df.format(sgstresult));

                    if (result == null) {
                        result = 0.0;
                    }
                    if (lessresult == null) {
                        lessresult = 0.0;
                    }
                    if (sgstresult == null) {
                        sgstresult = 0.0;
                    }
                    if (igstresult == null) {
                        igstresult = 0.0;
                    }
                    if (cgstresult == null) {
                        cgstresult = 0.0;
                    }
                    if (cessresult == null) {
                        cessresult = 0.0;
                    }

                    Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                    taxable_amount.setText("$" + df.format(taxamount));

                    Double total = result + taxamount - lessresult;
                    total_price.setText("$" + df.format(total));
                } else {
                    sgst_price.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        igst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isInputValid(igst)) {
                    String expresion = (s.toString() + "*" + (df.format(result))) + "/" + "100";
                    igstresult = new ExtendedDoubleEvaluator().evaluate(expresion);
                    igst_price.setText("$" + df.format(igstresult));

                    if (result == null) {
                        result = 0.0;
                    }
                    if (lessresult == null) {
                        lessresult = 0.0;
                    }
                    if (sgstresult == null) {
                        sgstresult = 0.0;
                    }
                    if (igstresult == null) {
                        igstresult = 0.0;
                    }
                    if (cgstresult == null) {
                        cgstresult = 0.0;
                    }
                    if (cessresult == null) {
                        cessresult = 0.0;
                    }

                    Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                    taxable_amount.setText("$" + df.format(taxamount));

                    Double total = result + taxamount - lessresult;
                    total_price.setText("$" + df.format(total));
                } else {
                    igst_price.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isInputValid(cess)) {
                    String expresion = ((df.format(result)) + "*" + s.toString()) + "/" + "100";
                    cessresult = new ExtendedDoubleEvaluator().evaluate(expresion);
                    cess_price.setText("$" + df.format(cessresult));

                } else {
                    cess_price.setText("0.0");
                }


                if (result == null) {
                    result = 0.0;
                }
                if (lessresult == null) {
                    lessresult = 0.0;
                }
                if (sgstresult == null) {
                    sgstresult = 0.0;
                }
                if (igstresult == null) {
                    igstresult = 0.0;
                }
                if (cgstresult == null) {
                    cgstresult = 0.0;
                }
                if (cessresult == null) {
                    cessresult = 0.0;
                }

                Double taxamount = cgstresult + sgstresult + igstresult + cessresult;
                taxable_amount.setText("$" + df.format(taxamount));

                Double total = result + taxamount - lessresult;
                total_price.setText("$" + df.format(total));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkForRequest() {
        String request = getIntent().getExtras().get(Constants.DML_TYPE).toString();
        if (request.equals(Constants.UPDATE)) {
            saveproduct.setText(Constants.UPDATE);
            product_name.setText(getIntent().getExtras().get(Constants.NAME).toString());
            hsn.setText(getIntent().getExtras().get(Constants.HSN).toString());
            quantity.setText(getIntent().getExtras().get(Constants.QUANTITY).toString());
            unit.setText(getIntent().getExtras().get(Constants.UNIT).toString());
            unit_price.setText(getIntent().getExtras().get(Constants.UNITPRICE).toString());
            discount.setText(getIntent().getExtras().get(Constants.DISCOUNT).toString());
            cgst.setText(getIntent().getExtras().get(Constants.CGST).toString());
            sgst.setText(getIntent().getExtras().get(Constants.SGST).toString());
            igst.setText(getIntent().getExtras().get(Constants.IGST).toString());
            cess.setText(getIntent().getExtras().get(Constants.CESS).toString());

            //set textview for update
            DecimalFormat df = new DecimalFormat("#.##");

            String amount = getIntent().getExtras().get(Constants.QUANTITY).toString() + "*" + getIntent().getExtras().get(Constants.UNITPRICE).toString();
            Double damount = new ExtendedDoubleEvaluator().evaluate(amount);
            amount_price.setText("$" + df.format(damount));

            String discount = df.format(damount) + "*" + getIntent().getExtras().get(Constants.DISCOUNT).toString() + "/" + "100";
            Double ddiscount = new ExtendedDoubleEvaluator().evaluate(discount);
            discount_price.setText("$" + df.format(ddiscount));

            String cgst = df.format(damount) + "*" + getIntent().getExtras().get(Constants.CGST).toString() + "/" + "100";
            Double dcgst = new ExtendedDoubleEvaluator().evaluate(cgst);
            cgst_price.setText("$" + df.format(dcgst));

            String sgst = df.format(damount) + "*" + getIntent().getExtras().get(Constants.SGST).toString() + "/" + "100";
            Double dsgst = new ExtendedDoubleEvaluator().evaluate(sgst);
            sgst_price.setText("$" + df.format(dsgst));

            String igst = df.format(damount) + "*" + getIntent().getExtras().get(Constants.IGST).toString() + "/" + "100";
            Double digst = new ExtendedDoubleEvaluator().evaluate(igst);
            igst_price.setText("$" + df.format(digst));

            String cess = df.format(damount) + "*" + getIntent().getExtras().get(Constants.CESS).toString() + "/" + "100";
            Double dcess = new ExtendedDoubleEvaluator().evaluate(cess);
            cess_price.setText("$" + df.format(dcess));

            Double dtaxable = dcgst + dsgst + digst + dcess;
            taxable_amount.setText("$" + df.format(dtaxable));

            Double total = damount + dtaxable - ddiscount;
            total_price.setText("$" + df.format(total));


        } else {
            saveproduct.setText(Constants.INSERT);
        }
    }

    private void bindWidgetsWithEvent() {
        saveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
    }

    private void getAllWidgets() {

        product_name = (EditText) findViewById(R.id.etproductname);
        hsn = (EditText) findViewById(R.id.ethsn);
        quantity = (EditText) findViewById(R.id.etquantity);
        unit = (EditText) findViewById(R.id.etunit);
        unit_price = (EditText) findViewById(R.id.etunitprice);
        discount = (EditText) findViewById(R.id.etdiscountperc);
        cgst = (EditText) findViewById(R.id.etcgst);
        sgst = (EditText) findViewById(R.id.etsgst);
        igst = (EditText) findViewById(R.id.etigst);
        cess = (EditText) findViewById(R.id.etcess);

        amount_price = (TextView) findViewById(R.id.amountprice);
        taxable_amount = (TextView) findViewById(R.id.taxableprice);
        cgst_price = (TextView) findViewById(R.id.cgstprice);
        sgst_price = (TextView) findViewById(R.id.sgstprice);
        igst_price = (TextView) findViewById(R.id.igstprice);
        cess_price = (TextView) findViewById(R.id.cessprice);
        total_price = (TextView) findViewById(R.id.totalprice);
        discount_price = (TextView) findViewById(R.id.discountprice);

        saveproduct = (Button) findViewById(R.id.btnDML);
    }

    private void onButtonClick() {
        if (product_name.getText().toString().equals("") || unit_price.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Add Both Fields", Toast.LENGTH_LONG).show();
            if (product_name.getText().toString().equals("")) {
                product_name.setError("Please Enter Item");
            } else if (unit_price.getText().toString().equals("")) {
                unit_price.setError("Please Enter Item");
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.NAME, product_name.getText().toString());
            intent.putExtra(Constants.HSN, hsn.getText().toString());
            intent.putExtra(Constants.QUANTITY, quantity.getText().toString());
            intent.putExtra(Constants.UNIT, unit.getText().toString());
            intent.putExtra(Constants.UNITPRICE, unit_price.getText().toString());
            intent.putExtra(Constants.DISCOUNT, discount.getText().toString());
            intent.putExtra(Constants.CGST, cgst.getText().toString());
            intent.putExtra(Constants.SGST, sgst.getText().toString());
            intent.putExtra(Constants.IGST, igst.getText().toString());
            intent.putExtra(Constants.CESS, cess.getText().toString());

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    protected boolean isInputValid(EditText etInput2) {
        if (etInput2.getText().toString().trim().length() < 1) {
            etInput2.setError("Please Enter Item");
            return false;
        } else {
            return true;
        }
    }
}