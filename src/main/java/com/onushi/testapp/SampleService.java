package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThisMethod;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class SampleService {
    public final int testField = 5;

    public int add(int x, int y) {
        return x + y;
    }

    public int negate(int x) {
        return -x;
    }

    public int returnZero() {
        return 0;
    }

    public float addFloats(float x, float y) {
        return x + y;
    }

    public Integer addStrings(String a, String b) {
        return Integer.parseInt(a) + Integer.parseInt(b);
    }

    public boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    public String toYYYY_MM_DD_T_HH_MM_SS_Z(Date date, Date date1) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public int testTypes(short s, byte b, int a, boolean b1, char c, double d) {
        return 5;
    }

    @RecordTestForThisMethod
    public Date minDate(Date date1, Date date2) {
        return date1.before(date2) ? date1 : date2;
    }
}
