package com.inc.automata.unamupdates.appconstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.inc.automata.unamupdates.R;

/**
 * Created by Detox on 24-Sep-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class NewsAlertDialog extends DialogFragment  {
    private final String TAG=this.getClass().getSimpleName();//TAG

    private String message;// to hold the message to be displayed
    private String title;// for alert dialog title

    public static NewsAlertDialog newInstance(String message, String title) {
        NewsAlertDialog f = new NewsAlertDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", title);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Resources res =getResources();
        // Title divider
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            //change color of divider
            titleDivider.setBackgroundColor(Color.parseColor("#e53935"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        message = getArguments().getString("message"); //get message
        title = getArguments().getString("title"); //get title

        final View scrollable = getActivity().getLayoutInflater().inflate(R.layout.layout_news_message,null);//set layout
        final TextView messageText=(TextView) scrollable.findViewById(R.id.news_message);//find message textview within layout

        messageText.setText(message);//set message
        messageText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //set scrollbars on message

        builder.setView(scrollable);//set view for alert

        builder.setIcon(R.mipmap.ic_launcher);//set app icon
        //set title and message
        builder.setTitle(Html.fromHtml("<font color='#e53935'>" + title + "</font>"));
        //set neutral button
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                //dismiss
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}
