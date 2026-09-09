package pl.fachowo.verification;

public final class NipValidator {

    private static final int[] WEIGHTS = {6, 5, 7, 2, 3, 4, 5, 6, 7};

    private NipValidator() {
    }

    public static String normalize(String nip) {
        if (nip == null) {
            return "";
        }
        return nip.replaceAll("\\D", "");
    }

    public static boolean isValid(String nip) {
        String digits = normalize(nip);
        if (digits.length() != 10) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * WEIGHTS[i];
        }
        int checksum = sum % 11;
        if (checksum == 10) {
            return false;
        }
        return checksum == Character.getNumericValue(digits.charAt(9));
    }
}
