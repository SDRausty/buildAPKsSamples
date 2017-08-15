
package com.marakana.android.securenote;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecureNoteActivity extends Activity implements OnClickListener, TextWatcher {
    private static final String PASSWORD_KEY = "password";

    private static final String CHARSET = "UTF-8";

    private static final String FILENAME = "secure.note";

    private static final String TAG = "SecureNoteActivity";

    private static final int GET_PASSWORD_FOR_LOAD = 1;

    private static final int GET_PASSWORD_FOR_SAVE = 2;

    private static final int CHANGE_PASSWORD = 3;

    private final int ENABLE_DEVICE_ADMIN_REQUEST = 4;

    private final int SET_DEVICE_ADMIN_PASSWORD_REQUEST = 5;

    private EditText noteText;

    private Button loadButton;

    private Button saveButton;

    private Button closeButton;

    private String password;

    private DevicePolicyManager devicePolicyManager;

    private ComponentName deviceAdminComponentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.deviceAdminComponentName = new ComponentName(this, SecureNoteDeviceAdminReceiver.class);
        super.setContentView(R.layout.secure_note);
        this.noteText = (EditText) super.findViewById(R.id.note_text);
        this.loadButton = (Button) super.findViewById(R.id.load_button);
        this.saveButton = (Button) super.findViewById(R.id.save_button);
        this.closeButton = (Button) super.findViewById(R.id.close_button);
        this.loadButton.setOnClickListener(this);
        this.saveButton.setOnClickListener(this);
        this.closeButton.setOnClickListener(this);
        this.noteText.addTextChangedListener(this);
        this.password = savedInstanceState == null ? null : savedInstanceState
                .getString(PASSWORD_KEY);
        if (this.isDeviceAdminEnabled()) {
            if (this.noteText.length() == 0) {
                if (this.isSecureNoteFilePresent()) {
                    this.saveButton.setVisibility(View.GONE);
                    this.noteText.setEnabled(false);
                    this.getPassword(GET_PASSWORD_FOR_LOAD, false);
                } else {
                    this.loadButton.setVisibility(View.GONE);
                    this.saveButton.setEnabled(false);
                }
            } else {
                this.loadButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() - started");
        super.onResume();
        if (this.enableDeviceAdmin()) {
            this.ensureDeviceAdminPasswordIsSufficient();
        }
        Log.d(TAG, "onResume() - finished");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PASSWORD_KEY, this.password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.secure_note, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.change_password_button).setEnabled(this.password != null);
        menu.findItem(R.id.delete_button).setEnabled(this.isSecureNoteFilePresent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_password_button:
                this.getPassword(CHANGE_PASSWORD, true);
                return true;
            case R.id.delete_button:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.delete_alert)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SecureNoteActivity.this.deleteSecureNote();
                                    }
                                }).setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getPassword(int requestCode, boolean verifyPasswords) {
        Log.d(TAG, "Getting password");
        Intent intent = new Intent(this, GetPasswordActivity.class);
        intent.putExtra(GetPasswordActivity.MIN_PASSWORD_LENGTH_REQUEST_PARAM, 6);
        intent.putExtra(GetPasswordActivity.VERIFY_PASSWORD_REQUEST_PARAM, verifyPasswords);
        super.startActivityForResult(intent, requestCode);
    }

    private boolean isSecureNoteFilePresent() {
        return super.getFileStreamPath(FILENAME).exists();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_DEVICE_ADMIN_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d(TAG, "Enabled device admin");
                    this.ensureDeviceAdminPasswordIsSufficient();
                    break;
                case RESULT_CANCELED:
                    Log.d(TAG, "Device admin not enabled - exiting");
                    this.finish();
                    break;
                default:
                    Log.w(TAG, "Unexpected enable device admin result: " + resultCode);
            }
        } else if (requestCode == SET_DEVICE_ADMIN_PASSWORD_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG,
                        "Device admin password set: sufficient="
                                + this.devicePolicyManager.isActivePasswordSufficient());
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Device admin password not set - exiting");
                this.finish();
            }
        } else {
            switch (resultCode) {
                case RESULT_OK:
                    this.password = data
                            .getStringExtra(GetPasswordActivity.PASSWORD_RESPONSE_PARAM);
                    switch (requestCode) {
                        case GET_PASSWORD_FOR_LOAD:
                            this.loadSecureNote();
                            break;
                        case GET_PASSWORD_FOR_SAVE:
                            this.saveSecureNote();
                            break;
                        case CHANGE_PASSWORD:
                            Log.d(TAG, "Changed password (in memory)");
                            this.toast(R.string.changed_password);
                            this.saveButton.setEnabled(true);
                            break;
                    }
                    break;
                case RESULT_CANCELED:
                    Log.d(TAG, "Canceled result. Ignoring.");
                    break;
                default:
                    Log.w(TAG, "Unexpected result: " + resultCode);
            }
        }
    }

    public void onClick(View v) {
        if (v == this.loadButton) {
            if (this.password == null) {
                this.getPassword(GET_PASSWORD_FOR_LOAD, false);
            } else {
                this.loadSecureNote();
            }
        } else if (v == this.saveButton) {
            if (this.password == null) {
                this.getPassword(GET_PASSWORD_FOR_SAVE, true);
            } else {
                this.saveSecureNote();
            }
        } else if (v == this.closeButton) {
            Log.d(TAG, "Closing...");
            if (this.saveButton.isEnabled()) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.close_alert)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SecureNoteActivity.this.finish();
                                    }
                                }).setNegativeButton(android.R.string.no, null).show();
            } else {
                super.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        this.password = null;
        this.noteText.getText().clear();
        this.noteText = null;
        super.onDestroy();
    }

    private void deleteSecureNote() {
        Log.d(TAG, "Deleteting note");
        if (super.getFileStreamPath(FILENAME).delete()) {
            this.password = null;
            this.noteText.getText().clear();
            this.loadButton.setVisibility(View.GONE);
            this.saveButton.setVisibility(View.VISIBLE);
            this.saveButton.setEnabled(false);
            this.noteText.setEnabled(true);
            this.toast(R.string.deleted_note);
            Log.d(TAG, "Deleted note");
        } else {
            this.toast(R.string.failed_to_delete);
            Log.e(TAG, "Failed to delete note");
        }
    }

    private void saveSecureNote() {
        Log.d(TAG, "Saving note");
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    Writer writer = new OutputStreamWriter(CryptUtil.encrypt(
                            SecureNoteActivity.super.openFileOutput(FILENAME, MODE_PRIVATE),
                            SecureNoteActivity.this.password.getBytes(CHARSET)), CHARSET);
                    try {
                        for (String string : strings) {
                            writer.write(string);
                        }
                    } finally {
                        writer.close();
                    }
                    Log.d(TAG, "Saved note to " + FILENAME);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "Failed to save note to " + FILENAME, e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    SecureNoteActivity.this.toast(R.string.saved_note);
                    SecureNoteActivity.this.saveButton.setEnabled(false);
                } else {
                    SecureNoteActivity.this.toast(R.string.failed_to_save);
                }
            }

        }.execute(this.noteText.getText().toString());
    }

    private void loadSecureNote() {
        Log.d(TAG, "Loading note...");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Reader reader = new InputStreamReader(CryptUtil.decrypt(
                            SecureNoteActivity.super.openFileInput(FILENAME),
                            SecureNoteActivity.this.password.getBytes(CHARSET)), CHARSET);
                    try {
                        StringBuilder out = new StringBuilder(1024);
                        CharBuffer buffer = CharBuffer.allocate(64);
                        for (int n; (n = reader.read(buffer)) != -1;) {
                            buffer.flip();
                            out.append(buffer, 0, n);
                        }
                        Log.d(TAG, "Loaded note from " + FILENAME);
                        return out.toString();
                    } finally {
                        reader.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load note from " + FILENAME, e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    SecureNoteActivity.this.toast(R.string.failed_to_load);
                } else {
                    SecureNoteActivity.this.noteText.setText(result);
                    SecureNoteActivity.this.toast(R.string.loaded_note);
                    SecureNoteActivity.this.loadButton.setVisibility(View.GONE);
                    SecureNoteActivity.this.saveButton.setVisibility(View.VISIBLE);
                    SecureNoteActivity.this.saveButton.setEnabled(false);
                    SecureNoteActivity.this.noteText.setEnabled(true);
                }
            }
        }.execute();
    }

    private void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    public void afterTextChanged(Editable e) {
        this.saveButton.setEnabled(true);
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // ignored
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // ignored

    }

    private boolean isDeviceAdminEnabled() {
        return this.devicePolicyManager.isAdminActive(this.deviceAdminComponentName);
    }

    private boolean enableDeviceAdmin() {
        if (this.isDeviceAdminEnabled()) {
            Log.d(TAG, "Device Admin is already active!");
            return true;
        } else {
            Log.d(TAG, "Device Admin is not active. Requesting it to be enabled");
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, this.deviceAdminComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    super.getText(R.string.device_admin_explanation));
            super.startActivityForResult(intent, ENABLE_DEVICE_ADMIN_REQUEST);
            return false;
        }
    }

    private boolean ensureDeviceAdminPasswordIsSufficient() {
        this.devicePolicyManager.setPasswordQuality(deviceAdminComponentName,
                DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
        this.devicePolicyManager.setPasswordMinimumLength(deviceAdminComponentName, 4);
        this.devicePolicyManager.setMaximumTimeToLock(deviceAdminComponentName, 10000);
        if (this.devicePolicyManager.isActivePasswordSufficient()) {
            Log.d(TAG, "Active password is sufficient");
            return true;
        } else {
            Log.d(TAG, "Active device admin password is insufficient. Setting new password.");
            super.startActivityForResult(new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD),
                    SET_DEVICE_ADMIN_PASSWORD_REQUEST);
            return false;
        }
    }
}
