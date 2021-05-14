package com.uodreams.tmgutils.utils;

import android.content.Context;
import android.widget.Toast;

import com.uodreams.tmgutils.model.Member;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Misc {
    public static long getTimestamp() {
        return (long) Math.floor(System.currentTimeMillis() / 1000);
    }

    /*public static long getTimestamp(final String dateString) {
        long result;

        try {
            final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
            final Date start = format.parse("1970/1/1 00:00");
            final Date date = format.parse(dateString);
            result = (long) Math.floor((date.getTime() - start.getTime()) / 1000);
        } catch (java.text.ParseException pe) {
            return 0;
        } catch (Exception e) {
            return 0;
        }

        return result;
    }*/
    private static Date getDate(final long timestamp) {
        return new Date(timestamp * 1000);
    }

    /*public static String getDateStr(final long timestamp, final String format) {
        final Date date = getDate(timestamp);
        final Format formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }*/

    public static String getWelcomeText(final Member member) {
        //final String strRank = getRankName(member.rank);
        return "Welcome, " + member.alias + "!";
    }

    public static String getRankName(final int rank) {
        String strRank = "";
        switch (rank) {
            case 0:
                strRank = "Member";
                break;
            case 1:
                strRank = "Emissary";
                break;
            case 2:
                strRank = "GM/Co-GM";
                break;
            case 3:
                strRank = "Developer";
                break;
        }
        return strRank;
    }

    public static void restToast(final Context context, final String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}