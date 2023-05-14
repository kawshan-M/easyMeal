package com.example.easymeal

import androidx.room.*

@Dao
interface MealDao {

    @Query("Select * from Meal")
    suspend fun allMeal(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(vararg user: Meal)

    @Insert
    suspend fun insertAll(vararg users: Meal)

    @Delete
    suspend fun deleteMeal(vararg user: Meal)

    @Query("Select * From Meal WHERE Meals LIKE :mealName OR mealIngredients LIKE :mealName" )
    suspend fun getMealByIngredient(mealName:String?): List<Meal>

    @Query("Select * From Meal WHERE Meals = :mealName"  )
    suspend fun getMealByName(mealName:String?): Meal?

    @Query("SELECT * FROM Meal WHERE  mealId= :id" )
    suspend fun getMealById(id:Int):Meal?
}