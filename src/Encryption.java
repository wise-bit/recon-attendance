public class Encryption {

    private static String temp = "";

    public static String encrypt(String password) {

        password = "SaLt" + password + "3.141592";
        char[] arr = password.toCharArray();
        int sum = 0;
        String encripString = "";
        for (char c : arr) {
            encripString += Integer.toString((int) c);
        }

        String encrLevel2 = "";
        for (int i = 0; i < encripString.length() - 2; i++) {
            int tempSum = encripString.charAt(i) + encripString.charAt(i+1) + encripString.charAt(i+2) - 144;
            encrLevel2 += tempSum;
        }

        return shortener(encrLevel2);

    }

    public static String decrypt(String encodedString) {
        return "This encryption is too strong!";
    }

    public static boolean compare (String word1, String word2) {
        return word1.equals(word2);
    }

    public static String shortener(String s) {
        if (s.length() == 10) {
            return s;
        } else if (s.length() > 10) {
            String encr = "";
            for (int i = 0; i < s.length() - 2; i+=3) {
                int tempSum = s.charAt(i) + s.charAt(i+1) + s.charAt(i+2) - 144;
                encr += tempSum;
            }
            return shortener(encr);
        } else {
            return shortener(s + "" + s);
        }
    }

}
