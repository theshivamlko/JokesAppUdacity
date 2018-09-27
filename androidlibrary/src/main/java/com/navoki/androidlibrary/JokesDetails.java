package com.navoki.androidlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class JokesDetails extends AppCompatActivity {
    public final static String ACTION_JOKES_VIEW = "com.navoki.androidlibrary.JOKE.DETAILS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jokes_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView text = findViewById(R.id.text);
        FloatingActionButton shareFab = findViewById(R.id.share_fab);
        CardView cardView = findViewById(R.id.cardView);

        final Intent intent = getIntent();
        if (intent != null) {
            text.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            text.setLineSpacing(2, 2);
            setTitle(intent.getStringExtra(Intent.EXTRA_TITLE));
        }
        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String builder = getString(R.string.share_msg) +
                        "\n" +
                        getString(R.string.app_name) +
                        "\n" +
                        "\n" +
                        intent.getStringExtra(Intent.EXTRA_TEXT);

                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(JokesDetails.this)
                        .setType(getString(R.string.data_text))
                        .setText(builder)
                        .setChooserTitle(R.string.share_it)
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        Animation animation = AnimationUtils.loadAnimation(JokesDetails.this,
                R.anim.anim_from_bottom);
        cardView.startAnimation(animation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId())
            finish();
        return super.onOptionsItemSelected(item);
    }
}
