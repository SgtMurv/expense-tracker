package com.example.matthewhonour.a16010337_cw1;

import java.text.DateFormat;
import java.util.Date;

public class UserInputChecks
{
    final static int        MINIMUM_NO_OF_CHARS = 20;
    final static DateFormat DATE_FORMAT         = DateFormat.getDateInstance();

    //checks the date the user has input
    public static boolean checkDateInput(String dateInput)
    {
        boolean retValue;
        try
        {
            DATE_FORMAT.format(Date.parse(dateInput));
            retValue = true;
        }catch (Exception e)
        {
            retValue = false;
        }
        return retValue;
    }
    //checks the string entered into the summary edit text
    public static boolean checkStringInput(String stringInput)
    {
        boolean retValue = true;
        if(stringInput.length()<MINIMUM_NO_OF_CHARS)
        {
            retValue = false;
        }
        return retValue;
    }
}
