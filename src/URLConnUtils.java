import java.io.IOException;
import java.net.URLConnection;

class URLConnUtils {

    static URLConnection getURLConnInstance() throws IOException {
        URLConnection urlConnection;

        if (Common.PROXY != null) {
            urlConnection = Common.URL.openConnection(Common.PROXY);
        } else {
            urlConnection = Common.URL.openConnection();
        }

        urlConnection.setConnectTimeout(Common.TIMEOUT);
        urlConnection.setReadTimeout(Common.TIMEOUT);
        urlConnection.setRequestProperty("user-agent", Common.USER_AGENT);

        if (Common.REFERER != null) {
            urlConnection.setRequestProperty("referer", Common.REFERER);
        }
        return urlConnection;
    }
}
