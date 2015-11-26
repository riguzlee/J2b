package cn.julytech.lepao.weixin;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.api.AccessTokenApi;

public class MediaApi {
    private static Logger       logger             = Logger.getLogger(MediaApi.class.getName());
    private static final String DEFAULT_CHARSET    = "UTF-8";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

    private static String       get_url            = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=";

    /**
     * 获取临时素材
     * @param media_id 素材Id
     * @return MediaFile
     */
    public static Attachment getMedia(String media_id) {
        String url = get_url + AccessTokenApi.getAccessTokenStr() + "&media_id=" + media_id;
        try {
            logger.info("Downloading...." + url);
            return HttpKit.download(url);
        }
        catch (IOException | ExecutionException | InterruptedException e) {
            logger.error("Download failed:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*
     * private static MediaFile download(String url) throws IOException {
     * MediaFile mediaFile = new MediaFile();
     * URL _url = new URL(url);
     * HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
     * // 连接超时
     * conn.setConnectTimeout(25000);
     * // 读取超时 --服务器响应比较慢，增大时间
     * conn.setReadTimeout(25000);
     * conn.setRequestMethod("GET");
     * conn.setRequestProperty("Content-Type",
     * "application/x-www-form-urlencoded");
     * conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
     * conn.setDoOutput(true);
     * conn.setDoInput(true);
     * conn.connect();
     * 
     * if (conn.getContentType().equalsIgnoreCase("text/plain")) {
     * // 定义BufferedReader输入流来读取URL的响应
     * InputStream in = conn.getInputStream();
     * BufferedReader read = new BufferedReader(new InputStreamReader(in,
     * DEFAULT_CHARSET));
     * String valueString = null;
     * StringBuffer bufferRes = new StringBuffer();
     * while ((valueString = read.readLine()) != null) {
     * bufferRes.append(valueString);
     * }
     * read.close();
     * IOUtils.closeQuietly(in);
     * mediaFile.setError(bufferRes.toString());
     * }
     * else {
     * BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
     * String ds = conn.getHeaderField("Content-disposition");
     * String fullName = ds.substring(ds.indexOf("filename=\"") + 10,
     * ds.length() - 1);
     * String relName = fullName.substring(0, fullName.lastIndexOf("."));
     * String suffix = fullName.substring(relName.length() + 1);
     * 
     * mediaFile.setFullName(fullName);
     * mediaFile.setFileName(relName);
     * mediaFile.setSuffix(suffix);
     * mediaFile.setContentLength(conn.getHeaderField("Content-Length"));
     * mediaFile.setContentType(conn.getHeaderField("Content-Type"));
     * mediaFile.setFileStream(bis);
     * 
     * // IOUtils.closeQuietly(bis);
     * }
     * // 关闭连接
     * if (conn != null) {
     * conn.disconnect();
     * }
     * return mediaFile;
     * }
     */
}
