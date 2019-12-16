package com.ww.commonlibrary.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.widget.BackgroundImageSpan;
import com.ww.commonlibrary.view.widget.CenterAlignImageSpan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            scale = scale * 2;
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

    /**
     * str出现的次数
     */
    public static int strCount(String st, String target) {
        int count = 0;
        while (st.indexOf(target) >= 0) {
            st = st.substring(st.indexOf(target) + target.length());
            count++;
        }
        return count;
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
            return String.format("%.1f万", (float) count / 10000f);
        }
    }

    public static String getLoadHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
            element.attr("alt", element.attr("src"));
            element.attr("src", "file:///android_asset/default_todaylife_img.jpg");
        }
        return doc.toString();
    }

}
