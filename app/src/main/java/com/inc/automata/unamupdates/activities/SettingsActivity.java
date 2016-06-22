package com.inc.automata.unamupdates.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.inc.automata.unamupdates.R;
import com.inc.automata.unamupdates.appconstants.AppConst;

/**
 * Created by Detox on 14-Aug-15.
 */
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG= SettingsActivity.class.getSimpleName();

    Toolbar toolBar;
    ImageButton btnSave,btnCancel;
    EditText editStudentNumber,editRegistrationYear;
    Spinner spinnerCourseCode;
    String studentNumber,registrationYear,studentCourse,callingActivity;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

       assignResources();//assign xml resources etc
        //get values into edit texts
        try{
           checkStudentValues();
        }catch (Exception ex){
            Toast.makeText(this,"Cannot read saved student values: "+ex.toString(),Toast.LENGTH_LONG).show();
        }

        callingActivity=getIntent().getStringExtra(AppConst.ACTIVITY_NAME);
    }
    private void assignResources(){

        toolBar=(Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolBar);//set toolbar
        toolBar.setLogo(R.mipmap.ic_launcher);//set logo
        getSupportActionBar().setTitle(this.getClass().getSimpleName()); //set name on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get image buttons,set onclick listeners
        btnSave=(ImageButton) findViewById(R.id.imgBtnSave); btnSave.setOnClickListener(this);
        btnCancel=(ImageButton) findViewById(R.id.imgBtnCancel);btnCancel.setOnClickListener(this);

        //edit texts and spinner
        editRegistrationYear=(EditText)findViewById(R.id.editRegistrationYear);
        editStudentNumber=(EditText)findViewById(R.id.editStudentNumber);
        spinnerCourseCode=(Spinner)findViewById(R.id.spinnerCourseCode);

        adapter=ArrayAdapter.createFromResource(this,R.array.course,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseCode.setAdapter(adapter);

    }

    private boolean checkStudentValues() throws Exception{
        SharedPreferences prefValues = getSharedPreferences(AppConst.SHAREDPREFERENCES, MODE_PRIVATE);
        //read shared preferences
        studentNumber=prefValues.getString(AppConst.STUDENTNUMBER,null);
        registrationYear=prefValues.getString(AppConst.REGISTRATIONYEAR,null);
        studentCourse=String.valueOf(prefValues.getString(AppConst.STUDENTCOURSE, null));

        if(studentNumber != null) editStudentNumber.setText(studentNumber);
        if(registrationYear != null) editRegistrationYear.setText(registrationYear);

        if(studentCourse != null){
            //get location of item in spinner
         int spinnerPosition = -1;
            for(int i =0;i<spinnerCourseCode.getCount();i++){
                if(spinnerCourseCode.getItemAtPosition(i).toString().equalsIgnoreCase(studentCourse)){
                    spinnerPosition = i;
                    break;
                }
            }//end for
           spinnerCourseCode.setSelection(spinnerPosition);
        }
        return studentNumber !=null && registrationYear !=null && studentCourse !=null;
    }

    private void saveStudentValues() throws Exception{
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.SHAREDPREFERENCES,MODE_PRIVATE).edit();

        editor.putString(AppConst.STUDENTNUMBER,editStudentNumber.getText().toString());
        editor.putString(AppConst.REGISTRATIONYEAR, editRegistrationYear.getText().toString());
        editor.putString(AppConst.STUDENTCOURSE, String.valueOf( spinnerCourseCode.getSelectedItem().toString()));

        //if not succesful throw exception
        if(!editor.commit()) throw new Exception();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            //save values
            case R.id.imgBtnSave:
                try {
                    saveStudentValues();
                }catch (Exception ex){
                    Toast.makeText(this,"Error saving: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                break;
            //cancel save
            case R.id.imgBtnCancel:
                //go back
                onBackPressed();
                break;
             default:
              break;

        }
    }
}
