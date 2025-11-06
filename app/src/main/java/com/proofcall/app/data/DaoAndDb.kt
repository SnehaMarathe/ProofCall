package com.proofcall.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CallDao {
    @Query("SELECT * FROM calls ORDER BY startedAt DESC")
    fun observeCalls(): Flow<List<Call>>

    @Query("SELECT * FROM calls WHERE id = :id LIMIT 1")
    fun observeCall(id: String): Flow<Call?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(call: Call)

    @Query("DELETE FROM calls WHERE id = :id")
    suspend fun delete(id: String)
}

@Database(entities = [Call::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProofDb : RoomDatabase() {
    abstract fun callDao(): CallDao
}

class CallRepository(private val dao: CallDao) {
    fun calls() = dao.observeCalls()
    fun call(id: String) = dao.observeCall(id)
    suspend fun save(call: Call) = dao.upsert(call)
    suspend fun delete(id: String) = dao.delete(id)
}
