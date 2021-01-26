package com.example.addtoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import io.flutter.embedding.android.FlutterFragment

class MainActivity3 : AppCompatActivity() {
    companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment_2"
    }

    // Declare a local variable to reference the FlutterFragment so that you
    // can forward calls to it later.
    private var flutterFragment: FlutterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        // Get a reference to the Activity's FragmentManager to add a new
        // FlutterFragment, or find an existing one.
        val fragmentManager: FragmentManager = supportFragmentManager

        // Attempt to find an existing FlutterFragment, in case this is not the
        // first time that onCreate() was run.
        flutterFragment = fragmentManager
            .findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?

        // Create and attach a FlutterFragment if one does not exist.
        if (flutterFragment == null) {
            val newFlutterFragment =
                FlutterFragment.withCachedEngine("flutter_module_fragment_with_route")
                    .build<FlutterFragment>()

            flutterFragment = newFlutterFragment
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.container_fragment,
                    newFlutterFragment,
                    TAG_FLUTTER_FRAGMENT
                )
                .commit()
        }
    }
}