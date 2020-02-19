package com.example.matthewhonour.a16010337_cw1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpense extends AppCompatActivity
{
    //constant values:
    final static    boolean     NEW_EXPENSES_ARE_NOT_PAID           = false;
    final static    String      EMPTY_STRING                        ="";
    final static    float       DEFAULT_FLOAT_VALUE                 = 0f;
    final static    float       MAXIMUM_ITEM_VALUE                  = 999999.00f;
    final           String      ENGLISH_LANGUAGE                    ="English";
    final           Locale      englishLocale                       = new Locale(ENGLISH_LANGUAGE);
    final           int         SELECT_IMAGE_OF_RECEIPT             =1;

    ExpensesDatabaseManager expensesDatabaseManager;

    Date currentDate = Calendar.getInstance().getTime();


    EditText itemValue;
    EditText dateAdded;
    EditText dateOfPurchase;
    EditText dateClaimed;
    EditText summaryOfExpense;
    Switch VatSwitch;
    Switch PaidSwitch;
    EditText vatComponent;
    EditText TotalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);

        expensesDatabaseManager = new ExpensesDatabaseManager(this);

        //Initialising the view variables so we can make use of data stored in these views.
        itemValue           = (EditText)findViewById(R.id.etItemValue);
        dateOfPurchase      = (EditText)findViewById(R.id.etDatePaymentWasMade);
        dateAdded           = (EditText)findViewById(R.id.etDateAdded);
        dateClaimed         = (EditText)findViewById(R.id.etDateClaimed);
        summaryOfExpense    = (EditText)findViewById(R.id.etSummaryOfExpense);
        VatSwitch           = (Switch)findViewById(R.id.VAT_switch);
        PaidSwitch          = (Switch)findViewById(R.id.Paid_switch);
        vatComponent        = (EditText)findViewById(R.id.etVAT);
        TotalCost           = (EditText)findViewById(R.id.etTotalCost);

        itemValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //as the item value is edited the vat and total cost values are updated
                if(!itemValue.getText().toString().equals(""))
                {
                    //ensuring the item value is less than 999,999.00
                    if(Float.valueOf(itemValue.getText().toString()) > MAXIMUM_ITEM_VALUE)
                    {
                        itemValue.setError(getString(R.string.item_value_surpassed_its_maximum));
                        //formats the string to be a 2 dp float value for the english language.
                        itemValue.setText(String.format(englishLocale,"%.2f",MAXIMUM_ITEM_VALUE));
                    }
                    else
                    {
                        calculateVAT();
                    }
                }
                else
                {
                    //formats the strings to be a 2 dp float value for the english language.
                    vatComponent.setText(String.format(englishLocale,"%.2f",DEFAULT_FLOAT_VALUE));
                    TotalCost.setText(String.format(englishLocale,"%.2f",DEFAULT_FLOAT_VALUE));
                }
            }
        });

        setDefaultValues();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //this is run when the choose image intent is returned
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case (SELECT_IMAGE_OF_RECEIPT):
                    Toast toast = Toast.makeText(this,"You just selected an image from the gallery",Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
    }

    //********************************************************************************************************************************************************************************************************
    //Methods connected to controls in the activity.

    public void VAT_switch_onClick(View view)
    {
        //ensuring the user has entered an item_value before trying to calculate the VAT and Total cost
        if(itemValue.getText().toString().matches(EMPTY_STRING))
        {
            itemValue.setError(getString(R.string.empty_Item_Value_when_calculating_VAT_error));
            VatSwitch.setChecked(false);
        }
        else
        {
            this.calculateVAT();
        }
    }

    public void btnSaveExpense_onClick(View view)
    {
        //check the data the user has entered.
        if(itemValue.getText().toString().matches(EMPTY_STRING) && dateOfPurchase.getText().toString().matches(EMPTY_STRING))
        {
            itemValue.setError(getString(R.string.empty_field_error));
            dateOfPurchase.setError(getString(R.string.empty_field_error));
        }
        if(itemValue.getText().toString().matches(EMPTY_STRING))
        {
            itemValue.setError(getString(R.string.empty_field_error));
        }
        else if(dateOfPurchase.getText().toString().matches(EMPTY_STRING))
        {
            dateOfPurchase.setError(getString(R.string.empty_field_error));
        }
        else
        {
            //if the data that the user has entered into the activity is of the correct type to add to the database
            if(checkExpenseDataEnteredByTheUser())
            {
                //convert the value of the switches to boolean values so they can be stored in the database
                boolean isVatIncludedInItemValue         = Conversion.convertSwitchValueToBoolean(VatSwitch);
                boolean hasExpenseBeenPaid               = Conversion.convertSwitchValueToBoolean(PaidSwitch);

                //putting all of the data input by the user into a new Expense object

                Expense newExpense = new Expense(
                        hasExpenseBeenPaid,
                        dateOfPurchase.getText().toString(),
                        dateAdded.getText().toString(),
                        dateClaimed.getText().toString(),
                        summaryOfExpense.getText().toString(),
                        isVatIncludedInItemValue,
                        Float.valueOf(vatComponent.getText().toString()),
                        Float.valueOf(TotalCost.getText().toString()),
                        Float.valueOf(itemValue.getText().toString())
                );

                expensesDatabaseManager.addExpense(newExpense);

                setResult(RESULT_OK);

                finish();
            }
        }

    }
    public void btnChoosePhoto_onClick(View view)
    {
        //opens the gallery and enables the user to choose an image.
        Intent intent =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image of receipt."),SELECT_IMAGE_OF_RECEIPT);
    }
    public void calculateVAT()
    {
        //calculates VAT and Total Cost based on the VAT switch value
        //formats the strings to be a 2 dp float value for the english language.
        vatComponent.setText(String.format(englishLocale,"%.2f",DEFAULT_FLOAT_VALUE));
        float FLOAT_AmountPaid = Float.valueOf(itemValue.getText().toString());
        if(VatSwitch.isChecked())   //i.e. if the VAT is already included in the total cost
        {
            float VAT               = FLOAT_AmountPaid * 0.2f;
            vatComponent.setText(String.format(englishLocale,"%.2f",VAT));
            TotalCost.setText(String.format(englishLocale,"%.2f",FLOAT_AmountPaid));//as total cost will be the same as the amount paid id the VAT is included in the price.
        }
        else                        //i.e. VAT is not included in the total cost already
        {
            float VAT               = (FLOAT_AmountPaid/80) * 20f;
            vatComponent.setText(String.format(englishLocale,"%.2f",VAT));
            TotalCost.setText(String.format(englishLocale,"%.2f",FLOAT_AmountPaid + VAT));
        }
    }

    public void setDefaultValues()
    {
        //Setting the default value of the date added to be current date.
        String currentDateAsString = UserInputChecks.DATE_FORMAT.format(currentDate);
        this.dateAdded.setText(currentDateAsString);
        this.dateAdded.setEnabled(false);

        //Set defaults for date Paid for
        this.dateClaimed.setText(EMPTY_STRING);
        this.dateClaimed.setEnabled(false);

        //datePurchased default values
        this.dateOfPurchase.setText(EMPTY_STRING);

        //Paid_switch default
        this.PaidSwitch.setChecked(NEW_EXPENSES_ARE_NOT_PAID);
        this.PaidSwitch.setEnabled(false);

        //Set etVAT to be not enabled:
        String strVatComponent = String.format(englishLocale,"%.2f",DEFAULT_FLOAT_VALUE);
        this.vatComponent.setText(strVatComponent);
        this.vatComponent.setEnabled(false);

        //etTotalCost defaults
        String strTotalCost = String.format(englishLocale,"%.2f",DEFAULT_FLOAT_VALUE);
        this.TotalCost.setText(strTotalCost);
        this.TotalCost.setEnabled(false);

    }

    //input data checks:
    public boolean checkExpenseDataEnteredByTheUser()
    {
        //determines whether the user has entered data of a valid format.
        boolean hasTheUserEnteredTheCorrectData = true;
        if(!UserInputChecks.checkDateInput(dateOfPurchase.getText().toString()))
        {
            dateOfPurchase.setError(getString(R.string.date_format_error));
            hasTheUserEnteredTheCorrectData = false;
        }

        if(!UserInputChecks.checkStringInput(summaryOfExpense.getText().toString()))
        {
            summaryOfExpense.setError(getString(R.string.summary_length_error));
            hasTheUserEnteredTheCorrectData = false;
        }
        return hasTheUserEnteredTheCorrectData;
    }
}
