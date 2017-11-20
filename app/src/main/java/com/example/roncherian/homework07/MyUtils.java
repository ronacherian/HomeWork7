package com.example.roncherian.homework07;

import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by roncherian on 19/11/17.
 */
public class MyUtils {
    public static boolean validateEmail(String email) {

        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean validateDOB(String dob) {

        if (dob == null) {
            return false;
        } else {
            Date today = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.YEAR, -13);
            Date thirteenYearsBack = cal.getTime();

            try {
                Date dobDate = new SimpleDateFormat("MM/dd/yyyy").parse(dob);

                if (dobDate.compareTo(thirteenYearsBack) <= 0) {
                    return true;
                }
            } catch (ParseException e) {
                return false;
            }

            return false;
        }
    }


}