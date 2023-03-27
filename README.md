[English](README.md) | [简体中文](README.zh-CN.md)

# AI Canteen

Android Live Demo inferenece of Yolov7 using ncnn

Object detection for 21 classes of dishes and ordering pages included

Faster and more convenient way of ordering based on Object detection

[](demo.gif)

## Install

Simply open this project with Android Studio, NCNN and OpenCV libraries are already included in this project

### If you want to change the library version

- https://github.com/Tencent/ncnn/releases

1. Download `ncnn-YYYYMMDD-android-vulkan.zip` or build NCNN for android yourself
2. Extract `ncnn-YYYYMMDD-android-vulkan.zip` into `app/src/main/jni` and change the ncnn_DIR path to yours in `app/src/main/jni/CMakeLists.txt`

- https://github.com/nihui/opencv-mobile

1. Download `opencv-mobile-XYZ-android.zip`
2. Extract `opencv-mobile-XYZ-android.zip` into `app/src/main/jni` and change the OpenCV_DIR path to yours in `app/src/main/jni/CMakeLists.txt`

## Custom Data

### Train and convert

1. Training YOLOv7-tiny model
2. Convert the model to NCNN format into `app/src/main/assets`. I personally use [PNNX](https://github.com/pnnx/pnnx)

### Mapping Array

Defined in `app/src/main/java/com/tencent/ncnnyolov7/MenuActivity.java`

```java
f = new int[]{1, 2, 3, 15, 20, 21, 4, 22, 16, 5, 6, 7, 8, 9, 10, 17, 11, 12, 23, 18, 13};
```

This array solves the problem that the model does not correspond to the classes number in the order page

### Details of Dishes

The dishes information is locally in the json string named `eleme_json` in `app/src/main/res/values/strings.xml`, and the picture is obtained by url

For more information please refer to [Linkage-RecyclerView](https://github.com/KunMinX/Linkage-RecyclerView)

## Reference

Dataset: https://universe.roboflow.com/skoopin-data/pandadata

YOLOv7: https://github.com/WongKinYiu/yolov7

YOLOv7 for Android: https://github.com/xiang-wuu/ncnn-android-yolov7

Ordering page Architecture: https://github.com/KunMinX/Linkage-RecyclerView

## License

[GPL-3.0](LICENSE)