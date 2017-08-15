
package com.marakana.android.securenote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GetPasswordActivity extends Activity implements OnClickListener, TextWatcher {
    private static final String TAG = "GetPasswordActivity";

    public static final String VERIFY_PASSWORD_REQUEST_PARAM = "verifyPassword";

    public static final String MIN_PASSWORD_LENGTH_REQUEST_PARAM = "minPasswordLength";

    public static final String PASSWORD_RESPONSE_PARAM = "password";

    private EditText password;

    private EditText passwordVerification;

    private Button ok;

    private Button cancel;

    private int minPasswordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.get_password);
        this.password = (EditText)super.findViewById(R.id.password_text);
        this.passwordVerification = (EditText)super.findViewById(R.id.password_verification_text);
        this.ok = (Button)super.findViewById(R.id.ok_button);
        this.cancel = (Button)super.findViewById(R.id.cancel_button);
        this.ok.setOnClickListener(this);
        this.cancel.setOnClickListener(this);

        Intent request = super.getIntent();
        boolean verifyPassword = request.getBooleanExtra(VERIFY_PASSWORD_REQUEST_PARAM, true);
        if (verifyPassword) {
            this.ok.setEnabled(false);
            this.minPasswordLength = request.getIntExtra(MIN_PASSWORD_LENGTH_REQUEST_PARAM, 0);
            if (minPasswordLength > 0) {
                this.password.setHint(super.getString(R.string.password_hint_min_length,
                        this.minPasswordLength));
            }
            this.password.addTextChangedListener(this);
            this.passwordVerification.addTextChangedListener(this);
        } else {
            this.passwordVerification.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        if (v == this.ok) {
            Intent data = new Intent();
            data.putExtra(PASSWORD_RESPONSE_PARAM, this.password.getText().toString());
            super.setResult(RESULT_OK, data);
        } else if (v == this.cancel) {
            super.setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    public void afterTextChanged(Editable s) {
        if (this.password.length() < this.minPasswordLength) {
            Log.d(TAG, "Password too short");
        } else if (this.password.length() != this.passwordVerification.length()) {
            Log.d(TAG, "Passwords' length differs");
        } else if (!this.password.getText().toString().equals(
                this.passwordVerification.getText().toString())) {
            Log.d(TAG, "Passwords differ");
        } else {
            Log.d(TAG, "Passwords are the same");
            this.ok.setEnabled(true);
            return;
        }
        this.ok.setEnabled(false);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignored
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ignored
    }
}
