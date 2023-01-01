package in.sunilpaulmathew.rootfilepicker.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.sunilpaulmathew.rootfilepicker.R;
import in.sunilpaulmathew.rootfilepicker.fragments.FilePickerFragment;
import in.sunilpaulmathew.rootfilepicker.utils.FilePicker;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class FilePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filepicker);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FilePickerFragment()).commit();
    }

}