package com.nikogalla.popularmovies.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nicola on 2016-03-22.
 */
public class TypeConverters {
    static SimpleDateFormat sdfISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static SimpleDateFormat sdfItalian = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    static SimpleDateFormat sdfCompactDateDataType = new SimpleDateFormat("yyyy-MM-dd");

    public static String fromDateToStringISO8601(Date date){
        String ISO8601Date = sdfISO8601.format(date);
        return ISO8601Date;
    }

    public static Date fromStringISO8601ToDate(String stringDate){
        Date convertedDate;
        try {
            convertedDate = sdfISO8601.parse(stringDate);
        } catch (Exception e) {
            convertedDate = null;
        }
        return convertedDate;
    }

    public static Date fromStringDateCompactToDate(String stringDate){
        Date convertedDate;
        try {
            convertedDate = sdfCompactDateDataType.parse(stringDate);
        } catch (Exception e) {
            convertedDate = null;
        }
        return convertedDate;
    }


    public static String fromStringISO8601ToItalianDateString(String ISO8601DateString){
        Date conversionDate;
        String convertedString;
        try {
            conversionDate = sdfISO8601.parse(ISO8601DateString);
            convertedString = sdfItalian.format(conversionDate);
        } catch (Exception e) {
            convertedString = null;
        }
        return convertedString;
    }

    public static String fromDateToItalianDateString(Date date){
        String convertedDate = sdfItalian.format(date);
        return convertedDate;
    }

    public static Boolean fromIntToBoolean(int number){
        Boolean b = (number != 0);
        return b;
    }


}
