package fr.jordanlambert.radion.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.jordanlambert.radion.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GamesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "GamesFragment";

    private OnFragmentInteractionListener mListener;

    public GamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onButtonCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_games, container, false);

        Button buttonTest = (Button) myInflatedView.findViewById(R.id.querryButtonGame);
        buttonTest.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView t = (TextView) v.getRootView().findViewById(R.id.alllala);
                t.setText("Loading ...");
                onButtonCall();
            }
        });


        // Inflate the layout for this fragment
        return myInflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void onButtonCall () {
        Log.d(TAG, "Button Called");
        new RetrieveFeedTask().execute();
    }

    private class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private static final String API_KEY = "lwUWLTDhm1mshKNpTegeITNu8qlVp1puJaGjsnlsx0Jnlkb1X3";
        private static final String API_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/?fields=name%2Csummary%2Cslug%2Curl&limit=1&offset=0&order=release_dates.date%3Adesc&search=call of duty";


        private Exception exception;

        protected void onPreExecute() {

            Log.d(TAG, "onPreExecute RetrieveFeedTask");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-Mashape-Key", API_KEY);

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            // Parse JSON response
            try {
                JSONArray jResponse = new JSONArray(response);
                for (int i=0; i < jResponse.length(); i++)
                {
                    try {
                        JSONObject oneObject = jResponse.getJSONObject(i);
                        // Pulling items from the array
                        String name = oneObject.getString("name");
                        String id = oneObject.getString("id");
                        String url = oneObject.getString("url");
                        String summary = oneObject.getString("summary");

                        Log.i("name of " + id + " ", name);

                    } catch (JSONException e) {
                        Log.e(TAG, "Something went wrong during JSON parsing :/");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView t = (TextView) getView().findViewById(R.id.alllala);
            t.setText(response);

            Log.d(TAG, response);

        }
    }

}
