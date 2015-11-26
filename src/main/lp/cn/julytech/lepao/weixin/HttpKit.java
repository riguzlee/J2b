package cn.julytech.lepao.weixin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HttpKit {
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 下载资源
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Attachment download(String url)
            throws ExecutionException,
            InterruptedException,
            IOException {
        Attachment att = new Attachment();
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        Future<Response> f = builder.execute();
        if (f.get().getContentType().equalsIgnoreCase("text/plain")) {
            att.setError(f.get().getResponseBody(DEFAULT_CHARSET));
        }
        else {
            BufferedInputStream bis = new BufferedInputStream(f.get().getResponseBodyAsStream());
            String ds = f.get().getHeader("Content-disposition");
            String fullName = ds.substring(ds.indexOf("filename=\"") + 10, ds.length() - 1);
            String relName = fullName.substring(0, fullName.lastIndexOf("."));
            String suffix = fullName.substring(relName.length() + 1);

            att.setFullName(fullName);
            att.setFileName(relName);
            att.setSuffix(suffix);
            att.setContentLength(f.get().getHeader("Content-Length"));
            att.setContentType(f.get().getContentType());
            att.setFileStream(bis);
        }
        http.close();
        return att;
    }

}
