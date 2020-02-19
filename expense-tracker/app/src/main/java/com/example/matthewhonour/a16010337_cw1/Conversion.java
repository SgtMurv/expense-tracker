package com.example.matthewhonour.a16010337_cw1;

import android.widget.Switch;

public class Conversion
{
    private static final int TRUE  = 1;
    private static final int FALSE = 0;

    private static final boolean INITIALIZE = false;
    private static final boolean YES        = true;
    private static final boolean NO         = false;

    private static final boolean CHECKED    = true;
    private static final boolean UNCHECKED  = false;

    public Conversion(){}

    public static boolean convertIntegerToBoolean(int integerToConvert)
    {
        boolean convertedValue = INITIALIZE;
        if(integerToConvert == TRUE)
        {
            convertedValue = YES;
        }
        else if(integerToConvert == FALSE)
        {
            convertedValue = NO;
        }
        return convertedValue;
    }

    public static int convertBooleanToInteger(boolean boolToConvert)
    {
        int convertedValue;
        if(boolToConvert == YES)
        {
            convertedValue = TRUE;
        }
        else
        {
            convertedValue = FALSE;
        }
        return convertedValue;
    }

    public static boolean convertSwitchValueToBoolean(Switch switchView)
    {
        boolean convertedValue = UNCHECKED;
        if(switchView.isChecked())
        {
            convertedValue = CHECKED;
        }
        return convertedValue;
    }
}
