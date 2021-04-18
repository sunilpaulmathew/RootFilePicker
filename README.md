# Root File Picker

[![](https://img.shields.io/badge/Root%20File%20&%20Picker%20-v0.3-green)](https://github.com/sunilpaulmathew/RootFilePicker/releases)
![](https://img.shields.io/github/languages/top/sunilpaulmathew/RootFilePicker)
![](https://img.shields.io/github/contributors/sunilpaulmathew/RootFilePicker)
![](https://img.shields.io/github/license/sunilpaulmathew/RootFilePicker)

A simple Android library to pick files from any directories on root enabled devices.

`Root File Picker` is a simple and very basic file picker made mainly to satisfy the needs of [SmartPack-Kernel Manager](https://github.com/SmartPack/SmartPack-Kernel-Manager) and other application developed by its [developer](https://github.com/sunilpaulmathew). It may not have much more features, except a decent interface and options to pick files from any part of the phone's storage. Also, as the name itself suggests, `Root File Picker` requires a rooted environment to work..

## Download
```groovy
android {
    compileOptions {
        // Root File Picker uses Java 8 features
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
   implementation 'com.github.sunilpaulmathew:RootFilePicker:v0.3'
}
```

## Tutorial

### Launch File Picker

```
Intent intent = new Intent(this, FilePickerActivity.class);
startActivityForResult(intent, 0);
```

### Set path to open on launching  file picker
```
FilePicker.setPath("/");
```

### Target specific file extension
```
FilePicker.setExtension(".zip");
```

### & finally do something with the selected file (FilePicker.getSelectedFile()) onActivityResult

```
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && FilePicker.getSelectedFile().exists()) {
            // Do something with the selected file
            File mSelectedFile = FilePicker.getSelectedFile();
        }
    }
```
