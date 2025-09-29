package ddwu.com.mobile.xmlparsertest.data

data class DailyBoxOffice(
    var rank: Int?,
    var title: String?,
    var openData: String?
){
    override fun toString() = "$rank: $title ($openData)"
}
