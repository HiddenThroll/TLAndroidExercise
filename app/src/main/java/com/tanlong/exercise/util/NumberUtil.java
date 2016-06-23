package com.tanlong.exercise.util;

import android.content.Context;


import com.tanlong.exercise.R;

import java.text.DecimalFormat;

/**
 * 数字相关工具
 * Created by Administrator on 2015/12/11.
 */
public class NumberUtil {

    /**
     * 保留2位小数
     * @param num -- 输入值
     * @return
     */
    public static String keepTwoDecimal(double num){
//        BigDecimal bigDecimal = new BigDecimal(num);
//        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (num == 0) {
            return "0.00";
        } else {
            if (num < 1) {
                return "0" + decimalFormat.format(num);
            } else {
                return decimalFormat.format(num);
            }
        }
    }

    /**
     * 保留2位小数
     * @param num -- 输入值
     * @return
     */
    public static String keepTwoDecimal(String num){
//        BigDecimal bigDecimal = new BigDecimal(num);
//        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double number = Double.valueOf(num);
        if (number == 0) {
            return "0.00";
        } else {
            if (number < 1) {
                return "0" + decimalFormat.format(number);
            } else {
                return decimalFormat.format(number);
            }
        }
    }

    /**
     * 获得区间或单一价格
     * @param context
     * @param price
     * @return
     */
    public static String getRegionPrice(Context context, String price) {
        String[] priceItems = price.split(",");
        if (priceItems.length == 1) {
            return context.getString(R.string.price_style, keepTwoDecimal(priceItems[0]));
        } else if (priceItems.length == 2) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("￥").append(keepTwoDecimal(priceItems[0])).append("-").append(keepTwoDecimal(priceItems[1]));
            return buffer.toString();
        }
        return "";
    }
}
