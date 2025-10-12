package ddwu.com.mobile.miniproject.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Root(
    val boxOfficeResult: BoxOfficeResult,
)

data class BoxOfficeResult(
    val boxofficeType: String,
    val showRange: String,
    val dailyBoxOfficeList: List<DailyBoxOfficeMovie>,
)

data class DailyBoxOfficeMovie(
    val rank: String,
    @SerializedName("movieNm") val title: String,
    @SerializedName("openDt") val openDate: String,
    val audiAcc: String, // 총 누적 관객 수
    val scrnCnt: String, // 총 상연관 수
) : Serializable{
    override fun toString() = "$rank : $title (누적 $audiAcc 명)"
}

fun DailyBoxOfficeMovie.toMovieEntity() : MovieEntity {
    return MovieEntity(0, title, openDate)
}
