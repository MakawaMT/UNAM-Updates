package com.inc.automata.unamupdates.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.inc.automata.unamupdates.activities.MainActivity;
import com.inc.automata.unamupdates.appconstants.NewsAlertDialog;
import com.inc.automata.unamupdates.appconstants.NotificationUtils;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Detox on 02-Oct-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG=CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public  CustomPushReceiver(){
        super();
    }
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if(intent==null)return;

        try{
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.d(TAG,"Push received: "+json);
            parseIntent = intent;
            parsePushJson(context,json);

        }catch (JSONException e){
            Log.e(TAG,"onPushReceive JSON data: "+e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    private void parsePushJson(Context context,JSONObject json){
        try{
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data= json.getJSONObject("data");
            String title=data.getString("title");
            String message=data.getString("message");
            //set cheat variables since bundle is not being received in main activity's onNewIntent
            MainActivity.cheatMessage=message;
            MainActivity.cheatTitle=title;
            Log.d(TAG,"Cheat variable set");

            if(!isBackground){
                Intent resultIntent = new Intent(context, MainActivity.class);
                showNotificationMessage(context,title,message,resultIntent);//show notification
            }
        }catch (JSONException e){
            Log.e(TAG,"Push message json exception: "+e.getMessage());
        }
    }

    private void showNotificationMessage(Context context,String title,String message,Intent intent){
        notificationUtils = new NotificationUtils(context); //create notification object
        intent.putExtras(parseIntent.getExtras());//get extras
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//set intent flags
        notificationUtils.showNotificationMessage(title, message, intent);//show notification
    }
}
