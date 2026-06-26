package xyz.cactusmod.playerquery.util;

public interface ThrowingConsumer<T, E extends Throwable> {

	void apply(T e) throws E;

}
