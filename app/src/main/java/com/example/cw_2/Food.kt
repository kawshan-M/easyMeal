package com.example.cw_2

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity

data class Food(

    @PrimaryKey (true)  val mealId:Int=0,
    val Meal:String?,
    val DrinkAlternate:String?,
    val Category:String?,
    val Area:String?,
    val Instuctions:String?,
    val MealThumb:String?,
    val Tags:String?,
    val Youtube:String?,
    val mealIngredients:String?,
    val Measures:String?,
    val Source:String?,
    val ImageSource:String?,
    val CreativeCommonsConfirmed:String?,
    var dateModified:String?

)

//resource:https://www.youtube.com/watch?v=NS7yYdW3Lho