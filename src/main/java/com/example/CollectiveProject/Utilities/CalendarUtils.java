package com.example.CollectiveProject.Utilities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@NoArgsConstructor
public class CalendarUtils {
    public Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        return formatter.parse(date);
    }

    public String convertDateToString(Date date) {
        String pattern = "dd-MM-yyyy";
        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(date);
    }
}
