package com.interview.lib;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public static int timeOfDayInt(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 11)
            return 0;
        else if (hour >= 11 && hour < 4)
            return 1;
        else if (hour >= 4 && hour < 9)
            return 2;
        else
            return 3;
    }

    public static String timeOfDayString(){
        switch (DateTime.timeOfDayInt()){
            case 0:
                return "Breakfast";
            case 1:
                return "Lunch";
            case 2:
                return "Dinner";
            default:
                return "Late Night";
        }
    }
}
