package util;

public class Util {
    
    public static Integer stringToInteger(String s) {
        if (s == null || s.isEmpty() || s.equals("null")) return null;
        return Integer.parseInt(s);
    }
    
    public static Integer[] stringToInteger(String[] s) {
        if (s == null) return null;
        Integer[] retorno = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            retorno[i] = stringToInteger(s[i]);
        }
        return retorno;
    }
    
}
