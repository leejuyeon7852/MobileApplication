package ddwu.com.mobile.naversearchtest.data

import android.media.Image

/*BOOK DTO -> XML의 item*/
data class Book(
    val title : String?,
    val author : String?,
    val publisher : String?,
    val image : String?,
    val pubDate: String?
){
    override fun toString(): String {
        return "${title} (${author}) - ${publisher} , ${pubDate} ${image}"
    }
}