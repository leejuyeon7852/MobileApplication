package ddwu.com.mobile.miniproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: MovieEntity) : Long

    @Update
    suspend fun updateMovie(movie: MovieEntity): Int

    @Delete
    suspend fun deleteMovie(movie: MovieEntity) : Int

    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE movie_title = :title")
    suspend fun getMovieByTitle(title: String): MovieEntity
}