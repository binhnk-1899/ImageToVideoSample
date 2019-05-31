package com.binhnk.sample.imagetovideo.di

import android.content.Context
import androidx.room.Room
import com.binhnk.sample.imagetovideo.data.constant.Constants
import com.google.gson.Gson
import org.koin.dsl.module

val repositoryModule = module {
    single { createDatabaseName() }
//    single { createAppDatabase(get(), get()) }
//    single { createUserDao(get()) }
//    single { createUserRepository(get()) }
//    single { Gson() }
}

fun createDatabaseName() = Constants.DATABASE_NAME

//fun createAppDatabase(dbName: String, context: Context) =
//    Room.databaseBuilder(
//        context,
//        UserDatabase::class.java,
//        dbName
//    ).fallbackToDestructiveMigration()
//        .allowMainThreadQueries()
//        .build()
//
//fun createUserDao(db: UserDatabase) = db.userDAO()
//
//fun createUserRepository(userApi: UserApi): UserRepository = UserRepositoryImpl(userApi)
