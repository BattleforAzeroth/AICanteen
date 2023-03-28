[English](README.md) | [简体中文](README.zh-CN.md)

# AI Canteen

使用目标检测技术实现更快捷方便的点单方式

基于ncnn实现的Yolov7网络安卓端部署，并实现了实时推理

21类菜品的目标检测，并包含点单界面

![](https://github.com/BattleforAzeroth/AICanteen/blob/assets/demo.gif)

## 安装

只需使用Android Studio打开此项目，项目中已经包含NCNN和OpenCV库

### 如果您需要更改库版本

- https://github.com/Tencent/ncnn/releases

1. 下载`ncnn-YYYYMMDD-android-vulkan.zip`或者自行构建NCNN
2. 将`ncnn-YYYYMMDD-android-vulkan.zip`解压到`app/src/main/jni`，并在`app/src/main/jni/CMakeLists.txt`文件中更改ncnn_DIR路径

- https://github.com/nihui/opencv-mobile

1. 下载`opencv-mobile-XYZ-android.zip`
2. 将`opencv-mobile-XYZ-android.zip`解压到`app/src/main/jni`，并在`app/src/main/jni/CMakeLists.txt`文件中更改OpenCV_DIR路径

## 使用自定义数据

### 训练模型和模型转换

1. 训练YOLOv7-tiny网络模型
2. 将模型转换为NCNN格式，放在`app/src/main/assets`中，我个人的转换方法是使用[PNNX](https://github.com/pnnx/pnnx)

### 映射数组

定义在`app/src/main/java/com/tencent/ncnnyolov7/MenuActivity.java`中

```java
f = new int[]{1, 2, 3, 15, 20, 21, 4, 22, 16, 5, 6, 7, 8, 9, 10, 17, 11, 12, 23, 18, 13};
```

此数组解决了模型中与点单界面中类别号不对应的问题

### 菜品详细信息

菜品信息存放于本地，位于`app/src/main/res/values/strings.xml`中名为`eleme_json`的json字符串，图片通过url获取

更多信息请参考[Linkage-RecyclerView](https://github.com/KunMinX/Linkage-RecyclerView)

## 参考

数据集: https://universe.roboflow.com/skoopin-data/pandadata

YOLOv7: https://github.com/WongKinYiu/yolov7

YOLOv7安卓端部署: https://github.com/xiang-wuu/ncnn-android-yolov7

点单界面架构: https://github.com/KunMinX/Linkage-RecyclerView

## 开源协议

[GPL-3.0](LICENSE)