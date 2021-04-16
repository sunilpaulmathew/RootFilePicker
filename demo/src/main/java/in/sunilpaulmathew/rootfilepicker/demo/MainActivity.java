package in.sunilpaulmathew.rootfilepicker.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

import in.sunilpaulmathew.rootfilepicker.activities.FilePickerActivity;
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
            Intent intent = new Intent(this, FilePickerActivity.class);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && FilePicker.getSelectedFilePath().exists()) {
            File mSelectedFile = FilePicker.getSelectedFilePath();
            new MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.select_question, mSelectedFile.getName()))
                    .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                    })
                    .setPositiveButton(getString(R.string.select), (dialogInterface, i) -> {
                        // Do something
                    }).show();
        }
    }

}