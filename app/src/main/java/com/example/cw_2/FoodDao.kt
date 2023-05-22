package com.example.cw_2

import androidx.room.*
//w1870619_Maleesha_Kawshan_Mobile_CW_easyMeal
//https://drive.google.com/drive/folders/1RGU4c2FXn9OqK1qDY_LPhacQSR_8jq6g?usp=sharing

@Dao
interface FoodDao {
    @Query("Select * from Food")
    suspend fun getAll(): List<Food>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(vararg user: Food)

    @Insert
    suspend fun insertAll(vararg users: Food)

    @Delete
    suspend fun deleteFoods(vararg user: Food)

    @Query("Select * From Food WHERE Meal LIKE :mealName OR mealIngredients LIKE :mealName" )
    suspend fun getFoodByNameorIngredient(mealName:String?): List<Food>

    @Query("Select * From Food WHERE Meal = :mealName"  )
    suspend fun getFoodByName(mealName:String?): Food?

    @Query("SELECT * FROM Food WHERE  mealId= :id" )
    suspend fun getFoodById(id:Int):Food?


//resource:https://www.youtube.com/watch?v=NS7yYdW3Lho
}