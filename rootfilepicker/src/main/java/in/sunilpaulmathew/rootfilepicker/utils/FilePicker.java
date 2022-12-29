package in.sunilpaulmathew.rootfilepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.TypedValue;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.topjohnwu.superuser.io.SuFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.sunilpaulmathew.rootfilepicker.R;
import in.sunilpaulmathew.rootfilepicker.activities.FilePickerActivity;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class FilePicker {

    private static ActivityResultLauncher<Intent> mResult = null;
    private static int mAccentColor = Integer.MIN_VALUE;
    public Context mContext;
    private static String mExtension = null, mPath = null, mSelectedFilePath = null;

    public FilePicker(ActivityResultLauncher<Intent> result, Context context) {
        mResult = result;
        mContext = context;
    }

    public static List<String> getData(Activity activity) {
        List<String> mData = new ArrayList<>(), mDir = new ArrayList<>(), mFiles = new ArrayList<>();
        try {
            // Add directories
            for (File mFile : Objects.requireNonNull(SuFile.open(getPath(activity)).listFiles())) {
                if (mFile.isDirectory()) {
                    mDir.add(mFile.getAbsolutePath());
                }
            }
            Collections.sort(mDir, String.CASE_INSENSITIVE_ORDER);
            if (!getBoolean("az_order", true, activity)) {
                Collections.reverse(mDir);
            }
            mData.addAll(mDir);
            // Add files
            for (File mFile : Objects.requireNonNull(SuFile.open(getPath(activity)).listFiles())) {
                if (mFile.isFile() && isSupportedFile(mFile.getAbsolutePath())) {
                    mFiles.add(mFile.getAbsolutePath());
                }
            }
            Collections.sort(mFiles, String.CASE_INSENSITIVE_ORDER);
            if (!getBoolean("az_order", true, activity)) {
                Collections.reverse(mFiles);
            }
            mData.addAll(mFiles);
        } catch (NullPointerException ignored) {
            activity.finish();
        }
        return mData;
    }

    public static boolean isImageFile(String path) {
        return path.endsWith(".bmp") || path.endsWith(".png") || path.endsWith(".jpg");
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private static boolean isDarkTheme(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static boolean isSupportedFile(String path) {
        if (mExtension == null) {
            return true;
        } else {
            if (!mExtension.startsWith(".")) {
                mExtension = "." + mExtension;
            }
            return path.endsWith(mExtension);
        }
    }

    public static boolean getBoolean(String name, boolean defaults, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(name, defaults);
    }

    public static boolean isRoot(Context context) {
        return getPath(context).equals("/");
    }

    public static boolean isStorageRoot(Context context) {
        return getPath(context).equals(Environment.getExternalStorageDirectory().toString());
    }

    public static Drawable getAPKIcon(String path, Context context) {
        if (context.getPackageManager().getPackageArchiveInfo(path, 0) != null) {
            return context.getPackageManager().getPackageArchiveInfo(path, 0).applicationInfo.loadIcon(context.getPackageManager());
        } else {
            return null;
        }
    }

    public static File getSelectedFile() {
        return SuFile.open(mSelectedFilePath);
    }

    public static int getAccentColor(Context context) {
        if (mAccentColor != Integer.MIN_VALUE) {
            return mAccentColor;
        } else {
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
            return value.data;
        }
    }

    public static int getOrientation(Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode() ?
                Configuration.ORIENTATION_PORTRAIT : activity.getResources().getConfiguration().orientation;
    }

    public static String getPath(Context context) {
        if (SuFile.open(getString("path", "/", context)).exists()) {
            return getString("path", "/", context);
        } else if (mPath != null && SuFile.open(mPath).exists()) {
            return mPath;
        } else {
            return "/";
        }
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

    public static String getString(String name, String defaults, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(name, defaults);
    }

    public static Uri getImageURI(String path) {
        File mFile = SuFile.open(path);
        if (mFile.exists()) {
            return Uri.fromFile(mFile);
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void saveBoolean(String name, boolean value, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(name, value).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void saveString(String name, String value, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(name, value).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static void setFileIcon(AppCompatImageButton icon, Drawable drawable, Context context) {
        icon.setImageDrawable(drawable);
        icon.setColorFilter(isDarkTheme(context) ? ContextCompat.getColor(context, R.color.colorWhite) :
                ContextCompat.getColor(context, R.color.colorBlack));
    }

    public static void setSelectedFilePath(String path) {
        mSelectedFilePath = path;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void launch() {
        saveString("path", mPath, mContext);
        Intent intent = new Intent(mContext, FilePickerActivity.class);
        mResult.launch(intent);
    }

    public void setAccentColor(int color) {
        mAccentColor = color;
    }

    public void setExtension(String ext) {
        mExtension = ext;
    }

    public void setPath(String path) {
        mPath = path;
    }

}