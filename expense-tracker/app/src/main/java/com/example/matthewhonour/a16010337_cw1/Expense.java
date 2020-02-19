package com.example.matthewhonour.a16010337_cw1;

import android.media.Image;

public class Expense
{
    private static final boolean EXPENSE_HAS_NOT_BEEN_PAID  =false;
    private static final boolean NO_VAT                     =false;
    private static final String  EMPTY_DATE                 = "";
    private static final String  EMPTY_SUMMARY              ="";
    private static final float   EMPTY_FLOAT                = 0.0f;
    private static final int     INVALID_ID                 = 0;

    //attributes of an Expense Object:
    private int expenseID                   = INVALID_ID;
    private boolean hasExpenseBeenPaid      = EXPENSE_HAS_NOT_BEEN_PAID;
    private String dateOfPurchase           = EMPTY_DATE;
    private String dateAddedToSystem        = EMPTY_DATE;
    private String datePaid                 = EMPTY_DATE;
    private String summaryOfExpense         = EMPTY_SUMMARY;
    private boolean doesTotalCostIncludeVAT = NO_VAT;
    private float vatComponent              = EMPTY_FLOAT;
    private float totalCost                 = EMPTY_FLOAT;
    private float itemValue                 = EMPTY_FLOAT;

    //constructor for setting the expense data:
    public Expense(
             boolean hasExpenseBeenPaid,
             String dateOfPurchase,
             String dateAddedToSystem,
             String datePaid,
             String summaryOfExpense,
             boolean doesTotalCostIncludeVAT,
             float vatComponent,
             float totalCost,
             float itemValue)
    {
        setHasExpenseBeenPaid(hasExpenseBeenPaid);
        setDateOfPurchase(dateOfPurchase);
        setDateAddedToSystem(dateAddedToSystem);
        setDatePaid(datePaid);
        setSummaryOfExpense(summaryOfExpense);
        setDoesTotalCostIncludeVAT(doesTotalCostIncludeVAT);
        setVatComponent(vatComponent);
        setTotalCost(totalCost);
        setItemValue(itemValue);
    }
    public void setExpenseID(int id)
    {
      this.expenseID = id;
    }

    public int getExpenseID(){return this.expenseID;}

    public void setHasExpenseBeenPaid(boolean hasExpenseBeenPaid)
    {
        this.hasExpenseBeenPaid = hasExpenseBeenPaid;
    }

    public boolean getHasExpenseBeenPaid()
    {
        return this.hasExpenseBeenPaid;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getDateOfPurchase()
    {
        return this.dateOfPurchase;
    }

    public void setDateAddedToSystem(String dateAddedToSystem)
    {
        this.dateAddedToSystem = dateAddedToSystem;
    }

    public String getDateAddedToSystem()
    {
        return this.dateAddedToSystem;
    }

    public void setDatePaid(String datePaid)
    {
        this.datePaid = datePaid;
    }

    public String getDatePaid()
    {
        return this.datePaid;
    }

    public void setSummaryOfExpense(String summaryOfExpense)
    {
        this.summaryOfExpense = summaryOfExpense;
    }

    public String getSummaryOfExpense()
    {
        return this.summaryOfExpense;
    }


    public void setDoesTotalCostIncludeVAT(boolean thereVAT)
    {
        doesTotalCostIncludeVAT = thereVAT;
    }

    public boolean getDoesTotalCostIncludeVAT()
    {
        return this.doesTotalCostIncludeVAT;
    }


    public void setTotalCost(float totalCost)
    {
        this.totalCost = totalCost;
    }

    public float getTotalCost()
    {
        return this.totalCost;
    }

    public void setVatComponent(float vatComponent)
    {
        this.vatComponent = vatComponent;
    }

    public float getVatComponent()
    {
        return this.vatComponent;
    }

    public void setItemValue(float itemValue)
    {
        this.itemValue = itemValue;
    }

    public float getItemValue()
    {
        return this.itemValue;
    }
}
