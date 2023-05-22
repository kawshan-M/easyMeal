package com.example.cw_2

import androidx.room.Database
import androidx.room.RoomDatabase
//w1870619_Maleesha_Kawshan_Mobile_CW_easyMeal
//https://drive.google.com/drive/folders/1RGU4c2FXn9OqK1qDY_LPhacQSR_8jq6g?usp=sharing

@Database(entities = [Food::class], version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
}

//resource:https://www.youtube.com/watch?v=NS7yYdW3Lho