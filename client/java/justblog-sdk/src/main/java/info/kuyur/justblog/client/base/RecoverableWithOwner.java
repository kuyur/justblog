package info.kuyur.justblog.client.base;

public interface RecoverableWithOwner<K, T extends Object> {

	T insertInto(K ownerKey, T object);
	void deleteFrom(K ownerKey, T object);
}
