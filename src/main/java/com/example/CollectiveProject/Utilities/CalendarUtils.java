package com.example.CollectiveProject.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
    public Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        return formatter.parse(date);
    }

    public String convertDateToString(String date) {
        DateFormat df = new SimpleDateFormat(date);

        Date today = Calendar.getInstance()
                .getTime();

        String dateToString = df.format(today);

        return dateToString;
    }
}
