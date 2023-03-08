package com.example.gitify.di

import android.content.Context
import androidx.room.Room
import com.example.gitify.db.AppDatabase
import com.example.gitify.network.GithubAPI.Companion.BASE_URL
import com.example.gitify.network.GithubAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  @Provides
  @Singleton
  fun provideGithubApi(retrofit: Retrofit): GithubAPI = retrofit.create(GithubAPI::class.java)

  @Singleton
  @Provides
  fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    AppDatabase.DB_NAME
  ).build()

  @Singleton
  @Provides
  fun providesUserDao(appDatabase: AppDatabase) = appDatabase.getUserDao()

  @Singleton
  @Provides
  fun providesRepoDao(appDatabase: AppDatabase) = appDatabase.getRepoDao()
}