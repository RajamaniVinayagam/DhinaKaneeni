package com.dreamerindia.rajamani.dhinakaneeni;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.dreamerindia.rajamani.dhinakaneeni.fragment.NewsFragment;
import com.dreamerindia.rajamani.dhinakaneeni.models.RSSFeed;
import com.dreamerindia.rajamani.dhinakaneeni.utils.NewsFeedParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    String RSSFEEDURL = "http://tamilcomputerinfo.blogspot.com/feeds/posts/default?alt=rss";
    RSSFeed feed;
    String fileName;
    private List<RSSFeed> mRssFeedList = new ArrayList<RSSFeed>();

    public SplashActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        fileName = "RSSFeed.td";
        File feedFile = getBaseContext().getFileStreamPath(fileName);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            // Connected - Start parsing
            new AsyncLoadXMLFeed().execute();

        } else {
            // No connectivity. Check if feed File exists
            if (!feedFile.exists()) {

                // No connectivity & Feed file doesn't exist: Show alert to exit
                // & check for connectivity
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        "சர்வரை அடைய முடியவில்லை, \nஉங்கள் இணைய இணைப்பு சரிபார்க்கவும்.")
                        .setTitle("தின கணினி")
                        .setCancelable(false)
                        .setPositiveButton("வெளியேறு",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        finish();
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
            } else {

                // No connectivty and file exists: Read feed from the File
                Toast toast = Toast.makeText(this,
                        "இணைப்பு இல்லை! கடைசியாக புதுப்பிக்கப்பட்டது படித்தல்...",
                        Toast.LENGTH_LONG);
                toast.show();
                feed = ReadFeed(fileName);
                MainActivity(feed);
            }

        }
    }

    private void MainActivity(RSSFeed feed) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("feed", feed);

        // launch List activity
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        // kill this activity
        finish();

    }

    private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Obtain feed
            NewsFeedParser mNewsFeeder;
            mNewsFeeder = new NewsFeedParser(RSSFEEDURL);
            mRssFeedList = mNewsFeeder.parseXmlData();
            mRssFeedList.size();
            if (feed != null && mRssFeedList.size() > 0)
                WriteFeed(feed);
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            MainActivity(feed);
        }

    }

    // Method to write the feed to the File
    private void WriteFeed(RSSFeed data) {

        FileOutputStream fOut = null;
        ObjectOutputStream osw = null;

        try {
            fOut = openFileOutput(fileName, MODE_PRIVATE);
            osw = new ObjectOutputStream(fOut);
            osw.writeObject(data);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to read the feed from the File
    private RSSFeed ReadFeed(String fName) {

        FileInputStream fIn = null;
        ObjectInputStream isr = null;

        RSSFeed _feed = null;
        File feedFile = getBaseContext().getFileStreamPath(fileName);
        if (!feedFile.exists())
            return null;

        try {
            fIn = openFileInput(fName);
            isr = new ObjectInputStream(fIn);

            _feed = (RSSFeed) isr.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return _feed;

    }
}
