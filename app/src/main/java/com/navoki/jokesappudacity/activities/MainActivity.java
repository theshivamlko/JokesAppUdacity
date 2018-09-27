package com.navoki.jokesappudacity.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.navoki.androidlibrary.JokesDetails;
import com.navoki.jokesappudacity.BuildConfig;
import com.navoki.jokesappudacity.R;
import com.navoki.jokesappudacity.dialogs.CustomDialog;
import com.navoki.jokesappudacity.interfaces.OnAsynResponse;
import com.navoki.jokesappudacity.utils.AppConstants;
import com.navoki.jokesappudacity.utils.JokesAsyncTask;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private ImageView chain;
    private static CustomDialog customDialog;
    private final int dimens = 200;
    private final int duration = 500; //milliseconds
    private static InterstitialAd mInterstitialAd;
    private float y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.app_name));
        TextView text = findViewById(R.id.text);
        chain = findViewById(R.id.chain);
        RelativeLayout container = findViewById(R.id.container);

        Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.fontPath));
        text.setTypeface(typeface);

        context = MainActivity.this;
        customDialog = new CustomDialog(context);

        chain.setOnTouchListener(onClickListener);

        if (BuildConfig.APP_TYPE.equals(AppConstants.TYPE_FREE)) {
            // experimental App ID from Google Docs
            MobileAds.initialize(this, getString(R.string.dummy_app_id));
            AdView adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getString(R.string.dummy_banner_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            adView.setLayoutParams(params);
            container.addView(adView);

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(getString(R.string.dummy_interstitials_id));
            mInterstitialAd.loadAd(adRequest);
        }
    }

    private final View.OnTouchListener onClickListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                // when user first touches the screen we get x and y coordinate
                case MotionEvent.ACTION_DOWN: {
                    y1 = motionEvent.getY();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    y2 = motionEvent.getY();
                    if (y1 < y2) {
                        chain.animate().translationYBy(dimens).setDuration(duration).setListener(animatorListener);
                    }
                }
            }
            return true;
        }
    };

    private final Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            chain.animate().translationY(0).setDuration(duration);
            chain.animate().setListener(null);
            if (!customDialog.isShowing())
                customDialog.show();

            if (BuildConfig.APP_TYPE.equals(AppConstants.TYPE_FREE))
                mInterstitialAd.show();

            new JokesAsyncTask(new OnAsynResponse() {
                @Override
                public void onResponse(String response) {
                    manageResponse(response);
                    Log.e("MSg",response+"");
                }
            }).execute();

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };


    private void manageResponse(final String s) {
        customDialog.dismiss();

        if (BuildConfig.APP_TYPE.equals(AppConstants.TYPE_FREE)) {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    openDetailPage(s);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());        }
            });
        } else
            openDetailPage(s);
    }

    private static void openDetailPage(String joke) {
        if (joke != null) {
            Intent intent = new Intent(JokesDetails.ACTION_JOKES_VIEW);
            intent.putExtra(Intent.EXTRA_TITLE, context.getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, joke);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
            else
                Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }
}
