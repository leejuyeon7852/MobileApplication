package ddwu.com.mobile.naverretrofittest.data

import com.google.gson.annotations.SerializedName

/*결과 JSON을 분석하여 생성한 DTO 클래스 작성
* 책 정보를 표현하는 DTO에는 toString()을 재정의해 볼 것*/
data class Root(
    @SerializedName("items")
    val books: List<Book>,
)

data class Book(
    val title: String,
    val image: String,
    val author: String,
    val publisher: String,
){
    override fun toString() = "title=$title($author), $image, $publisher"
}
