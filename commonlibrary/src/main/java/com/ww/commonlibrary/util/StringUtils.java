package com.ww.commonlibrary.util;

import android.content.Context;
import android.widget.TextView;

import java.util.Random;
import java.util.zip.CRC32;

import io.reactivex.internal.operators.flowable.FlowableTake;

public class StringUtils {

    public static String trimStr(String s) {
        return s.trim();
    }


    public static String getVideoContentApi(String videoid) {
        String VIDEO_HOST = "http://ib.365yg.com";
        String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
        String r = getRandom();
        String s = String.format(VIDEO_URL, videoid, r);
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        String crcString = crc32.getValue() + "";
        String url = VIDEO_HOST + s + "&s=" + crcString;
        return url;
    }

    public static String getMarqueeText(String text, int textSize) {
        float sWidth = ScreenUtils.getScreenWidth();
        StringBuilder stringBuilder = new StringBuilder();
        int scale = (int) Math.ceil(sWidth / (textSize * text.length()));
        if (scale > 1) {
            for (int i = 0; i < scale; i++) {
                stringBuilder.append(text);
                stringBuilder.append("  ");
            }
            return stringBuilder.toString();
        }
        return text;
    }

    public static String[] getHiddenKeyword(String text) {
        if (text == null) {
            return new String[]{""};
        }
        String[] strings = text.split(",");

        if (strings.length > 1) {
            return strings;
        }
        strings[0] = text;
        return strings;
    }

    private static String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public static String timeToMS(int time) {
        String hStr;
        int h = time / 60 / 60;
        int m = (time - h * 60 * 60) / 60;
        int s = (time - h * 60) % 60;
        if (h > 0 && h < 10) {
            hStr = "0" + h + ":";
        } else {
            hStr = "";
        }
        return hStr + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    public static String getCountStr(int count) {
        if (count / 10000 == 0) {
            return String.valueOf(count);
        } else {
            return count / 10000 + "万";
        }
    }

}