package com.proofcall.app.di

import android.content.Context
import androidx.room.Room
import com.proofcall.app.data.CallRepository
import com.proofcall.app.data.ProofDb

object ServiceLocator {
    @Volatile private var db: ProofDb? = null
    @Volatile private var repo: CallRepository? = null

    fun repository(context: Context): CallRepository {
        val database = db ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, ProofDb::class.java, "proof.db").build().also { db = it }
        }
        return repo ?: synchronized(this) {
            CallRepository(database.callDao()).also { repo = it }
        }
    }
}
