package com.inc.automata.unamupdates.fragments;

import android.app.Activity;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.inc.automata.unamupdates.R;
import com.inc.automata.unamupdates.appconstants.AppConst;
import com.inc.automata.unamupdates.appconstants.AppController;
import com.inc.automata.unamupdates.appconstants.ConnectionDetector;
import com.inc.automata.unamupdates.appconstants.NewsAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class GeneralNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String TAG= GeneralNewsFragment.class.getSimpleName();
    ListView lvGeneralNews;
    int count;
    ArrayList<HashMap<String,String>> newsItemList;
    ListAdapter adapter;

    private final String TAG_TITLE="title";private final String TAG_MESSAGE="message";private final String TAG_DATE="date_time";
    private final String TAG_OWNER="owner";
    private static JSONArray jsonArray;

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isDataAvailable; //stores whether data is avaialble or not

    public static GeneralNewsFragment newInstance(String param1, String param2) {
        GeneralNewsFragment fragment = new GeneralNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GeneralNewsFragment() {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvGeneralNews = (ListView) getView().findViewById(R.id.listViewGeneral);

        swipeRefreshLayout=(SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout_general);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true); //set refreshing
                GetGeneralNews();//get news
            }
        });

       lvGeneralNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               JSONObject singleNews;
               try {
                   singleNews = jsonArray.getJSONObject(position);
                   String title = singleNews.getString(TAG_TITLE);//get title
                   String message = singleNews.getString(TAG_MESSAGE);//get message

                   NewsAlertDialog nad = NewsAlertDialog.newInstance(message, title);
                   nad.show(getFragmentManager(), TAG); //show dialog

               } catch (JSONException jex) {
                   singleNews = null;
                   Toast.makeText(getActivity().getApplicationContext(), "JSON: Error parsing news", Toast.LENGTH_LONG).show();
               } catch (NullPointerException npe) {
                   singleNews = null;
                   Toast.makeText(getActivity().getApplicationContext(), "Null pointer exception: "+npe.getMessage(), Toast.LENGTH_LONG).show();
               } catch (Exception ex) {
                   singleNews = null;
                   Toast.makeText(getActivity().getApplicationContext(), "Error parsing news: "+ex.getMessage(), Toast.LENGTH_LONG).show();
               }
           }
       });
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
        return inflater.inflate(R.layout.fragment_general_news, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGeneralNewsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    //for swipe refresh listener
    @Override
    public void onRefresh() {
    //call method to get updated news
      swipeRefreshLayout.setRefreshing(true); //set refreshing
      GetGeneralNews();//get news
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onGeneralNewsFragmentInteraction(Uri uri);
    }

    private void GetGeneralNews(){
        isDataAvailable = new ConnectionDetector(getActivity().getApplicationContext()).isConnectingToInternet();

        if(!isDataAvailable){
            swipeRefreshLayout.setRefreshing(false);
            Log.e(TAG, "no internet connection. isDataAvailable: " + isDataAvailable);
            Toast.makeText(getActivity().getApplicationContext(),"Internet connection not available. Try again.",Toast.LENGTH_LONG).show();
            return; //do not continue processing
        }
        newsItemList = new ArrayList<HashMap<String, String>>();
        JsonArrayRequest request = new JsonArrayRequest(AppConst.GENERALNEWSURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                    GeneralNewsFragment.jsonArray = jsonArray;
                try{

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject newsItemObject=(JSONObject) jsonArray.get(i);
                        String title=newsItemObject.getString(TAG_TITLE);//get title
                        String message=newsItemObject.getString(TAG_MESSAGE);//get message
                        String date_time=newsItemObject.getString(TAG_DATE);//get date
                        date_time=date_time.substring(0,10);//trim date
                        String owner;
                        try {
                            owner  =message.substring((int)(message.length()*(0.9)));//get substring of only the end of the message
                            owner=owner.substring(owner.indexOf("by"));

                        }catch(Exception ex) {
                            owner="by Default";
                        }
                        HashMap<String,String> singleItem= new HashMap<String,String>();//hashmap to store key value pairs
                        singleItem.put(TAG_TITLE,title);//put title
                        singleItem.put(TAG_MESSAGE,message);//put message
                        singleItem.put(TAG_DATE,date_time);//put date time
                        singleItem.put(TAG_OWNER,owner);//put owner

                        newsItemList.add(singleItem);//add to overall list
                    }
                    if(newsItemList !=null) {
                        //adapter for list
                        adapter = new SimpleAdapter(getActivity(), newsItemList, R.layout.list_item, new String[]{TAG_TITLE, TAG_DATE, TAG_OWNER}, new int[]{R.id.news_title, R.id.news_date, R.id.news_owner});
                        lvGeneralNews.setAdapter(adapter);

                    }else{
                        //inform user that JSON data was not retrieved
                        Toast.makeText(getActivity().getApplicationContext(),"Error retrieving JSON data. Please check internet connection",Toast.LENGTH_LONG).show();
                        Log.e(TAG,"Retrieving JSON data: ");
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }catch(JSONException jex){
                    Toast.makeText(getActivity().getApplicationContext(),"JSON Error: "+jex.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,jex.getMessage());
                }catch(Exception ex){
                    Toast.makeText(getActivity().getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,ex.toString());
                }finally {
                    swipeRefreshLayout.setRefreshing(false);//dismiss refresher
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, "JSON Volley error: " + volleyError.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),"Volley could not complete request: "+volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //add to volley request queue
        AppController.getInstance().addToRequestQueue(request);
    }

}
