package in.sunilpaulmathew.rootfilepicker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.topjohnwu.superuser.io.SuFile;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.sunilpaulmathew.rootfilepicker.R;
import in.sunilpaulmathew.rootfilepicker.adapters.RecycleViewAdapter;
import in.sunilpaulmathew.rootfilepicker.utils.FilePicker;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class FilePickerFragment extends androidx.fragment.app.Fragment {

    private LinearLayoutCompat mProgress;
    private MaterialTextView mTitle;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mRecycleViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_filepicker, container, false);

        AppCompatImageButton mBack = mRootView.findViewById(R.id.back);
        mTitle = mRootView.findViewById(R.id.title);
        AppCompatImageButton mSortButton = mRootView.findViewById(R.id.sort);
        mProgress = mRootView.findViewById(R.id.progress_layout);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);

        if (FilePicker.getAccentColor(requireActivity()) != Integer.MIN_VALUE) {
            mBack.setColorFilter(FilePicker.getAccentColor(requireActivity()));
            mTitle.setTextColor(FilePicker.getAccentColor(requireActivity()));
        }

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
                requireActivity().setResult(Integer.MIN_VALUE, intent);
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
                    FilePicker.setPath(Objects.requireNonNull(SuFile.open(FilePicker.getPath()).getParentFile()).getPath());
                    reload(requireActivity());
                }
            }
        });

        return mRootView;
    }

    private void finish() {
        requireActivity().finish();
    }

    private void reload(Activity activity) {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        mProgress.setVisibility(View.VISIBLE);
        executors.execute(() -> {
            mRecycleViewAdapter = new RecycleViewAdapter(FilePicker.getData(activity));
            new Handler(Looper.getMainLooper()).post(() -> {
                mRecyclerView.setAdapter(mRecycleViewAdapter);
                mProgress.setVisibility(View.GONE);
                mTitle.setText(FilePicker.isRoot() ? "Root" : FilePicker.isStorageRoot() ? "Storage Root" : SuFile.open(FilePicker.getPath()).getName().toUpperCase());
                if (!executors.isShutdown()) executors.shutdown();
            });
        });
    }
    
}