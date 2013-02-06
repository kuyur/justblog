package info.kuyur.justblog.client.init;

import info.kuyur.justblog.client.annotation.Proxy;
import info.kuyur.justblog.client.annotation.ProxyImplType;
import info.kuyur.justblog.client.base.ClientException;
import info.kuyur.justblog.client.base.Recoverable;
import info.kuyur.justblog.client.base.RecoverableWithOwner;
import info.kuyur.justblog.client.stateful.BaseClient;
import info.kuyur.justblog.client.stateful.ISessionClient;
import info.kuyur.justblog.client.util.ClientUtils;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Initializer {

	private final Log logger = LogFactory.getLog(getClass());

	private ISessionClient baseClient;
	private LinkedBlockingDeque<Triple<Object, String, Object>> insertedObjectTriples;
	private ConcurrentHashMap<String, Object> proxyClients;

	public Initializer(String hostname, int port, boolean isSecure, String basePath,
			String account, String password) {
		baseClient = new BaseClient(hostname, port, isSecure, basePath, account, password);
		baseClient.login();
		insertedObjectTriples = new LinkedBlockingDeque<Triple<Object, String, Object>>();
		proxyClients = new ConcurrentHashMap<String, Object>();
	}

	/**
	 * Base client must have logined before you call initialize function.
	 * @param baseClient
	 */
	public Initializer(ISessionClient baseClient) {
		this.baseClient = baseClient;
		insertedObjectTriples = new LinkedBlockingDeque<Triple<Object, String, Object>>();
		proxyClients = new ConcurrentHashMap<String, Object>();
	}

	/**
	 * Initialize the object into Remote Server by RESTful API and return the initialized result.
	 * @param obj
	 * @return initialized result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T initialize(T obj) {
		if (obj == null) {
			throw new ClientException("Object to be initialize is null.");
		}
		try {
			Class objClass = obj.getClass();

			Proxy proxyClientAnnotation = (Proxy)objClass.getAnnotation(Proxy.class);
			if (proxyClientAnnotation == null) {
				throw new ClientException("Annotation of ProxyClient not found in " + objClass);
			}
			ProxyImplType implType = proxyClientAnnotation.implType();
			if (implType != ProxyImplType.Recoverable) {
				throw new ClientException("Value of implType should be Recoverable in " + objClass);
			}
			String proxyClientName = proxyClientAnnotation.proxy();
			Class clientClass = Class.forName(proxyClientName);
			Class[] interfaces = clientClass.getInterfaces();
			boolean implemented = false;
			for (Class _interface: interfaces) {
				if (_interface.equals(Recoverable.class)) {
					implemented = true;
					break;
				}
			}
			if (!implemented) {
				throw new ClientException("Interface of Recoverable not implemented yet in " + clientClass);
			}

			Recoverable client = (Recoverable)proxyClients.get(proxyClientName);
			if (client == null) {
				Constructor con = clientClass.getConstructor(ISessionClient.class);
				proxyClients.putIfAbsent(proxyClientName, (Recoverable) con.newInstance(baseClient));
				client = (Recoverable)proxyClients.get(proxyClientName);
			}

			logger.info("Insert object of " + objClass + ":" + obj.toString());
			T insertedResult = ((Recoverable<T>)client).execute(obj);
			insertedObjectTriples.add(new Triple(insertedResult, proxyClientName, null));
			return insertedResult;
		} catch (Exception e) {
			throw ClientException.create(e);
		}
	}

	public <T> T initialize(String json, Class<T> type) {
		try {
			T obj = ClientUtils.readJsonFromString(json, type);
			return initialize(obj);
		} catch (Exception e) {
			throw ClientException.create(e);
		}
	}

	/**
	 * Initialize the object into Remote Server by RESTful API and return the initialized result.
	 * @param ownerKey
	 * @param obj
	 * @return initialized result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <K, T> T initialize(K ownerKey, T obj) {
		if (obj == null) {
			throw new ClientException("Object to be initialize is null.");
		}
		try {
			Class objClass = obj.getClass();

			Proxy proxyClientAnnotation = (Proxy)objClass.getAnnotation(Proxy.class);
			if (proxyClientAnnotation == null) {
				throw new ClientException("Annotation of ProxyClient not found in " + objClass);
			}
			ProxyImplType implType = proxyClientAnnotation.implType();
			if (implType != ProxyImplType.RecoverableWithOwner) {
				throw new ClientException("Value of implType should be RecoverableWithOwner in " + objClass);
			}
			String proxyClientName = proxyClientAnnotation.proxy();
			Class clientClass = Class.forName(proxyClientName);
			Class[] interfaces = clientClass.getInterfaces();
			boolean implemented = false;
			for (Class _interface: interfaces) {
				if (_interface.equals(RecoverableWithOwner.class)) {
					implemented = true;
					break;
				}
			}
			if (!implemented) {
				throw new ClientException("Interface of RecoverableWithOwner not implemented yet in " + clientClass);
			}

			RecoverableWithOwner client = (RecoverableWithOwner)proxyClients.get(proxyClientName);
			if (client == null) {
				Constructor con = clientClass.getConstructor(ISessionClient.class);
				proxyClients.putIfAbsent(proxyClientName, (RecoverableWithOwner) con.newInstance(baseClient));
				client = (RecoverableWithOwner)proxyClients.get(proxyClientName);
			}

			logger.info("Insert object of " + objClass + ":" + obj.toString());
			T insertedResult = ((RecoverableWithOwner<K, T>)client).insertInto(ownerKey, obj);
			insertedObjectTriples.add(new Triple(insertedResult, proxyClientName, ownerKey));
			return insertedResult;
		} catch (Exception e) {
			throw ClientException.create(e);
		}
	}

	public <K, T> T initialize(String json, K ownerKey, Class<T> type) {
		try {
			T obj = ClientUtils.readJsonFromString(json, type);
			return initialize(ownerKey, obj);
		} catch (Exception e) {
			throw ClientException.create(e);
		}
	}

	/**
	 * Remove objects by LIFO (Last In, First Out) order.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recovery() {
		try {
			Triple<Object, String, Object> triple;
			while ((triple = insertedObjectTriples.pollLast()) != null) {
				Object obj = triple.getFirst();
				String proxyClientName = triple.getSecond();
				Object ownerKey = triple.getThird();

				logger.info("Delete object of " + obj.getClass() + ":" + obj);
				if (ownerKey != null) {
					RecoverableWithOwner client = (RecoverableWithOwner)proxyClients.get(proxyClientName);
					client.deleteFrom(ownerKey, obj);
				} else {
					Recoverable client = (Recoverable)proxyClients.get(proxyClientName);
					client.cancel(obj);
				}
			}
		} catch (Exception e) {
			throw ClientException.create(e);
		}
	}

	private class Triple<T1, T2, T3>{
		private T1 first;
		private T2 second;
		private T3 third;

		public Triple(T1 first, T2 second, T3 third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public T1 getFirst() {
			return first;
		}

		public T2 getSecond() {
			return second;
		}

		public T3 getThird() {
			return third;
		}
	}
}
