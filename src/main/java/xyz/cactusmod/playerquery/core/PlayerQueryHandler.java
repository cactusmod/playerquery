package xyz.cactusmod.playerquery.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlayerQueryHandler {

	public static final UUID CONSOLE_UUID = new UUID(0xB19B00B5L, 0xCAFEBABEL);

	private final AtomicInteger sessionCounter;
	private final ExecutorService queryNarrowExecutor;
	private final Map<UUID, QuerySession> selectedSessions;
	private final Map<Integer, QuerySession> createdSessions;

	public PlayerQueryHandler() {
		this.sessionCounter = new AtomicInteger(0);
		this.queryNarrowExecutor = Executors.newCachedThreadPool();
		this.selectedSessions = new ConcurrentHashMap<>();
		this.createdSessions = new ConcurrentHashMap<>();
	}

	public QuerySession createSession(UUID creator) {
		int id = sessionCounter.getAndIncrement();
		QuerySession session = new QuerySession(this, id, creator, Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet()));
		selectedSessions.put(creator, session);
		createdSessions.put(id, session);
		return session;
	}

	public void deleteSession(int id) {
		selectedSessions.entrySet().removeIf(e -> e.getValue().getId() == id);
		createdSessions.remove(id);
	}

	public QuerySession getSession(UUID owner) {
		return selectedSessions.get(owner);
	}

	public QuerySession getSession(int id) {
		return createdSessions.get(id);
	}

	public ExecutorService getExecutor() {
		return queryNarrowExecutor;
	}

	public void clearSessions() {
		selectedSessions.clear();
		createdSessions.clear();
		sessionCounter.set(0);
	}

	public Collection<QuerySession> getSessions() {
		return Collections.unmodifiableCollection(createdSessions.values());
	}

	public Collection<Integer> getSessionIds() {
		return createdSessions.keySet();
	}

}
