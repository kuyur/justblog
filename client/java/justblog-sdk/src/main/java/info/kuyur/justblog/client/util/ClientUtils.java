package info.kuyur.justblog.client.util;

import info.kuyur.justblog.client.base.ClientException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.core.MediaType;


import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SM;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class ClientUtils {

	private static final String DEFAULT_ENCODING = "UTF-8";

	public static <T> String toJsonString(T t) {
		return toJsonString(t, t.getClass());
	}

	public static <T> String toJsonString(T t, Type generic) {
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(baos);
			provider.writeTo(t, t.getClass(), generic, new Annotation[0], MediaType.APPLICATION_JSON_TYPE, null, bos);
			return baos.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			throw new ClientException("Serialize to JSON failed.", e);
		}
	}

	/**
	 * JAXBAnnotation and JacksonAnnotation supported.
	 * @param is InputStream
	 * @param type Class type. Generic type supported.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readFromJson(InputStream is, Type type) {
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		try {
			return (T)provider.readFrom(Object.class, type, new Annotation[0], MediaType.APPLICATION_JSON_TYPE, null, is);
		} catch (Exception e) {
			throw new ClientException("Deserialize from JSON failed.", e);
		}
	}

	public static ParameterizedType getParameterizedReturnType(Class<?> clazz, String methodName, Class<?>... param) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, param);
			Type type = method.getGenericReturnType();
			if (type instanceof ParameterizedType) {
				return (ParameterizedType) type;
			} else {
				throw new ClientException(type.getClass() + "is not ParameterizedType");
			}
		} catch (SecurityException e) {
			throw new ClientException(e);
		} catch (NoSuchMethodException e) {
			throw new ClientException(e);
		}
	}

	public static File saveToFile(InputStream inputStream, String targetPath) {
		File file = new File(targetPath);
		if (file.exists()) {
			throw new ClientException("File has existed at: " + targetPath);
		}

		FileOutputStream fos = null;
		int size = 0;
		byte[] buffer = new byte[1024];
		try {
			fos = new FileOutputStream(file);
			while ((size = inputStream.read(buffer)) != -1){
				fos.write(buffer, 0, size);
			}
		} catch (FileNotFoundException e) {
			throw new ClientException(e);
		} catch (IOException e) {
			throw new ClientException(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {}
			}
		}

		return file;
	}

	/**
	 * Only JacksonAnnotation supported.
	 * @param jsonString JSON string
	 * @param type Class type. Generic type is not supported.
	 * @return
	 */
	public static <T> T readJsonFromString(String jsonString, Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonString, type);
		} catch (JsonParseException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		} catch (IOException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		}
	}

	public static <T> T readJsonFromString(String jsonString, TypeReference<T> typeRef) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.<T>readValue(jsonString, typeRef);
		} catch (JsonParseException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		} catch (IOException e) {
			throw new RuntimeException("Deserialize from JSON failed.", e);
		}
	}

	public static Header formatCookies(final List<Cookie> cookies) {
		if (cookies == null || cookies.isEmpty()) {
			return null;
		}
		CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
		buffer.append(SM.COOKIE);
		buffer.append(": ");
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie = cookies.get(i);
			if (i > 0) {
				buffer.append("; ");
			}
			buffer.append(cookie.getName());
			buffer.append("=");
			String s = cookie.getValue();
			if (s != null) {
				buffer.append(s);
			}
		}
		return new BufferedHeader(buffer);
	}

	public static boolean fileExisting(File file) {
		if (file == null) {
			return false;
		}
		return file.exists() ? file.isFile() : false;
	}
}