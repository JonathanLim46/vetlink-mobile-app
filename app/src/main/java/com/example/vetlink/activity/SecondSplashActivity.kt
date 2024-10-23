package com.example.vetlink.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.R
import com.example.vetlink.helper.Session
import com.example.vetlink.helper.SessionManager

class SecondSplashActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent

            if (session.getAuthToken() == null){
                intent = Intent(this@SecondSplashActivity, LoginActivity::class.java)
            }else{
                intent = Intent(this@SecondSplashActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        },2000)

    }
}