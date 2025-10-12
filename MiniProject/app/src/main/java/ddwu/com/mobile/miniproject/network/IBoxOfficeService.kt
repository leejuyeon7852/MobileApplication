package ddwu.com.mobile.miniproject.network

import ddwu.com.mobile.miniproject.data.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface IBoxOfficeService {
    @GET("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    suspend fun getDailyBoxOffice(
        @Query("key") key: String,
        @Query("targetDt") targetDate: String,
    ) : Root
}