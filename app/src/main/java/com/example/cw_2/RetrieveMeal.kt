package com.example.cw_2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
//w1870619_Maleesha_Kawshan_Mobile_CW_easyMeal
//https://drive.google.com/drive/folders/1RGU4c2FXn9OqK1qDY_LPhacQSR_8jq6g?usp=sharing

class RetrieveMeal : AppCompatActivity() {
    private lateinit var searchButton:Button
    private lateinit var input:EditText
    private lateinit var results:TextView
    private var image:String?=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve_meal)
        supportActionBar?.hide()

        //connect xml elements
        searchButton =findViewById(R.id.btn_retrieve)
        input=findViewById(R.id.search_meal)
        results=findViewById(R.id.output)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "Database").build()
        val foodDao =db.foodDao()

        searchButton.setOnClickListener {
            if(input.text?.toString()!=null && input.text?.toString()!!.trim().isNotEmpty()){
                val mealName=input.text.toString()
                runBlocking {
                    launch {
                        results.text=""
                     val foods:List<Food> = foodDao.getFoodByNameorIngredient("%${mealName}%")
                        for(food in foods){
                            results.append("${food.Meal}\n")
                            image=food.MealThumb
                          launch {
                              val bitmap=getUrl(image)
                              runOnUiThread{
                                  findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                              }
                          }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUrl(url: String?): Bitmap? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(url)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                connection?.disconnect()
            }
        }
    }
}