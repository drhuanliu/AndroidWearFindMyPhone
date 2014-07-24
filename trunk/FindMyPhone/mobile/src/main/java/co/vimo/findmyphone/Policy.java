package co.vimo.findmyphone;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by huan on 7/23/14.
 */

public class Policy {
    public static final int REQUEST_ADD_DEVICE_ADMIN = 1;

    private static final String APP_PREF = "APP_PREF";
    private static final String KEY_PASSWORD_LENGTH = "PW_LENGTH";
    private static final String KEY_PASSWORD_QUALITY = "PW_QUALITY";
    private static final String KEY_PASSWORD_MIN_UPPERCASE = "PW_MIN_UPPERCASE";

    // Password quality values.  This list must match the list
    // found in res/values/arrays.xml
    final static int[] PASSWORD_QUALITY_VALUES = new int[] {
            DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED,
            DevicePolicyManager.PASSWORD_QUALITY_SOMETHING,
            DevicePolicyManager.PASSWORD_QUALITY_NUMERIC,
            DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC,
            DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC,
            DevicePolicyManager.PASSWORD_QUALITY_COMPLEX
    };

    private int mPasswordQuality;
    private int mPasswordLength;
    private int mPasswordMinUpperCase;
    private Context mContext;
    private DevicePolicyManager mDPM;
    private ComponentName mPolicyAdmin;

    public Policy(Context context) {
        mContext = context;
        mPasswordQuality = -1;
        mPasswordLength = 0;
        mPasswordMinUpperCase = 0;
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
      //  mPolicyAdmin = new ComponentName(context, PolicyAdmin.class);
    }

    /**
     * Saves the policy parameters.
     *
     * @param passwordQuality Password quality.
     * @param passwordLength Password minimum length.
     * @param passwordUppercase Password minimum number of upper case alphabets.
     */
    public void saveToLocal(int passwordQuality, int passwordLength, int passwordMinUppercase) {
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).edit();
        if (mPasswordQuality != passwordQuality) {
            editor.putInt(KEY_PASSWORD_QUALITY, passwordQuality);
            mPasswordQuality = passwordQuality;
        }
        if (mPasswordLength != passwordLength) {
            editor.putInt(KEY_PASSWORD_LENGTH, passwordLength);
            mPasswordLength = passwordLength;
        }
        if (mPasswordMinUpperCase != passwordMinUppercase) {
            editor.putInt(KEY_PASSWORD_MIN_UPPERCASE, passwordMinUppercase);
            mPasswordMinUpperCase = passwordMinUppercase;
        }
        editor.commit();
    }

    public void readFromLocal() {
        SharedPreferences prefs = mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        mPasswordQuality = prefs.getInt(KEY_PASSWORD_QUALITY, -1);
        mPasswordLength = prefs.getInt(KEY_PASSWORD_LENGTH, -1);
        mPasswordMinUpperCase = prefs.getInt(KEY_PASSWORD_MIN_UPPERCASE, -1);
    }

    /**
     * Getter for password quality.
     *
     * @return
     */
    public int getPasswordQuality() { return mPasswordQuality; }

    /**
     * Getter for password length.
     *
     * @return
     */
    public int getPasswordLength() { return mPasswordLength; }

    /**
     * Getter for password minimum upper case alphabets.
     *
     * @return
     */
    public int getPasswordMinUpperCase() { return mPasswordMinUpperCase; }

    /**
     * Getter for the policy administrator ComponentName object.
     *
     * @return
     */
    public ComponentName getPolicyAdmin() { return mPolicyAdmin; }

    /**
     * Indicates whether the device administrator is currently active.
     *
     * @return
     */
    public boolean isAdminActive() {
        return mDPM.isAdminActive(mPolicyAdmin);
    }

    public boolean isActivePasswordSufficient() {
        return mDPM.isActivePasswordSufficient();
    }

    public boolean isDeviceSecured() {
        return isAdminActive() && isActivePasswordSufficient();
    }

    /**
     * Configure policy defined in the object.
     */
    public void configurePolicy() {
        mDPM.setPasswordQuality(mPolicyAdmin, PASSWORD_QUALITY_VALUES[mPasswordQuality]);
        mDPM.setPasswordMinimumLength(mPolicyAdmin, mPasswordLength);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDPM.setPasswordMinimumUpperCase(mPolicyAdmin, mPasswordMinUpperCase);
        }
    }

    /**
     * Through the PolicyAdmin receiver, the app can use this to trap various device
     * administration events, such as password change, incorrect password entry, etc.
     *
     */
    public static class PolicyAdmin extends DeviceAdminReceiver {

        void showToast(Context context, String msg) {
            String status = "enabled";// context.getString(R.string.admin_receiver_status, msg);
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            showToast(context, "onenabled"); // context.getString(R.string.admin_receiver_status_enabled));
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "disableRequested"; // context.getString(R.string.admin_receiver_status_disable_warning);
        }


        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            showToast(context, "password changed"); //context.getString(R.string.admin_receiver_status_pw_changed));
        }
        @Override
        public void onDisabled(Context context, Intent intent) {
            // Called when the app is about to be deactivated as a device administrator.
            // Deletes previously stored password policy.
            super.onDisabled(context, intent);
            SharedPreferences prefs = context.getSharedPreferences(APP_PREF, Activity.MODE_PRIVATE);
            prefs.edit().clear().commit();
        }
    }
}

