# Root File Picker

[![](https://img.shields.io/badge/Root%20File%20&%20Picker%20-v0.7-green)](https://github.com/sunilpaulmathew/RootFilePicker/releases)
![](https://img.shields.io/github/languages/top/sunilpaulmathew/RootFilePicker)
![](https://img.shields.io/github/contributors/sunilpaulmathew/RootFilePicker)
![](https://img.shields.io/github/license/sunilpaulmathew/RootFilePicker)

A simple Android library to pick files from any directories on root enabled devices.

`Root File Picker` is a simple and very basic file picker made mainly to satisfy the needs of [SmartPack-Kernel Manager](https://github.com/SmartPack/SmartPack-Kernel-Manager) and other application developed by its [developer](https://github.com/sunilpaulmathew). It may not have much more features, except a decent interface and options to pick files from any part of the phone's storage. Also, as the name itself suggests, `Root File Picker` requires a rooted environment to work..

## Download

Step 1: Add it in your root-level build.gradle at the end of repositories:
```
allprojects {
        repositories {
                ...
                maven { url 'https://jitpack.io' }
        }
}
```

Step 2: Add dependency to the app-level build.gradle:
```
dependencies {
        implementation 'com.github.sunilpaulmathew:RootFilePicker:Tag'
}
```
*Please Note: **Tag** should be replaced with the latest **[release tag](https://github.com/sunilpaulmathew/RootFilePicker/releases)** or **[commit id](https://github.com/sunilpaulmathew/RootFilePicker/commits/main)**.*

## Tutorial

### Launch File Picker

```
FilePicker filePicker = new FilePicker(
    activityResultLauncher /* in which the result handled; usage: mandatory */,
    context /* your activity or context; usage: mandatory */
);
filePicker.setExtension(extention); /* target specific file extension; usage: optional; default: null */
filePicker.setPath(path); /* path to open when launching  file picker; usage: optional; default: null */
filePicker.setAccentColor(accentColor); /* apply custom accent color; usage: optional; default: ContextCompat.getColor(this, R.color.colorBlue) */
filePicker.launch();
```

### & do something with the selected file (FilePicker.getSelectedFile()) on activityResultLauncher

```
ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && FilePicker.getSelectedFile().exists()) {
            // Do something with the selected file
            File mSelectedFile = FilePicker.getSelectedFile();
        }
    }
);
```