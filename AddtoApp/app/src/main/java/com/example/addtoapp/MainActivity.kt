package com.example.addtoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myButton.setOnClickListener {
            startActivity(
                FlutterActivity.createDefaultIntent(this)
            )
        }

        myButton1.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withNewEngine()
                    .initialRoute("/route_1")
                    .build(this)
            )
        }


        myButton2.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flutter_module")
                    .build(this)
            )
        }

        myButton3.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flutter_module_with_route")
                    .build(this)
            )
        }

        myButton4.setOnClickListener {
            startActivity(
                Intent(this, MainActivity2::class.java)
            )
        }

        myButton5.setOnClickListener {
            startActivity(
                Intent(this, MainActivity3::class.java)
            )
        }
    }
}