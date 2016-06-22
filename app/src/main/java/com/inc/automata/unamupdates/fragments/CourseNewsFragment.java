package com.inc.automata.unamupdates.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inc.automata.unamupdates.R;
import com.inc.automata.unamupdates.activities.MainActivity;
import com.inc.automata.unamupdates.appconstants.AppConst;
import com.inc.automata.unamupdates.appconstants.AppController;
import com.inc.automata.unamupdates.appconstants.ConnectionDetector;
import com.inc.automata.unamupdates.appconstants.NewsAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class CourseNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCourseNewsFragmentInteractionListener mListener;

    private String TAG= CourseNewsFragment.class.getSimpleName();
    ListView lvCourseNews;
    int count;
    ArrayList<HashMap<String,String>> newsItemList;

    private final String TAG_MODULE="module_name";private final String TAG_MESSAGE="message";private final String TAG_DATE="date_time";
    private final String TAG_STAFF="staff_username";

    JSONArray jsonArray;
    private SwipeRefreshLayout swipeRefreshLayout;
    ListAdapter adapter;

    private boolean isDataAvailable; //stores whether data is avaialble or not
    // TODO: Rename and change types and number of parameters
    public static CourseNewsFragment newInstance(String param1, String param2) {
        CourseNewsFragment fragment = new CourseNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CourseNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //allow all threading policies
        if(Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find swipe view and add listener and initial task to execute
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout_course);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

           swipeRefreshLayout.setRefreshing(true);
           GetCourseNews();
            }
        });

        //find list view and set item clicker
        lvCourseNews=(ListView) getView().findViewById(R.id.listViewCourse);
        lvCourseNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject singleNews;
                try{
                    singleNews= jsonArray.getJSONObject(position);
                    String module = singleNews.getString(TAG_MODULE);//get module
                    String message=singleNews.getString(TAG_MESSAGE);//get message
                    NewsAlertDialog nad = NewsAlertDialog.newInstance(message,module);
                    nad.show(getFragmentManager(),TAG);//show dialog

                }catch(JSONException jex){
                    singleNews=null;
                    Toast.makeText(getActivity().getApplicationContext(),"Error parsing news item",Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Error parsing news item: "+jex.getMessage());
                }catch (Exception ex){
                    Toast.makeText(getActivity().getApplicationContext(),"News item error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e(TAG,"News item error: "+ex.getMessage());
                }

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCourseNewsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCourseNewsFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        //call method to get updated news
        swipeRefreshLayout.setRefreshing(true);
        GetCourseNews();
    }

private void GetCourseNews(){

    isDataAvailable = new ConnectionDetector(getActivity().getApplicationContext()).isConnectingToInternet();

    //if no internet connection do not do anything
    if(!isDataAvailable){
        swipeRefreshLayout.setRefreshing(false);
        Log.e(TAG,"no internet connection. isDataAvailable: "+isDataAvailable);
        Toast.makeText(getActivity().getApplicationContext(),"Internet connection not available. Try again.",Toast.LENGTH_LONG).show();
        return; //do not continue processing
    }
    newsItemList = new ArrayList<HashMap<String, String>>();//initialise arraylist

    //get shared preferences values
    SharedPreferences prefValues = getActivity().getSharedPreferences(AppConst.SHAREDPREFERENCES, Context.MODE_PRIVATE);
    final String studentNumber=prefValues.getString(AppConst.STUDENTNUMBER, null);
    final String registrationYear=prefValues.getString(AppConst.REGISTRATIONYEAR,null);
    final String studentCourse=String.valueOf(prefValues.getString(AppConst.STUDENTCOURSE, null));

    StringRequest request = new StringRequest(Request.Method.POST, AppConst.COURSENEWSURL, new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                jsonArray =new JSONArray(s);
                for (int i =0;i<jsonArray.length();i++) {

                    JSONObject newsItemObject = jsonArray.getJSONObject(i);

                    String module = newsItemObject.getString(TAG_MODULE);
                    String message = newsItemObject.getString(TAG_MESSAGE);
                    String date_time = newsItemObject.getString(TAG_DATE);
                    date_time = date_time.substring(0, 10);//trim date
                    String staff = "by " + newsItemObject.getString(TAG_STAFF);

                    //store key pair values
                    HashMap<String, String> singleItem = new HashMap<String, String>();//hashmap to store key value pairs
                    singleItem.put(TAG_MODULE, module);//put module
                    singleItem.put(TAG_MESSAGE, message);//put message
                    singleItem.put(TAG_DATE, date_time);//put date time
                    singleItem.put(TAG_STAFF, staff);//put staff

                    newsItemList.add(singleItem);//add overall to list
                }
                if(newsItemList !=null){
                    //adapter for list for news
                    adapter = new SimpleAdapter(getActivity(), newsItemList, R.layout.list_item, new String[]{TAG_MODULE, TAG_DATE, TAG_STAFF}, new int[]{R.id.news_title, R.id.news_date, R.id.news_owner});
                    lvCourseNews.setAdapter(adapter);
                }else{
                    //inform user that JSON data was not retrieved
                    Toast.makeText(getActivity().getApplicationContext(), "Please check student details. No data returned", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Retrieving JSON data: ");
                }
            }catch (JSONException jex){
                Log.e(TAG,"JSON parsing: "+jex.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),"JSON Error while parsing data,check student details: "+jex.getMessage(),Toast.LENGTH_LONG).show();
                //delete list view over here in future
            }catch(NullPointerException npex){
                Log.e(TAG,"NPE: "+npex.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),"Null pointer exception while parsing data: "+npex.getMessage(),Toast.LENGTH_LONG).show();

            }catch (Exception ex){
                Log.e(TAG,"General exception parsing: "+ex.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
            }finally {
                swipeRefreshLayout.setRefreshing(false);//dismiss refresher animation
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(TAG, "JSON Volley error: " + volleyError.getMessage());
           Toast.makeText(getActivity().getApplicationContext(),"Volley could not complete request: "+volleyError.getMessage(),Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);//stop refreshing
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            //store details for POST method
            Map<String,String> params= new HashMap<String,String>();
            params.put(AppConst.POST_STUDENT,studentNumber);
            params.put(AppConst.POST_COURSE,studentCourse);
            params.put(AppConst.POST_REGISTRATION,registrationYear);
            return params;
        }
    };
    swipeRefreshLayout.setRefreshing(false); //stop refreshing
    //add to request queue
    AppController.getInstance().addToRequestQueue(request);

}
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnCourseNewsFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onCourseNewsFragmentInteraction(Uri uri);
    }

}
