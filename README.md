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
  
  <img src="images/1.png" width="500" height="500">
  
  - Tiếp theo
  
  <img src="images/2.png" width="500" height="500">
  
- Sử dụng Flutter Activity
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
  In Flutter module
  ```dart
  // With Route
  void main() => runApp(chooseWidget(window.defaultRouteName));

  Widget chooseWidget(String route) {
    switch (route) {
      // name of the route defined in the host app
      case '/route_1':
        return MyApp();

      default:
        return MaterialApp(
          home: Scaffold(
            body: Center(
              child: Text('Unknown'),
            ),
          ),
        );
    }
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
 - Sử dụng FlutterFragment
 
    - Tạo Activity chứa FlutterFragment
    ```dart
    class MyActivity : FragmentActivity() {
      companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
      }

      // Declare a local variable to reference the FlutterFragment so that you
      // can forward calls to it later.
      private var flutterFragment: FlutterFragment? = null

      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate a layout that has a container for your FlutterFragment. For
        // this example, assume that a FrameLayout exists with an ID of
        // R.id.fragment_container.
        setContentView(R.layout.my_activity_layout)

        // Get a reference to the Activity's FragmentManager to add a new
        // FlutterFragment, or find an existing one.
        val fragmentManager: FragmentManager = supportFragmentManager

        // Attempt to find an existing FlutterFragment, in case this is not the
        // first time that onCreate() was run.
        flutterFragment = fragmentManager
          .findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?

        // Create and attach a FlutterFragment if one does not exist.
        if (flutterFragment == null) {
          var newFlutterFragment = FlutterFragment.createDefault()
          flutterFragment = newFlutterFragment
          fragmentManager
            .beginTransaction()
            .add(
              R.id.fragment_container,
                newFlutterFragment,
                TAG_FLUTTER_FRAGMENT
              )
              .commit()
          }
        }
      }
    ```
    
    ```dart
    val flutterFragment = FlutterFragment.withNewEngine()
    .initialRoute("myInitialRoute/")
    .build()
    ```
    
    
  
    - Using a pre-warmed FlutterEngine
  
    ```dart
    // Somewhere in your app, before your FlutterFragment is needed,
    // like in the Application class ...
    // Instantiate a FlutterEngine.
    val flutterEngine = FlutterEngine(context)

    // Start executing Dart code in the FlutterEngine.
    flutterEngine.getDartExecutor().executeDartEntrypoint(
        DartEntrypoint.createDefault()
    )

    // Cache the pre-warmed FlutterEngine to be used later by FlutterFragment.
    FlutterEngineCache
      .getInstance()
      .put("my_engine_id", flutterEngine)
  
    ```
    
    ```dart
    FlutterFragment.withCachedEngine("my_engine_id").build()
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

