package ddwu.com.mobile.retrofittest.network

import ddwu.com.mobile.retrofittest.data.Root
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IBoxOfficeService {
    @GET("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.{type}")
    suspend fun getDailyBoxOffice(
        @Path("type") type: String,
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
    ) : Root
}

