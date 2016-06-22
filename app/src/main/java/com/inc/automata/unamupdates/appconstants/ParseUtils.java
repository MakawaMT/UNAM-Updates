package com.inc.automata.unamupdates.appconstants;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Detox on 02-Oct-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class ParseUtils {
    private static String TAG = ParseUtils.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        //empty client and parse key show error
        if (TextUtils.isEmpty(AppConst.PARSE_APPLICATION_ID) || TextUtils.isEmpty(AppConst.PARSE_CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    //register parse
    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, AppConst.PARSE_APPLICATION_ID, AppConst.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground(AppConst.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");
            }
        });
    }

    //subscribe via email
    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("email", email);

        installation.saveInBackground();
    }

    //subscribe to channel
    public static void subscribeToChannel(String channelName) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("channels", channelName);

        installation.saveInBackground();
    }
}
