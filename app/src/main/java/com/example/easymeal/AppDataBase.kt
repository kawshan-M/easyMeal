package com.example.easymeal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Meal::class], version=1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun mealDao(): MealDao
}
