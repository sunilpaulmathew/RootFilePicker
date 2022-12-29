package in.sunilpaulmathew.rootfilepicker.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

import in.sunilpaulmathew.rootfilepicker.utils.FilePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialCardView mCard = findViewById(R.id.demo_card);
        mCard.setOnClickListener(v -> new FilePicker(
                null,
                null,
                ContextCompat.getColor(this, R.color.colorBlue),
                filePickerResultLauncher,
                this).launch()
        );
    }

    ActivityResultLauncher<Intent> filePickerResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null && FilePicker.getSelectedFile().exists()) {
                    File mSelectedFile = FilePicker.getSelectedFile();
                    new MaterialAlertDialogBuilder(this)
                            .setMessage(getString(R.string.select_question, mSelectedFile.getName()))
                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                            })
                            .setPositiveButton(getString(R.string.select), (dialogInterface, i) -> {
                                // Do your task
                            }).show();
                }
            }
    );

}