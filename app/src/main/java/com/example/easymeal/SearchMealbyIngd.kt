package com.example.easymeal

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SearchMealbyIngd : AppCompatActivity() {

    private lateinit var retrieve: Button
    private lateinit var saveMeal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_mealby_ingd)
        supportActionBar?.hide()

        retrieve=findViewById(R.id.retbutton)
        saveMeal=findViewById(R.id.savebutton)

        retrieve.setOnClickListener {
            if(textArea.text?.toString()!=null && textArea.text?.toString()!!.trim().isNotEmpty()){
                textResults.setTextColor(Color.BLACK)
                mutable2.clear()
                bundleResult.clear()
                getMealNameByIngredient(textArea.text.toString())
                textResults.text=bundleResult
            }
        }
    }
}