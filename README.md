# Add to App
## Flutter to Android

- Thêm config vào app/build.gradle (vì Flutter chỉ hỗ trợ build ahead-of-time (AOT) cho x86_64, armeabi-v7a and arm64-v8a)
```dart
android {
  //...
  defaultConfig {
    ndk {
      // Filter for architectures supported by Flutter.
      abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86_64'
    }
  }
}
```

```dart
dependencies {
  implementation project(':flutter')
}
```
- Kiểm tra Java 8 requirement

```dart
android {
  //...
  compileOptions {
    sourceCompatibility 1.8
    targetCompatibility 1.8
  }
}
```

- Tạo Flutter module
  - Sử dụng Android Studio
  - File > New > New Module
  
  <img src="../images/1.png" width="500">
  
  - Tiếp theo
  
  <img src="../images/2.png" width="500">
  
- Khởi tạo Flutter Activity
  - Khai báo activity trong Manifest
  ```dart
  <activity
  android:name="io.flutter.embedding.android.FlutterActivity"
  android:theme="@style/LaunchTheme"
  android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
  android:hardwareAccelerated="true"
  android:windowSoftInputMode="adjustResize"
  />
  ```
  - Start Flutter Activity
  ```dart
  myButton.setOnClickListener {
    startActivity(
      FlutterActivity.createDefaultIntent(this)
    )
  }
  ```
  - Start Flutter Activity with Route
  ```dart
  myButton.setOnClickListener {
    startActivity(
      FlutterActivity
        .withNewEngine()
        .initialRoute("/my_route")
        .build(this)
    )
  }
  ```
  - (Optional) Start Flutter Activity using a cached FlutterEngine
  
    - Tạo Application
    
    ```dart
    class MyApplication : Application() {
      lateinit var flutterEngine : FlutterEngine

      override fun onCreate() {
        super.onCreate()

      // Instantiate a FlutterEngine.
      flutterEngine = FlutterEngine(this)

      // Start executing Dart code to pre-warm the FlutterEngine.
      flutterEngine.dartExecutor.executeDartEntrypoint(
        DartExecutor.DartEntrypoint.createDefault()
      )

      // Cache the FlutterEngine to be used by FlutterActivity.
      FlutterEngineCache
        .getInstance()
        .put("my_engine_id", flutterEngine)
      }
    }

    ```
    - Start Flutter Activity with Cached
    ```dart
    myButton.setOnClickListener {
      startActivity(
        FlutterActivity
          .withCachedEngine("my_engine_id")
          .build(this)
      )
    }
    ```
    - Initial route with a cached engine
    ```dart
    class MyApplication : Application() {
      lateinit var flutterEngine : FlutterEngine
      override fun onCreate() {
          super.onCreate()
        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)
        // Configure an initial route.
        flutterEngine.navigationChannel.initialRoute = "your/route/here";
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
          DartExecutor.DartEntrypoint.createDefault()
        )
        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache
          .getInstance()
          .put("my_engine_id", flutterEngine)
      }
    }

    ```

