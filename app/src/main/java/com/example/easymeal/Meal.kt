package com.example.easymeal

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity

data class Meal(
    @PrimaryKey (true)  val mealId:Int=0,
    val Meals:String?,
    val DrinkAlternate:String?,
    val Category:String?,
    val Area:String?,
    val Instructions:String?,
    val MealThumb:String?,
    val Tags:String?,
    val Youtube:String?,
    val mealIngredients:String?,
    val Measures:String?,
    val Source:String?,
    val ImageSource:String?,
    val CreativeCommonsConfirmed:String?,
    var Modifieddate:String?
)
