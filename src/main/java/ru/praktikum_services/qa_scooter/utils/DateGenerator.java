package ru.praktikum_services.qa_scooter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerator {
    public static  String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
