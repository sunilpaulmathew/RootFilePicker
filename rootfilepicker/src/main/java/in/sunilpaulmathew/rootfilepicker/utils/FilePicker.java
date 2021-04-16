package in.sunilpaulmathew.rootfilepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;

import com.topjohnwu.superuser.io.SuFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sunilpaulmathew.rootfilepicker.R;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class FilePicker {

    private static String mExtension = null, mPath = null, mSelectedFilePath = null;;

    public static List<String> getData(Activity activity) {
        List<String> mData = new ArrayList<>(), mDir = new ArrayList<>(), mFiles = new ArrayList<>();
        try {
            mData.clear();
            // Add directories
            for (File mFile : SuFile.open(getPath()).listFiles()) {
                if (mFile.isDirectory()) {
                    mDir.add(mFile.getAbsolutePath());
                }
            }
            Collections.sort(mDir);
            if (!getBoolean("az_order", true, activity)) {
                Collections.reverse(mDir);
            }
            mData.addAll(mDir);
            // Add files
            for (File mFile : SuFile.open(getPath()).listFiles()) {
                if (mFile.isFile() && isSupportedFile(mFile.getAbsolutePath())) {
                    mFiles.add(mFile.getAbsolutePath());
                }
            }
            Collections.sort(mFiles);
            if (!getBoolean("az_order", true, activity)) {
                Collections.reverse(mFiles);
            }
            mData.addAll(mFiles);
        } catch (NullPointerException ignored) {
            activity.finish();
        }
        return mData;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static boolean isDarkTheme(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static boolean isImageFile(String path) {
        return path.endsWith(".bmp") || path.endsWith(".png") || path.endsWith(".jpg");
    }

    public static boolean isSupportedFile(String path) {
        if (getExtension() == null) {
            return true;
        } else {
            return path.endsWith(getExtension());
        }
    }

    public static boolean getBoolean(String name, boolean defaults, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(name, defaults);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void saveBoolean(String name, boolean value, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(name, value).apply();
    }

    public static void setPath(String path) {
        mPath = path;
    }

    public static boolean isRoot() {
        return getPath().equals("/");
    }

    public static boolean isStorageRoot() {
        return getPath().equals(Environment.getExternalStorageDirectory().toString());
    }

    public static int getOrientation(Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode() ?
                Configuration.ORIENTATION_PORTRAIT : activity.getResources().getConfiguration().orientation;
    }

    public static int getThemeAccentColor(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public static String getPath() {
        if (mPath == null) {
            return "/";
        } else {
            return mPath;
        }
    }

    public static String getExtension() {
        return mExtension;
    }

    public static File getSelectedFilePath() {
        return SuFile.open(mSelectedFilePath);
    }

    public static String getFileSize(String path) {
        long mSize = SuFile.open(path).length() / 1024;
        long mDecimal = (SuFile.open(path).length() - 1024) / 1024;
        if (mSize > 1024) {
            return mSize / 1024 + "." + mDecimal + " MB";
        } else {
            return mSize  + " KB";
        }
    }

    public static Uri getImageURI(String path) {
        File mFile = SuFile.open(path);
        if (mFile.exists()) {
            return Uri.fromFile(mFile);
        }
        return null;
    }

    public static void setExtension(String path) {
        mExtension = path;
    }

    public static void setSelectedFilePath(String path) {
        mSelectedFilePath = path;
    }

}