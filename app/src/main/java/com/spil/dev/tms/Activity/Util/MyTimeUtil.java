package com.spil.dev.tms.Activity.Util;


import java.util.concurrent.TimeUnit;

public class MyTimeUtil {
    public static String minToStringDuration(int min) {
        long hari = TimeUnit.MINUTES.toDays(min);
        long jam = TimeUnit.MINUTES.toHours(min - TimeUnit.DAYS.toMinutes(hari));
        long menit = min - (TimeUnit.DAYS.toMinutes(hari) + TimeUnit.HOURS.toMinutes(jam));

        String sHari = hari == 0 ? "" : " " + hari + " Hari";
        String sJam = jam == 0 ? "" : " " + jam + " Jam";
        String sMenit = " " + menit + " Menit";
        return sHari + sJam + sMenit;
    }

    public static void main(String[] args) {
        System.out.println(minToStringDuration(3114));
    }

}
