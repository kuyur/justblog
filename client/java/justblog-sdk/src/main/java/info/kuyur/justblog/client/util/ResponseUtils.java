package info.kuyur.justblog.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.CharArrayBuffer;

public class ResponseUtils {

	private ResponseUtils(){}

	public static String getContentBodyString(HttpResponse response, String defaultCharset) throws IOException{
		if (response == null) {
			throw new IllegalArgumentException("HTTP response may not be null");
		}
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		InputStream is = entity.getContent();
		if (is == null) {
			return null;
		}
		if (defaultCharset == null || "".equals(defaultCharset)) {
			defaultCharset = "UTF-8";
		}
		try {
			if (entity.getContentLength() > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
			}
			int i = (int)entity.getContentLength();
			if (i < 0) {
				i = 4096;
			}
			ContentType contentType = ContentType.get(entity);
			Charset charset = contentType != null ? contentType.getCharset() : null;
			if (charset == null) {
				charset = Charset.forName(defaultCharset);
			}
			Reader reader = new InputStreamReader(is, charset);
			CharArrayBuffer buffer = new CharArrayBuffer(i);
			char[] tmp = new char[1024];
			int l;
			while((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
			return buffer.toString();
		} finally {
			is.close();
		}
	}
}
