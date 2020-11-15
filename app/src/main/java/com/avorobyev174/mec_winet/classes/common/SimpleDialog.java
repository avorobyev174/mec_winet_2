package com.avorobyev174.mec_winet.classes.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.avorobyev174.mec_winet.R;


public class SimpleDialog extends Dialog {
    private View.OnClickListener onClickListener;
    private String conditionTitle;
    private String actionTitle;
    public Button confirmButton, cancelButton;
    public TextView conditionTextView;

    public SimpleDialog(@NonNull Activity activity, View.OnClickListener onClickListener, String conditionTitle, String actionTitle) {
        super(activity);
        this.onClickListener = onClickListener;
        this.conditionTitle = conditionTitle;
        this.actionTitle = actionTitle;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_dialog_activity);

        confirmButton = findViewById(R.id.confirmSimpleDialogButton);
        confirmButton.setText(actionTitle);
        cancelButton = findViewById(R.id.cancelSimpleDialogButton);
        conditionTextView = findViewById(R.id.simpleDialogTitle);
        conditionTextView.setText(conditionTitle);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmButton.setOnClickListener(onClickListener);
    }
}
