package in.sunilpaulmathew.rootfilepicker.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.topjohnwu.superuser.io.SuFile;

import in.sunilpaulmathew.rootfilepicker.R;
import in.sunilpaulmathew.rootfilepicker.adapters.RecycleViewAdapter;
import in.sunilpaulmathew.rootfilepicker.utils.FilePicker;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class FilePickerFragment extends androidx.fragment.app.Fragment {

    private LinearLayout mProgress;
    private MaterialTextView mTitle;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mRecycleViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("StringFormatInvalid")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_filepicker, container, false);

        AppCompatImageButton mBack = mRootView.findViewById(R.id.back);
        mTitle = mRootView.findViewById(R.id.title);
        AppCompatImageButton mSortButton = mRootView.findViewById(R.id.sort);
        mProgress = mRootView.findViewById(R.id.progress_layout);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), FilePicker.getOrientation(requireActivity()) == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1));
        mRecycleViewAdapter = new RecycleViewAdapter(FilePicker.getData(requireActivity()));
        mRecyclerView.setAdapter(mRecycleViewAdapter);

        mTitle.setText(FilePicker.isRoot() ? "Root" : FilePicker.isStorageRoot() ? "Storage Root" : SuFile.open(FilePicker.getPath()).getName().toUpperCase());

        mRecycleViewAdapter.setOnItemClickListener((position, v) -> {
            if (SuFile.open(FilePicker.getData(requireActivity()).get(position)).isDirectory()) {
                FilePicker.setPath(FilePicker.getData(requireActivity()).get(position));
                reload(requireActivity());
            } else {
                Intent intent = new Intent();
                FilePicker.setSelectedFilePath(FilePicker.getData(requireActivity()).get(position));
                requireActivity().setResult(0, intent);
                finish();
            }
        });

        mBack.setOnClickListener(v -> finish());

        mSortButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireActivity(), mSortButton);
            Menu menu = popupMenu.getMenu();
            menu.add(Menu.NONE, 0, Menu.NONE, "A-Z").setCheckable(true)
                    .setChecked(FilePicker.getBoolean("az_order", true, requireActivity()));
            popupMenu.setOnMenuItemClickListener(item -> {
                FilePicker.saveBoolean("az_order", !FilePicker.getBoolean("az_order", true, requireActivity()), requireActivity());
                reload(requireActivity());
                return false;
            });
            popupMenu.show();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void handleOnBackPressed() {
                if (FilePicker.isRoot()) {
                    finish();
                } else {
                    FilePicker.setPath(SuFile.open(FilePicker.getPath()).getParentFile().getPath());
                    reload(requireActivity());
                }
            }
        });

        return mRootView;
    }

    private void finish() {
        FilePicker.setExtension(null);
        FilePicker.setPath(null);
        requireActivity().finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("StaticFieldLeak")
    private void reload(Activity activity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                mProgress.setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(Void... voids) {
                mRecycleViewAdapter = new RecycleViewAdapter(FilePicker.getData(activity));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mProgress.setVisibility(View.GONE);
                mTitle.setText(FilePicker.isRoot() ? "Root" : FilePicker.isStorageRoot() ? "Storage Root" : SuFile.open(FilePicker.getPath()).getName().toUpperCase());
                mRecyclerView.setAdapter(mRecycleViewAdapter);
            }
        }.execute();
    }
    
}