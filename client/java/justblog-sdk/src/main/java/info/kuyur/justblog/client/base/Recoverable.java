package info.kuyur.justblog.client.base;

public interface Recoverable<T extends Object> {

	T execute(T object);
	void cancel(T object);
}
