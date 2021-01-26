package com.example.addtoapp

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MyApplication : Application() {
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var flutterEngineWithRoute: FlutterEngine
    lateinit var flutterEngineFragment: FlutterEngine
    private lateinit var flutterEngineFragmentWithRoute: FlutterEngine

    override fun onCreate() {
        super.onCreate()

        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)
        flutterEngineWithRoute = FlutterEngine(this)
        flutterEngineFragment = FlutterEngine(this)
        flutterEngineFragmentWithRoute = FlutterEngine(this)

        // Configure an initial route.
        flutterEngineWithRoute.navigationChannel.setInitialRoute("/route_1")
        flutterEngineFragmentWithRoute.navigationChannel.setInitialRoute("/route_1")

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        flutterEngineWithRoute.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        flutterEngineFragment.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        flutterEngineFragmentWithRoute.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("flutter_module", flutterEngine)

        FlutterEngineCache
            .getInstance()
            .put("flutter_module_with_route", flutterEngineWithRoute)

        FlutterEngineCache
            .getInstance()
            .put("flutter_module_fragment", flutterEngineFragment)

        FlutterEngineCache
            .getInstance()
            .put("flutter_module_fragment_with_route", flutterEngineFragmentWithRoute)

    }
}
