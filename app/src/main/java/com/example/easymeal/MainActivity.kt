package com.example.easymeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val ingredientButton = findViewById<Button>(R.id.ingredientbutton)
        ingredientButton.setOnClickListener {
            val start = Intent(this,SearchMealbyIngd::class.java)
            startActivity(start)
        }
        val searchButton = findViewById<Button>(R.id.searchmealbutton)
        searchButton.setOnClickListener {
            val start = Intent(this,SearchForMeal::class.java)
            startActivity(start)
        }
    }
}