package ddwu.com.mobile.movieparsertest.data.network

import android.util.Xml
import ddwu.com.mobile.xmlparsertest.data.DailyBoxOffice
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream


class MovieParser {
    private val ns: String? = null

    /*목표태그 상수 선언*/
    companion object{
        val LIST_TAG = "dailyBoxOfficeList"
        val ITEM_TAG = "dailyBoxOffice"
        val RANK_TAG = "rank"
        val TITLE_TAG = "movieNm"
        val OPENDT_TAG = "openDt"
    }


    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream?) : List<DailyBoxOffice> {

        inputStream.use { inputStream ->
            val parser : XmlPullParser = Xml.newPullParser()
            /*Parser 의 동작 정의, next() 호출 전 반드시 호출 필요*/
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)   // Paring 입력 inputStream 설정
            while (parser.name != LIST_TAG) {  // 항목태그를 갖고 있는 목록태그까지 이동
                parser.next()
            }
            val movies : List<DailyBoxOffice> = readBoxOffice(parser)/*태그파싱 함수 호출 및 결과 저장*/
            return movies
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readBoxOffice(parser: XmlPullParser) : List<DailyBoxOffice> {
        val movies = mutableListOf<DailyBoxOffice>()    // 목표 항목을 보관할 리스트 선언

        parser.require(XmlPullParser.START_TAG, ns, LIST_TAG)  // 대상태그 상위태그인지 확인, 아닐 경우 예외 발생

        // 대상태그들을 포함하고 있는 상위태그가 닫힐 때까지 반복
        while(parser.next() != XmlPullParser.END_TAG /*닫는태그 유형*/) {
            if (parser.eventType != XmlPullParser.START_TAG) {  // 시작태그 유형 이외 skip
                continue
            }
            if (parser.name == ITEM_TAG/*항목태그명*/) {      // 대상태그(dailyBoxOffice) 확인
                movies.add( readDailyBoxOffice(parser) /*항목태그 Parsing함수 호출*/ )    // 필요태그 Parsing 결과를 리스트에 추가
            } else {
                skip(parser)    // 항목태그가 아닐 경우 skip
            }
        }

        return movies
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDailyBoxOffice(parser: XmlPullParser) : DailyBoxOffice {
        parser.require(XmlPullParser.START_TAG, ns, ITEM_TAG)   // 항목태그인지 확인
        /*관심태그 값 보관변수 선언*/
        var rank: Int? = null
        var title : String? = null
        var openDate : String? = null

        
        while (parser.next() != XmlPullParser.END_TAG) {    // 닫는 항목태그가 나올 때까지 반복
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {    // 태그명 확인 후 관심태그일 경우 값 확인 및 저장
                /*관심태그일 경우 관심태그 사이의 값 추출함수 호출 및 추출값 저장*/
                RANK_TAG -> rank = readTextInTag(parser, RANK_TAG).toInt()
                TITLE_TAG -> title = readTextInTag(parser, TITLE_TAG)
                OPENDT_TAG -> openDate = readTextInTag(parser, OPENDT_TAG)
                else -> skip(parser)
            }
        }
        return DailyBoxOffice(rank, title, openDate)/*rank, title, openDt 로 DTO 생성*/   // DTO에 관심태그 값 보관 및 반환
    }


    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTextInTag (parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)    // 현재 태그 확인
        var text = ""

        /*Parsing 이벤트가 TEXT 일 경우 값 추출 및 다음으로 이동*/
        if(parser.next() == XmlPullParser.TEXT){
            text = parser.text
            parser.nextTag()
        }

        parser.require(XmlPullParser.END_TAG, ns, tag)  // 현재 태그의 닫는 태그 확인
        return text
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {    // 현재태그 하위의 태그들을 모두 skip
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }


}

