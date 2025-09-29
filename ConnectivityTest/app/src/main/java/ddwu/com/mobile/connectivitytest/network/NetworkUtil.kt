package ddwu.com.mobile.connectivitytest.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkUtil (context: Context) {

    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isWifiConn: Boolean = false
    var isMobileConn: Boolean = false

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // 네트워크 연결이 되었을 때 호출되는 callback 함수
        override fun onAvailable(network: Network) {
            val networkCapabilities = connMgr.getNetworkCapabilities(network)
            if (networkCapabilities != null) {
                Log.d("NetworkUtil", "onAvailable: $network")
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isWifiConn = true
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isMobileConn = true
                }
            }
        }
        // 네트워크 연결이 끊어졌을 때 호출되는 callback 함수
        override fun onLost(network: Network) {
            val networkCapabilities = connMgr.getNetworkCapabilities(network)
            if (networkCapabilities != null) {
                Log.d("NetworkUtil", "onLost: $network")
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isWifiConn = false
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isMobileConn = false
                }
            }
        }
    }

    val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()        // 상태를 조사할 network 유형 요청을 생성

    fun checkNetworkStatus() { // 요청
        // network 상태 조사
        connMgr.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopCheckNetworkStatus() { // 요청 중단
        // network 상태 조사 중단 (조사 후 필수 사용)
        connMgr.unregisterNetworkCallback(networkCallback)
    }


    fun downloadText(address: String) : String? {
        var receivedContents : String? = null
        var resultStream : InputStream? = null
        var conn : HttpURLConnection? = null

        try {
            conn = getConnection("GET", address, null)
            resultStream = conn?.inputStream                                    // 응답 결과 스트림 확인
            receivedContents = streamToString (resultStream)     // stream 처리 함수를 구현한 후 사용
        } catch (e: Exception) {        // MalformedURLException, IOExceptionl, SocketTimeoutException 등 처리 필요
            e.printStackTrace()
        } finally {
            if (resultStream != null) {
                try { resultStream.close()}
                catch (e: IOException) { Log.d("NETWORK", e.message!!) }
            }
            if (conn != null) conn.disconnect()
        }

        return receivedContents
    }


    fun downloadImage(address : String) : Bitmap? {
        var receivedBitmap : Bitmap? = null
        var resultStream : InputStream? = null
        var conn: HttpsURLConnection? = null

        try {
            conn = getConnection("GET", address, null)
            resultStream = conn?.inputStream        // 응답 결과 스트림 확인
            receivedBitmap = streamToBitmap (resultStream)   // Stream Bitmap 처리 함수를 구현한 후 사용
        } catch (e : Exception) {
            e.printStackTrace()
        } finally {
            if (resultStream != null) {
                try { resultStream.close()}
                catch (e: IOException) { Log.d("NETWORK", e.message!!) } }
            if (conn != null) conn.disconnect()
        }

        return receivedBitmap
    }



    fun sendPostData(address: String, data: String) : String? {
        var receivedContents : String? = null
        var resultStream : InputStream? = null
        var conn : HttpsURLConnection? = null

        try {
            conn = getConnection("POST", address, data)
            resultStream = conn?.inputStream                         // 응답 결과 스트림 확인
            receivedContents = streamToString (resultStream)     // stream 처리 함수를 구현한 후 사용
        } catch (e: Exception) {        // MalformedURLException, IOExceptionl,
            e.printStackTrace()         // SocketTimeoutException 등 처리 필요
        } finally {
            if (resultStream != null) {
                try { resultStream.close()}
                catch (e: IOException) { Log.d("NETWORK", e.message!!) } }
            if (conn != null) conn.disconnect()
        }
        return receivedContents
    }


    /* requestMethod(GET/POST)에 따라 address 에 접속하여 결과 connection 반환 */
    private fun getConnection(requestMethod: String, address: String, data: String?) : HttpsURLConnection? {
        val url = URL(address)
        var conn = url.openConnection() as HttpsURLConnection   // https connection 생성

        conn.readTimeout = 5000             // 읽기 타임아웃 지정 SocketTimeoutException 발생 가능
        conn.connectTimeout = 3000          // 연결 타임아웃 지정 SocketTimeoutException 발생 가능
        conn.doInput = true                 // 서버의 응답 지정 default
        conn.requestMethod = requestMethod  // requestMethod 지정 (GET/POST)

        if (requestMethod.equals("POST")) { // POST인 경우 데이터 전송 처리 부분 수행
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            val params = "subject=" + data
            val outputStreamWriter : OutputStreamWriter = OutputStreamWriter(conn.outputStream, "UTF-8")
            val writer : BufferedWriter = BufferedWriter(outputStreamWriter)
            writer.write(params)
            writer.flush()
        }

        val responseCode = conn.responseCode        // 서버 요청 및 응답 수신

        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw IOException("HTTP error code: $responseCode")
            return null
        }

        return conn
    }

    // inputStream 을 String 으로 변환
    private fun streamToString(iStream : InputStream?) : String {
        val resultBuilder = StringBuilder()

        val inputStreamReader = InputStreamReader(iStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        var readLine : String? = bufferedReader.readLine()
        while (readLine != null) {
            resultBuilder.append(readLine + System.lineSeparator())
            readLine = bufferedReader.readLine()
        }

        bufferedReader.close()
        return resultBuilder.toString()
    }

    // inputStream 을 Bitmap 으로 변환
    private fun streamToBitmap(iStream: InputStream?) : Bitmap {
        val bitmap = BitmapFactory.decodeStream(iStream)
        return bitmap
    }


 }

