package in.sunilpaulmathew.rootfilepicker.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
        mCard.setOnClickListener(v -> {
            FilePicker filePicker = new FilePicker(
                    filePickerResultLauncher /* in which the result handled; usage: mandatory */,
                    this /* your activity or context; usage: mandatory */
            );
            filePicker.setExtension(null); /* target specific file extension; usage: optional; default: null */
            filePicker.setPath(null); /* path to open when launching  file picker; usage: optional; default: null */
            filePicker.setAccentColor(Integer.MIN_VALUE); /* apply custom accent color; usage: optional; default: ContextCompat.getColor(this, R.color.colorBlue) */
            filePicker.launch();
        });
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