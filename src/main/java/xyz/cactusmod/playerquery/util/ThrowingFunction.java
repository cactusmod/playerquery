package xyz.cactusmod.playerquery.util;

public interface ThrowingFunction<T, R, E extends Throwable> {

	R apply(T e) throws E;

}
