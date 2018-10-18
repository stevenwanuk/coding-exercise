package com.sven.ce;

public class TestConstants
{
    private TestConstants()
    {

    }

    public static String USER_NAME_VALID = "Steven";
    public static String USER_NAME_EMPTY = "";
    public static String USER_NAME_WITH_NON_AlPHANUMERICAL_CHAR = "Steven!";
    public static String USER_NAME_WITH_WHITESPACE = "Steven!";

    public static String PASSWORD_VALID = "Abc1";
    public static String PASSWORD_WITH_LESS_THAN_4_CHARS = "A1B";
    public static String PASSWORD_WITH_NO_UPPERCASE_CHAR = "abc1";
    public static String PASSWORD_WITH_NO_NUMBER_CHAR = "Abcd";
    
    public static String DOB_WITH_VALID = "2018-01-02";
    public static String DOB_WITH_INVALID_FORMATE = "01/02/2018";
    
    public static String SSN_EMPTY = "";
    public static String SSN_VALID = "111111111";
}
