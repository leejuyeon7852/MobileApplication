package ddwu.com.mobile.naverretrofittest.network

import ddwu.com.mobile.naverretrofittest.data.Root
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IBookSearchService {
    /*네이버 책검색 OpenAPI 를 요청하는 함수 선언 강의자료 11페이지 참고*/
    @GET("v1/search/book.json")
    suspend fun getBooks(
        @Query("query") query: String,
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
    ): Root

}