package com.example.trabalho_commov

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ipvc.estg.room.Main_Menu

class Logged_In_Activity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged__in_)


        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val buttonall: Button = findViewById(R.id.button)
        val button: Button = findViewById(R.id.button2)
        val buttonOut: Button = findViewById(R.id.buttonSIGNOUT)

        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonall.setOnClickListener {
            val intent = Intent(this, ALLNotas::class.java)
            startActivity(intent)
        }

        buttonOut.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.commit()
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }
    }
}