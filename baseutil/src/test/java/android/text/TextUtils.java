package android.text;

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null ? true : str.equals("");
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
