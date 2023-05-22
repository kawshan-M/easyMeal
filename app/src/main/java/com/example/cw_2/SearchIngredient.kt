package com.example.cw_2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
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

class SearchIngredient : AppCompatActivity() {
    private lateinit var searchButton:Button
    private lateinit var saveButton:Button
    private lateinit var textInput:EditText
    private lateinit var textResults:TextView

    private  var sampleText= StringBuilder()
    private  var Text2= StringBuilder()
    private  var finalText= StringBuilder()

    private  var mutable1= mutableListOf<String?>()
    private  var mutable2= mutableListOf<Food>()
    private var mutable3= mutableListOf<String>()
    private var mutable4= mutableListOf<String>()

    private var ingrd=""
    private var measure=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredient)
        supportActionBar?.hide()

        //connect xml elements
        searchButton=findViewById(R.id.btn_retrieve)
        saveButton=findViewById(R.id.btn_save)
        textInput=findViewById(R.id.search_meal)
        textResults=findViewById(R.id.output)//
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "Database").build()
        val foodDao =db.foodDao()


        searchButton.setOnClickListener {
           if(textInput.text?.toString()!=null && textInput.text?.toString()!!.trim().isNotEmpty()){
               textResults.setTextColor(Color.BLACK)
               mutable2.clear()
               finalText.clear()
               getMealNameByIngredient(textInput.text.toString())
               textResults.text=finalText
           }
        }
        saveButton.setOnClickListener {
          runBlocking {
              launch {
                  for (i in mutable2){
                      val existingFood = foodDao.getFoodByName(i.Meal)
                      if (existingFood == null) {
                            foodDao.insertFoods(i)
                      }
                  }
                  Toast.makeText(this@SearchIngredient,"Added to Database", Toast.LENGTH_SHORT).show()
              }
          }
        }
    }
    private fun getFromJSON(bundle:StringBuilder) {
        if (bundle.isNotEmpty() ) {
            val jsonObject = JSONObject(bundle.toString())
            if(!jsonObject.isNull("meals")) {
                finalText.append("Meals are:\n\n")
                val meals = jsonObject.getJSONArray("meals")
                for (i in 0 until meals.length()) {
                    val meal = meals.getJSONObject(i)
                    val name: String? = meal.getString("strMeal")
                    val drinkAlternate: String? = meal.getString("strDrinkAlternate")
                    val category: String? = meal.getString("strCategory")
                    val area: String? = meal.getString("strArea")
                    val instructions: String? = meal.getString("strInstructions")
                    val thumbImage: String? = meal.getString("strMealThumb")
                    val tags: String? = meal.getString("strTags")
                    val youTube: String? = meal.getString("strYoutube")
                    val source: String? = meal.getString("strSource")
                    val imageSource: String? = meal.getString("strImageSource")
                    val creativeCommonsConfirmed: String? = meal.getString("strCreativeCommonsConfirmed")
                    val dateModified: String? = meal.getString("dateModified")

                    finalText.append("Meal- $name\n")
                    finalText.append("DrinkAlternate- $drinkAlternate\n")
                    finalText.append("Category- $category\n")
                    finalText.append("Area- $area\n")
                    finalText.append("Instructions- $instructions\n")
                    finalText.append("Tags- $tags\n")
                    finalText.append("YouTube- $youTube\n")

                    for (j in 1..20) {
                        val ingredient: String? = meal.getString("strIngredient$j")
                        if (ingredient != null && ingredient.isNotEmpty() && ingredient.isNotBlank()) {
                            finalText.append("Ingredient $j-  " + meal.getString("strIngredient$j") + "\n")
                            mutable3.add(ingredient)
                        }
                    }
                    for (j in 1..20) {
                        val measure: String? = meal.getString("strMeasure$j")
                        if (measure != null && measure.isNotEmpty() && measure.isNotBlank()) {
                            finalText.append("Measure $j-  " + meal.getString("strMeasure$j") + "\n")
                            mutable4.add(measure)
                        }
                    }
                    for(j in mutable3.indices){
                        ingrd += if(j==mutable3.size-1){
                            mutable3[j]
                        } else{
                            mutable3[j] +","
                        }
                    }
                    for(j in mutable4.indices){
                        measure += if(j==mutable4.size-1){

                            mutable4[j]
                        } else{
                            mutable4[j] +","
                        }
                    }
                    val food= Food(
                        Meal=name,
                        DrinkAlternate=drinkAlternate,
                        Category=category,
                        Area=area,
                        Instuctions=instructions,
                        MealThumb=thumbImage,
                        Tags=tags,
                        Youtube=youTube,
                        mealIngredients = ingrd,
                        Measures=measure,
                        Source=source,
                        ImageSource=imageSource,
                        CreativeCommonsConfirmed=creativeCommonsConfirmed,
                        dateModified=dateModified
                    )
                    mutable2.add(food)

                    for(j in mutable4.indices){
                        mutable4[j]= null.toString()
                        mutable3[j]= null.toString()
                    }
                    ingrd=""
                    measure=""
                    finalText.append("\n\t\t\t\t\t\t\t\t\t\t\t\t***********\n\n")
                }
            }
            else{
                textResults.setTextColor(Color.RED)
                finalText.append("No meals with this ingredient")
            }
        } else {
            finalText.append("No meals with this ingredient")
        }
    }
    private  fun getMealNameByIngredient(ingredient:String){
        val urlString="https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient"
        val url= URL(urlString)
        val con: HttpURLConnection =url.openConnection() as HttpURLConnection
        Text2.clear()
        runBlocking {
            launch {
                withContext(Dispatchers.IO){
                    val bf= BufferedReader(InputStreamReader(con.inputStream))
                    var line:String?=bf.readLine()
                    while (line!=null){
                        Text2.append(line)
                        line=bf.readLine()
                    }
                    mealName(Text2)
                }
            }
        }
    }

    private fun mealName(bundle:StringBuilder){
        mutable1.clear()
        if (bundle.isNotEmpty() ) {
            val jsonObject = JSONObject(bundle.toString())
            if(!jsonObject.isNull("meals")) {

                val meals = jsonObject.getJSONArray("meals")
                for (i in 0 until meals.length()) {

                    val meal = meals.getJSONObject(i)

                    val name: String? = meal.getString("strMeal")
                    mutable1.add(name)
                }
                getMealByName()
            }
            else{
                textResults.setTextColor(Color.RED)
                finalText.append("No meals with this ingredient")
            }
        }
    }

    private fun getMealByName (){
      for (i in mutable1){
          val urlString="https://www.themealdb.com/api/json/v1/1/search.php?s=$i"
          val url= URL(urlString)
          val con: HttpURLConnection =url.openConnection() as HttpURLConnection
          sampleText.clear()
          runBlocking {
              launch {
                  withContext(Dispatchers.IO){
                      val bf= BufferedReader(InputStreamReader(con.inputStream))
                      var line:String?=bf.readLine()
                      while (line!=null){
                          sampleText.append(line)
                          line=bf.readLine()
                      }
                      getFromJSON(sampleText)
                  }
              }
          }
      }
    }

    //orientation changes settings
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("BUILDER", sampleText.toString())
        outState.putString("Text", textInput.text.toString())
        outState.putString("BUILDER_INGREDIENT", Text2.toString())
        outState.putString("BUILDER_RESULTS", finalText.toString())
        outState.putString("STRING_INGREDIENT",ingrd)
        outState.putString("STRING_MEASURE",measure)
        outState.putStringArrayList("BUILDER_RESULT_NAME", ArrayList(mutable1))
        outState.putStringArrayList("BUILDER_INGREDIENT", ArrayList(mutable3))
        outState.putStringArrayList("BUILDER_MEASURE", ArrayList(mutable4))
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val getText=savedInstanceState.getString("Text")
        // Restore the state of the UI
        getMealNameByIngredient(getText.toString())
        textResults.text=savedInstanceState.getString("BUILDER_RESULTS")
    }



}