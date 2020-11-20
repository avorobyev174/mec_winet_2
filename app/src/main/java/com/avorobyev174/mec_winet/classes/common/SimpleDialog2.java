package com.avorobyev174.mec_winet.classes.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.house.HouseCreateDialogFragment;


public class SimpleDialog2 extends Dialog {
    private View.OnClickListener onClickListener;
    private String conditionTitle;
    private String actionTitle;
    public Button confirmButton, cancelButton;
    public TextView conditionTextView;
    private FragmentManager fragmentManager;
    private final DialogFragment dialogFragment;

    public SimpleDialog2(@NonNull Activity activity, View.OnClickListener onClickListener, String conditionTitle, String actionButtonTitle, FragmentManager fragmentManager, DialogFragment dialogFragment) {
        super(activity);
        this.onClickListener = onClickListener;
        this.conditionTitle = conditionTitle;
        this.actionTitle = actionButtonTitle;
        this.fragmentManager = fragmentManager;
        this.dialogFragment = dialogFragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_dialog_activity2);

        confirmButton = findViewById(R.id.confirmSimpleDialogButton);
        confirmButton.setText(actionTitle);

        cancelButton = findViewById(R.id.cancelSimpleDialogButton);

        conditionTextView = findViewById(R.id.simpleDialogTitle);
        conditionTextView.setText(conditionTitle);

        fragmentManager.beginTransaction().replace(R.id.fragment_dialog_container2, HouseCreateDialogFragment.newInstance()).commitNow();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmButton.setOnClickListener(onClickListener);
    }
}
