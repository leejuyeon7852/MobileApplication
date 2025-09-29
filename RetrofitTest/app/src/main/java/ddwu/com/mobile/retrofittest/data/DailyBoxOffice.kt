package ddwu.com.mobile.retrofittest.data

import com.google.gson.annotations.SerializedName

data class Root(
    val boxOfficeResult: BoxOfficeResult,
)

data class BoxOfficeResult(
    val dailyBoxOfficeList: List<DailyBoxOfficeMovie>,
)

data class DailyBoxOfficeMovie(
    var rank: String,

    @SerializedName("movieNm") // 원래 이름
    var title: String, //바꾼 것

    @SerializedName("openDt")
    var openDate: String,
){
    override fun toString() = "$rank: $title ($openDate)"
}
