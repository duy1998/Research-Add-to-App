# Add to App
## Flutter to Android

- config in app/build.gradle (Flutter currently only supports building ahead-of-time (AOT) compiled libraries for x86_64, armeabi-v7a and arm64-v8a)
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
- Check Java 8 requirement

```dart
android {
  //...
  compileOptions {
    sourceCompatibility 1.8
    targetCompatibility 1.8
  }
}
```

- Create Flutter module
  - Use Android Studio
  - File > New > New Module
  
  <img src="images/1.png" width="500" height="500">
  
  - Next
  
  <img src="images/2.png" width="500" height="500">
  
- Use Flutter Activity
  - Declare in Manifest
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
  
    - Create application
    
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
 - Use FlutterFragment
 
    - Create Activity to host FlutterFragment
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
    
 ## Flutter to IOS
  - Create Flutter module
  ```dart
  cd some/path/
  flutter create --template module my_flutter
  ```
  - Create PodFile
  ```dart
  flutter_application_path = '../my_flutter'
  load File.join(flutter_application_path, '.ios', 'Flutter', 'podhelper.rb')
  target 'MyApp' do
    install_all_flutter_pods(flutter_application_path)
  end
  ```
  
  - Run pod install
  - Create FlutterEngine
  ```dart
  import UIKit
  import Flutter
  // Used to connect plugins (only if you have plugins with iOS platform code).
  import FlutterPluginRegistrant

  @UIApplicationMain
  class AppDelegate: FlutterAppDelegate { // More on the FlutterAppDelegate.
    lazy var flutterEngine = FlutterEngine(name: "my flutter engine")
  
    override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
      // Runs the default Dart entrypoint with a default Flutter route.
      flutterEngine.run();
      // Used to connect plugins (only if you have plugins with iOS platform code).
      GeneratedPluginRegistrant.register(with: self.flutterEngine);
      return super.application(application, didFinishLaunchingWithOptions: launchOptions);
    }
  }
  ```
  - Show a FlutterViewController with your FlutterEngine
