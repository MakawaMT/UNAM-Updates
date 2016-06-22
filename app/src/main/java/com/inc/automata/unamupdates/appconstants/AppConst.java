package com.inc.automata.unamupdates.appconstants;

/**
 * Created by Detox on 14-Aug-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */

//keeps app constants
public class AppConst {
    public static final String SHAREDPREFERENCES="UNAMupdates"; //shared preferences

    public static final String GENERALNEWSURL="http://www.omafano.com/unamupdates/getgeneral.php";//general news link

    public static final String COURSENEWSURL="http://www.omafano.com/unamupdates/getcourse.php";//course news link

    //for shared preferences tags
    public static final String STUDENTNUMBER="studentNumber";
    public static final String STUDENTCOURSE="studentCourse";
    public static final String REGISTRATIONYEAR="registrationYear";

    //for name value pairs
    public static final String POST_STUDENT="student_number";
    public static final String POST_REGISTRATION="registration_year";
    public static final String POST_COURSE="course";

    public static final String ACTIVITY_NAME="Activity_Name"; //for passing activity identifiers

    //for parse push notifications
    public static final String PARSE_CHANNEL = "_11BLAW";
    public static final String PARSE_APPLICATION_ID = "kMQNzOtejbVR4E5vO1o8sM8a74oBGUNrB4sq5y6q";
    public static final String PARSE_CLIENT_KEY = "2HHeib7E2AzzFzr8vrnXHorSqEU9NOl89sPEdM29";
    public static final int NOTIFICATION_ID = 101;
}
