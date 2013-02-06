package info.kuyur.justblog.client.util;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceCloser {

	private static Log log =LogFactory.getLog(ResourceCloser.class);

	private ResourceCloser() {}

	public static void close(InputStream istream) {
		if (istream != null) {
			try {
				istream.close();
			} catch (Exception ignore) {
				log.error(ignore.toString());
			}
		}
	}

	public static void close(OutputStream outstream) {
		if (outstream != null) {
			try {
				outstream.close();
			} catch (Exception ignore) {
				log.error(ignore.toString());
			}
		}
	}
}
