package io.github.vvb2060.ims;

import static io.github.vvb2060.ims.PrivilegedProcess.TAG;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.system.Os;
import android.util.Log;

import org.lsposed.hiddenapibypass.LSPass;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;

public class ShizukuProvider extends rikka.shizuku.ShizukuProvider {
    static {
        LSPass.setHiddenApiExemptions("");
    }

    private boolean skip = false;

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        int sdkUid;
        try {
            sdkUid = (Integer) Process.class.getMethod("toSdkSandboxUid", int.class).invoke(null, Os.getuid());
        } catch (Exception e) {
            sdkUid = Os.getuid();
        }
        final int finalSdkUid = sdkUid;
        var callingUid = Binder.getCallingUid();
        if (callingUid != finalSdkUid && callingUid != Process.SHELL_UID) {
            return new Bundle();
        }

        if (METHOD_SEND_BINDER.equals(method)) {
            Shizuku.addBinderReceivedListener(() -> {
                if (!skip && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                    startInstrument(getContext());
                }
            });
        } else if (METHOD_GET_BINDER.equals(method) && callingUid == finalSdkUid && extras != null) {
            skip = true;
            Shizuku.addBinderReceivedListener(() -> {
                var binder = extras.getBinder("binder");
                if (binder != null && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                    startShellPermissionDelegate(binder, finalSdkUid);
                }
            });
        }
        return super.call(method, arg, extras);
    }

    private static void startShellPermissionDelegate(IBinder binder, int sdkUid) {
        try {
            var serviceManager = Class.forName("android.os.ServiceManager");
            var activity = serviceManager.getMethod("getService", String.class).invoke(null, Context.ACTIVITY_SERVICE);
            var iActivityManager = Class.forName("android.app.IActivityManager");
            var stubClass = Class.forName("android.app.IActivityManager$Stub");
            var am = stubClass.getMethod("asInterface", IBinder.class).invoke(null, new ShizukuBinderWrapper((IBinder) activity));
            am.getClass().getMethod("startDelegateShellPermissionIdentity", int.class, String.class).invoke(am, sdkUid, null);
            var data = Parcel.obtain();
            binder.transact(1, data, null, 0);
            data.recycle();
            am.getClass().getMethod("stopDelegateShellPermissionIdentity").invoke(am);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private static void startInstrument(Context context) {
        try {
            var serviceManager = Class.forName("android.os.ServiceManager");
            var binder = serviceManager.getMethod("getService", String.class).invoke(null, Context.ACTIVITY_SERVICE);
            var stubClass = Class.forName("android.app.IActivityManager$Stub");
            var am = stubClass.getMethod("asInterface", IBinder.class).invoke(null, new ShizukuBinderWrapper((IBinder) binder));
            var name = new ComponentName(context, PrivilegedProcess.class);
            var flagsField = ActivityManager.class.getField("INSTR_FLAG_DISABLE_HIDDEN_API_CHECKS");
            var flags = flagsField.getInt(null);
            var flagsField2 = ActivityManager.class.getField("INSTR_FLAG_INSTRUMENT_SDK_SANDBOX");
            flags |= flagsField2.getInt(null);
            am.getClass().getMethod("startInstrumentation", ComponentName.class, String.class, int.class, Bundle.class, IBinder.class, int.class, String.class).invoke(am, name, null, flags, new Bundle(), null, 0, null);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
