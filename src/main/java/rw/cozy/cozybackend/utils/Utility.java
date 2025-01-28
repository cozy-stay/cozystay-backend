package rw.cozy.cozybackend.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rw.cozy.cozybackend.exceptions.BadRequestException;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class Utility {
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String NUM = "0123456789";
    private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random rng = new SecureRandom();

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    static char randomChar() {
        return ALPHANUM.charAt(rng.nextInt(ALPHANUM.length()));
    }

    static char randomNum() {
        return NUM.charAt(rng.nextInt(NUM.length()));
    }

    public static char randomStr() {
        return ALPHA.charAt(rng.nextInt(ALPHA.length()));
    }

    public static String randomUUID(int length, int spacing, char returnType) {
        StringBuilder sb = new StringBuilder();
        char spacerChar = '-';
        int spacer = 0;
        while (length > 0) {
            if (spacer == spacing && spacing > 0) {
                spacer++;
                sb.append(spacerChar);
            }
            length--;
            spacer++;

            switch (returnType) {
                case 'A':
                    sb.append(randomChar());
                    break;
                case 'N':
                    sb.append(randomNum());
                    break;
                case 'S':
                    sb.append(randomStr());
                    break;
                default:
                    logger.error("");
                    break;
            }
        }
        return sb.toString();
    }

    public static String randomUUIDWithStartLetter(int length, int spacing, char returnType, char startLetter,
            int numDigits) {
        StringBuilder sb = new StringBuilder();
        char spacerChar = '-';
        int spacer = 0;

        // Append the starting letter
        sb.append(startLetter);

        // Append current date in "dd-mm-yyyy" format separated with a hyphen
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        sb.append(formattedDate);

        length -= formattedDate.length(); // Reduce the remaining length

        // Append random characters or numbers based on returnType
        while (length > 0) {
            if (spacing > 0 && spacer == spacing) {
                spacer = 0;
                sb.append(spacerChar);
            }
            length--;
            spacer++;

            switch (returnType) {
                case 'A':
                    sb.append(randomChar());
                    break;
                case 'N':
                    sb.append(randomNum());
                    break;
                case 'S':
                    sb.append(randomStr());
                    break;
                default:
                    // Handle the default case or log an error
                    break;
            }
        }

        // If 'N' is chosen, append the specified number of digits
        if (returnType == 'N') {
            for (int i = 0; i < numDigits; i++) {
                sb.append(randomNum());
            }
        }

        return sb.toString();
    }

    public static boolean isCodeValid(String activationCode, String sentCode) {
        return activationCode.trim().equalsIgnoreCase(sentCode.trim());
    }

    public static void validateDob(Date dob) {
        if (dob == null)
            throw new BadRequestException("The date od birth should not be null");
        if (dob.toInstant().isAfter(Instant.now()))
            throw new BadRequestException("The date of birth should not be greater than now");
    }

    public static String generatedCode() {
        String caseCode = null;
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        String genCode = Utility.randomUUID(6, 0, 'N');
        caseCode = year + "-" + month + "-" + genCode;
        return caseCode;
        /**
         * TODO: To verify if the code is already in use and add the data location on
         * front
         *
         */
    }

}
