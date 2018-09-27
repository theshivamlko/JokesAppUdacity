package com.navoki.jokesappudacity.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.navoki.jokesappudacity.R;

public class CustomDialog extends AlertDialog {
    private final Context mContext;
    private final Animation animation;
    private ImageView image=null;

    public CustomDialog(Context context) {
        super(context);
        mContext = context;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_clockwise);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, R.layout.progress_dialog_layout, null);
        image = view.findViewById(R.id.image);

        final Window window = getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(view);
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        image.setAnimation(animation);
    }
}
