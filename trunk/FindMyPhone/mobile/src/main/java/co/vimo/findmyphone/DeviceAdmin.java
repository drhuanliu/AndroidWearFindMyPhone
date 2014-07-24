package co.vimo.findmyphone;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by huan on 7/24/14.
 */
public class DeviceAdmin extends DeviceAdminReceiver {

    void showToast(Context context, String msg) {
        String status = "enabled";// context.getString(R.string.admin_receiver_status, msg);
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
//        showToast(context, "onenabled"); // context.getString(R.string.admin_receiver_status_enabled));
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return super.onDisableRequested(context, intent);
//        return "disableRequested"; // context.getString(R.string.admin_receiver_status_disable_warning);
    }


    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
//        showToast(context, "password changed"); //context.getString(R.string.admin_receiver_status_pw_changed));
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        // Called when the app is about to be deactivated as a device administrator.
        // Deletes previously stored password policy.
        super.onDisabled(context, intent);
//        SharedPreferences prefs = context.getSharedPreferences(APP_PREF, Activity.MODE_PRIVATE);
//        prefs.edit().clear().commit();
    }
}
