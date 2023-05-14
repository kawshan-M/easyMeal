package com.example.easymeal

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

class SearchMealbyIngd : AppCompatActivity() {

    private lateinit var retrieve: Button
    private lateinit var saveMeal: Button
    private lateinit var results: TextView
    private lateinit var input: EditText
    private  var fullResult= StringBuilder()
    private  var bundleIng= StringBuilder()
    private  var mList= mutableListOf<String?>()
    private  var bundle= StringBuilder()
    private var mutable2= mutableListOf<String>()
    private var mutable3= mutableListOf<String>()
    private var mutable4= mutableListOf<String>()
    private var ingredient=""
    private var measureC=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_mealby_ingd)
        supportActionBar?.hide()

        retrieve=findViewById(R.id.retbutton)
        saveMeal=findViewById(R.id.savebutton)
        results=findViewById(R.id.textView)
        input=findViewById(R.id.rettext)

        //retrieve button implementation
        retrieve.setOnClickListener {
            if(input.text?.toString()!=null && input.text?.toString()!!.trim().isNotEmpty()){
                results.setTextColor(Color.BLACK)
                //mutable2.clear()
                fullResult.clear()
                getMealApi(input.text.toString())
                results.text=fullResult
            }
        }
    }
    private  fun getMealApi(ingredient:String){
        val urlString="https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient"
        val url= URL(urlString)
        val con: HttpURLConnection =url.openConnection() as HttpURLConnection
        bundleIng.clear()
        runBlocking {
            launch {
                withContext(Dispatchers.IO){
                    val bfReader= BufferedReader(InputStreamReader(con.inputStream))
                    var line:String?=bfReader.readLine()
                    while (line!=null){
                        bundleIng.append(line) //rename.........................................................
                        line=bfReader.readLine()
                    }
                    mealName(bundleIng)
                }

            }
        }
    }
    private fun mealName(bundle:StringBuilder){
        mList.clear()
        if (bundle.isNotEmpty() ) {
            val jObject = JSONObject(bundle.toString())
            if(!jObject.isNull("meals")) {
                val meals = jObject.getJSONArray("meals")
                for (i in 0 until meals.length()) {
                    val meal = meals.getJSONObject(i)
                    val name: String? = meal.getString("strMeal")
                    mList.add(name)
                }
                getMealByName()
            }
            else{
                results.setTextColor(Color.RED)
                fullResult.append("There are no meals with this ingredient")
            }
        }
    }
    private fun getMealByName (){
        for (i in mList){
            val urlString="https://www.themealdb.com/api/json/v1/1/search.php?s=$i"
            val url= URL(urlString)
            val con: HttpURLConnection =url.openConnection() as HttpURLConnection
            bundle.clear()
            runBlocking {
                launch {
                    withContext(Dispatchers.IO){
                        val bf= BufferedReader(InputStreamReader(con.inputStream))
                        var line:String?=bf.readLine()
                        while (line!=null){
                            bundle.append(line)
                            line=bf.readLine()
                        }
                        getByUrl(bundle)
                    }
                }
            }
        }
    }
    private fun getByUrl(bundle:StringBuilder) {
        if (bundle.isNotEmpty() ) {
            val jObject = JSONObject(bundle.toString())
            if(!jObject.isNull("meals")) {
                fullResult.append("Meals are:\n\n")
                val meals = jObject.getJSONArray("meals")
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

                    fullResult.append("Meal- $name\n")
                    fullResult.append("DrinkAlternate- $drinkAlternate\n")
                    fullResult.append("Category- $category\n")
                    fullResult.append("Area- $area\n")
                    fullResult.append("Instructions- $instructions\n")
                    fullResult.append("Tags- $tags\n")
                    fullResult.append("YouTube- $youTube\n")

                    for (j in 1..20) {
                        val ingredient: String? = meal.getString("strIngredient$j")
                        if (ingredient != null && ingredient.isNotEmpty() && ingredient.isNotBlank()) {
                            fullResult.append("Ingredient $j-  " + meal.getString("strIngredient$j") + "\n")
                            mutable3.add(ingredient)

                        }

                    }
                    for (j in 1..20) {
                        val measure: String? = meal.getString("strMeasure$j")
                        if (measure != null && measure.isNotEmpty() && measure.isNotBlank()) {
                            fullResult.append("Measure $j-  " + meal.getString("strMeasure$j") + "\n")
                            mutable4.add(measure)

                        }
                    }
                    for(j in mutable3.indices){
                        ingredient += if(j==mutable3.size-1){

                            mutable3[j]
                        } else{

                            mutable3[j] +","
                        }
                    }
                    for(j in mutable4.indices){
                        measureC += if(j==mutable4.size-1){

                            mutable4[j]
                        } else{

                            mutable4[j] +","
                        }
                    }
                    val food= Meal(
                        Meals=name,
                        DrinkAlternate=drinkAlternate,
                        Category=category,
                        Area=area,
                        Instructions=instructions,
                        MealThumb=thumbImage,
                        Tags=tags,
                        Youtube=youTube,
                        mealIngredients = ingredient,
                        Measures=measureC,
                        Source=source,
                        ImageSource=imageSource,
                        CreativeCommonsConfirmed=creativeCommonsConfirmed,
                        Modifieddate=dateModified
                    )
                    mutable2.add(food.toString())

                    for(j in mutable4.indices){
                        mutable4[j]= null.toString()
                        mutable3[j]= null.toString()
                    }
                    ingredient=""
                    measureC=""
                    fullResult.append("\n\t\t\t\t\t\t\t\t\t\t\t\t***********\n\n")
                }
            }
            else{
                results.setTextColor(Color.RED)
                fullResult.append("There are no meals with this ingredient")
            }
        } else {
            fullResult.append("There are no meals with this ingredient")
        }
    }
}


