package utilities;

import com.example.shoppingcartapplication.enums.Gender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    /**
     * ^ - start of string
     * $ - end of string.
     *
     * @param email
     * @return boolean
     */
    public static boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    /**
     * allows on male and female gender
     *
     * @param gender
     * @return boolean
     */
    public static boolean validateGender(String gender) {
        boolean flag = false;

        Gender genderEnum;
        try {
            genderEnum = Gender.valueOf(gender.toUpperCase());
            flag = true;
        } catch (IllegalArgumentException error) {
            flag = false;
        }

        return flag;
    }

    /**
     * Allows password only greater than 5 in length
     *
     * @param password
     * @return boolean
     */
    public static boolean validatePassword(String password) {
        if (password.length() > 5) return true;
        return false;
    }

    /**
     * ^ - start of string
     * [0] or [\+]?[234] - start of a number
     * 10 - 12 digits
     * $ - end of string.
     *
     * @param phoneNumber
     * @return boolean
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^[0]\\d{10}$|^[\\+]?[234]\\d{12}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * ^ - start of string
     * [a-zA-Z]{4,} - 4 or more ASCII letters
     * (?: [a-zA-Z]+){0,2} - 0 to 2 occurrences of a space followed with one or more ASCII letters
     * $ - end of string.
     *
     * @param fullName
     * @return boolean
     */
    public static boolean validateFullName(String fullName) {
        String regex = "^[a-zA-Z]{3,}(?: [a-zA-Z]+){1,2}$";
        return fullName.matches(regex);
    }

    /**
     * validation for registration
     *
     * @param phoneNumber
     * @param email
     * @param fullName
     * @param gender
     * @param password
     * @return boolean
     */
    public static String validateRegistration(String email, String gender, String password,
                                              String phoneNumber, String fullName) {

        String message = "Successful validation";

        if (!validateEmail(email))
            message = "Invalid Email Entered";

        if (!validatePassword(password))
            message = "Enter Password greater than 5 characters length";

        if (!validateFullName(fullName))
            message = "Enter your full name(Surname and Firstname)";

        if (!validateGender(gender))
            message = "Choose a gender(Male or Female)";

        if (!validatePhoneNumber(phoneNumber))
            message = "Enter the phone number format +(234), 234 or 0";

        return message;

    }
}
