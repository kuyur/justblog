package info.kuyur.justblog.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamUtils {

	private static final Log log = LogFactory.getLog(StreamUtils.class);

	public static String readInputStream(InputStream is, Charset charset) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, charset));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
		} catch (IOException e) {
			log.error("Error while reading stream", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				log.error("Error while closing stream", e);
			}
		}
		return sb.toString();		
	}
}
