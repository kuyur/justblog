package info.kuyur.justblog.client.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

@ThreadSafe
public class ConcurrentDistinctCookieStore implements CookieStore, Serializable {

	private static final long serialVersionUID = -245240365730266465L;

	private final ConcurrentHashMap<String, Cookie> cookies;

	public ConcurrentDistinctCookieStore() {
		super();
		this.cookies = new ConcurrentHashMap<String, Cookie>();
	}

	@Override
	public void addCookie(Cookie cookie) {
		if (cookie != null && cookie.getName() != null && !cookie.isExpired(new Date())) {
			cookies.put(cookie.getName(), cookie);
		}
	}

	public void addCookies(Collection<Cookie> cookies) {
		if (cookies == null) {return;}
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		for (Cookie cookie : cookies) {
			if (cookie.getName() == null) {continue;}
			cookieMap.put(cookie.getName(), cookie);
		}
		this.cookies.putAll(cookieMap);
	}

	public void addCookies(Map<String, Cookie> cookies) {
		if (cookies == null) {return;}
		this.cookies.putAll(cookies);
	}
	/**
	 * Thread-safe but not persistence.<br>
	 * Any changes to map will affect ArrayList elements during the ArrayList constructing procedure.
	 */
	@Override
	public List<Cookie> getCookies() {
		return new ArrayList<Cookie>(cookies.values());
	}

	/**
	 * Thread-safe but not persistence.
	 */
	@Override
	public boolean clearExpired(final Date date) {
		if (date == null) {
			return false;
		}
		boolean removed = false;
		for (Iterator<Cookie> iter = cookies.values().iterator(); iter.hasNext();) {
			if (iter.next().isExpired(date)) {
				iter.remove();
				removed = true;
			}
		}
		return removed;
	}

	@Override
	public void clear() {
		cookies.clear();
	}

	@Override
	public String toString() {
		return "";
	}
}
