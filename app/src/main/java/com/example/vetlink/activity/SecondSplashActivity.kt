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

class SecondSplashActivity : AppCompatActivity() {

    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = Session(this)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent

            if (session.getToken() == null){
                intent = Intent(this@SecondSplashActivity, LoginActivity::class.java)
            }else{
                intent = Intent(this@SecondSplashActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        },2000)

    }
}