package com.app.jsonvolley;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{

    ListView l;
    CustomAdapter adapter;
    String [] movie;
    String [] year;
    String [] rating;
    String [] duration;
    String [] director;
    String [] tagline;
    String [] images;
    JSONObject json;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = (ListView) findViewById(R.id.listView);
        fetchdata();

    }

    public void fetchdata()
    {


        try
        {

            // Instantiate the RequestQueue.

            String url ="https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                                json=response;
                            loaddata();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Toast.makeText(getApplicationContext(),"That Didn't Work!!!",Toast.LENGTH_LONG);
                        }
                    });


                        // Add the request to the RequestQueue.
            MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);



        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
        }

    }


    public void loaddata()
    {

        try
        {


            JSONArray movies=json.getJSONArray("movies");

            movie=new String[movies.length()];
            year=new String[movies.length()];
            rating=new String[movies.length()];
            duration=new String[movies.length()];
            director=new String[movies.length()];
            tagline=new String[movies.length()];
            images=new String[movies.length()];

            for(int i=0;i<movies.length();i++)
            {
                json=movies.getJSONObject(i);

                movie[i]=json.getString("movie");

                tagline[i]=json.getString("tagline");

                year[i]=json.getString("year");

                rating[i]=json.getString("rating");

                duration[i]=json.getString("duration");

                director[i]=json.getString("director");

                images[i]=json.getString("image");
            }
            adapter = new CustomAdapter(this, movie, year, rating, duration, director, tagline,images);
            l.setAdapter(adapter);
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
        }

    }

}


class CustomAdapter extends ArrayAdapter<String>
{
    Context context;
    String [] movie;
    String [] year;
    String [] rating;
    String [] duration;
    String [] director;
    String [] tagline;
    String [] images;


    CustomAdapter(Context c, String[] movie, String[] year, String[] rating, String[] duration, String[] director, String[] tagline, String []  images)
    {
        super(c,R.layout.movie,R.id.moviename,movie);
        this.context=c;
        this.movie=movie;
        this.year=year;
        this.rating=rating;
        this.duration=duration;
        this.director=director;
        this.tagline=tagline;
        this.images=images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.movie,parent,false);

        ImageLoader mImageLoader;

        TextView mname = (TextView) row.findViewById(R.id.moviename);
        TextView yr = (TextView) row.findViewById(R.id.year);
        TextView rtg = (TextView) row.findViewById(R.id.rating);
        TextView dur = (TextView) row.findViewById(R.id.duration);
        TextView dir = (TextView) row.findViewById(R.id.director);
        TextView tag = (TextView) row.findViewById(R.id.tagline);
        NetworkImageView mNetworkImageView = (NetworkImageView) row.findViewById(R.id.networkImageView);


        mname.setText(movie[position]);
        yr.setText(year[position]);
        rtg.setText(rating[position]);
        dur.setText(duration[position]);
        dir.setText(director[position]);
        tag.setText(tagline[position]);

        // Get the ImageLoader through your singleton class.
        mImageLoader = MySingleton.getInstance(getContext().getApplicationContext()).getImageLoader();

// Set the URL of the image that should be loaded into this view, and
// specify the ImageLoader that will be used to make the request.
        mNetworkImageView.setImageUrl(images[position], mImageLoader);

        return row;
    }

}

