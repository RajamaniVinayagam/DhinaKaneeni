package com.dreamerindia.rajamani.dhinakaneeni;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.dreamerindia.rajamani.dhinakaneeni.models.RSSFeed;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;


public class WebViewActivity extends AppCompatActivity {


    private WebView webView1;
    private Toolbar toolbar;
    private String title;
    private String url;
    FloatingActionButton topCenterButton;
    FloatingActionMenu topCenterMenu;
    private boolean serviceWillBeDismissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");


        getSupportActionBar().setTitle(title);

        if (savedInstanceState != null) {
            ((WebView) findViewById(R.id.webView1)).restoreState(savedInstanceState);
        } else {

            webView1 = (WebView) findViewById(R.id.webView1);

            webView1.getSettings().setJavaScriptEnabled(true);

            webView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            final Activity activity = this;

            webView1.setWebViewClient(new WebViewClient()

            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,
                                                        String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }
            });

            webView1.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    activity.setProgress(progress * 1000);

                }
            });

            webView1.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Code for WebView goes here
                    webView1.loadUrl(url);
                }
            });
        }

        serviceWillBeDismissed = false;

        // Set up the large red button on the top center side
        // With custom button and content sizes and margins
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_plus));

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);


        topCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        tCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        SubActionButton.Builder tCRedBuilder = new SubActionButton.Builder(this);
        tCRedBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_red_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        tCSubBuilder.setLayoutParams(blueParams);
        tCRedBuilder.setLayoutParams(blueParams);

        ImageView tcIcon1 = new ImageView(this);
        ImageView tcIcon2 = new ImageView(this);
        ImageView tcIcon3 = new ImageView(this);


        tcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_share));
        tcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_save));
        tcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_home));

        SubActionButton tcSub1 = tCSubBuilder.setContentView(tcIcon1, blueContentParams).build();
        SubActionButton tcSub2 = tCSubBuilder.setContentView(tcIcon2, blueContentParams).build();
        SubActionButton tcSub3 = tCSubBuilder.setContentView(tcIcon3, blueContentParams).build();


        // Build another menu with custom options
        topCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(tcSub2, tcSub2.getLayoutParams().width, tcSub2.getLayoutParams().height)
                .addSubActionView(tcSub3, tcSub3.getLayoutParams().width, tcSub3.getLayoutParams().height)
                .setRadius(redActionMenuRadius)
                .setStartAngle(175)
                .setEndAngle(275)
                .attachTo(topCenterButton)
                .build();

        topCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                if (serviceWillBeDismissed) {

                    serviceWillBeDismissed = false;
                }
            }
        });

        // make the red button terminate the service
        tcSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Choose the option to share", Toast.LENGTH_SHORT).show();
                serviceWillBeDismissed = true; // the order is important
                topCenterMenu.close(true);
            }
        });
        tcSub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "News has been saved for offline reading!", Toast.LENGTH_SHORT).show();
                serviceWillBeDismissed = true; // the order is important
                topCenterMenu.close(true);
            }
        });
        tcSub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "You are in Home", Toast.LENGTH_SHORT).show();
                serviceWillBeDismissed = true; // the order is important
                topCenterMenu.close(true);

            }
        });

    }

    @Override
    public void onDestroy() {
        if (topCenterMenu != null && topCenterMenu.isOpen()) topCenterMenu.close(false);
        if (topCenterButton != null) topCenterButton.detach();

        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((WebView) findViewById(R.id.webView1)).saveState(outState);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
            webView1.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
