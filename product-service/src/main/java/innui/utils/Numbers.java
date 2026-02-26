package innui.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class Numbers {
    public static double parseDouble(String input) {
        return NumberUtils.toDouble(input.replace(",", "."));
    }

    public static long parseLong(String input) {
        return NumberUtils.toLong(input);
    }

    public static int parseInt(String input) {
        return NumberUtils.toInt(input);
    }
}
