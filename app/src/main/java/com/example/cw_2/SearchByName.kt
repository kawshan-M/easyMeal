package com.example.cw_2

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
//w1870619_Maleesha_Kawshan_Mobile_CW_easyMeal
//https://drive.google.com/drive/folders/1RGU4c2FXn9OqK1qDY_LPhacQSR_8jq6g?usp=sharing

class SearchByName : AppCompatActivity() {

    private  var text= StringBuilder()
    private  var fullResult= StringBuilder()
    private lateinit var input2: EditText
    private lateinit var results2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_name)
        supportActionBar?.hide()

        //connect xml elements
        input2=findViewById(R.id.search_meal)
        results2=findViewById(R.id.output)

        input2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if(input2.text?.toString()!=null && input2.text?.toString()!!.trim().isNotEmpty()){
                    getMealByIngrediant(input2.text.toString())
                    results2.text=fullResult
                }
            }
        })
    }
    private fun getMealByIngrediant(ingredient:String){
        val urlString="https://www.themealdb.com/api/json/v1/1/search.php?s=$ingredient"
        val url= URL(urlString)

        val con: HttpURLConnection =url.openConnection() as HttpURLConnection
        text.clear()
        runBlocking {
            launch {
                withContext(Dispatchers.IO){
                    val bf= BufferedReader(InputStreamReader(con.inputStream))
                    var line:String?=bf.readLine()
                    while (line!=null){
                        text.append(line)
                        line=bf.readLine()
                    }
                    parseJSON(text)
                }

            }
        }
    }

    private fun parseJSON(bundle:StringBuilder)
    {
        fullResult.clear()
        val jsonObject = JSONObject(bundle.toString())

        if(!jsonObject.isNull("meals")) {
            results2.setTextColor(Color.BLACK)
            fullResult.append("Meals are:\n\n")
            val meals = jsonObject.getJSONArray("meals")
            for (i in 0 until meals.length()) {
                val meal = meals.getJSONObject(i)
                val name: String? = meal.getString("strMeal")
                fullResult.append("Meal- $name\n")
                fullResult.append("\n\t\t\t\t\t\t\t\t\t\t\t\t**********\n\n")
            }
        }
        else{
            results2.setTextColor(Color.RED)
            fullResult.append("No meals with this ingredient")
        }
    }
}