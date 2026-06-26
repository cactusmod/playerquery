package xyz.cactusmod.playerquery.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.core.query.QueryOperation;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class QuerySession {

	private final PlayerQueryHandler handler;
	private final int id;
	private final UUID owner;

	private final Object mutex = new Object[0];

	private final List<UUID> players;
	private final BitSet candidates;
	private volatile int candidateCount;
	private final Deque<QueryOperation> history = new ArrayDeque<>();

	public QuerySession(PlayerQueryHandler handler, int id, UUID owner, Collection<UUID> initialPlayers) {
		this.handler = handler;
		this.id = id;
		this.owner = owner;

		this.players = List.copyOf(initialPlayers);
		this.candidates = new BitSet(players.size());
		this.candidates.set(0, players.size());
		this.candidateCount = players.size();
	}

	public CompletableFuture<EvictionResult> narrow(AbstractQuery.Lookup<?> lookup) {
		return CompletableFuture.supplyAsync(() -> {
			synchronized (this.mutex) {
				BitSet removed = new BitSet(players.size());
				int evicted = 0;

				for (int i = candidates.nextSetBit(0); i >= 0; i = candidates.nextSetBit(i + 1)) {
					UUID uuid = players.get(i);
					Player player = Bukkit.getPlayer(uuid);

					if (player == null || !lookup.test(player)) {
						candidates.clear(i);
						removed.set(i);
						evicted++;
					}
				}

				if(evicted > 0) {
					history.push(new QueryOperation(lookup, removed));
					candidateCount -= evicted;
				}

				return new EvictionResult(candidateCount, evicted);
			}
		}, handler.getExecutor());
	}

	public AbstractQuery.Lookup<?> undo() {
		synchronized (this.mutex) {
			QueryOperation operation = history.pollFirst();

			if (operation == null) {
				return null;
			}

			BitSet removed = operation.removed();
			candidates.or(removed);
			candidateCount += removed.cardinality();
			return operation.lookup();
		}
	}

	public Optional<QueryOperation> peekLastOperation() {
		synchronized (mutex) {
			return Optional.ofNullable(history.peekFirst());
		}
	}

	public List<UUID> getCandidates() {
		synchronized (mutex) {
			List<UUID> list = new ArrayList<>(candidateCount);

			for (int i = candidates.nextSetBit(0); i >= 0; i = candidates.nextSetBit(i + 1)) {
				list.add(players.get(i));
			}

			return list;
		}
	}

	public int getCandidateCount() {
		return candidateCount;
	}

	public int getQueryCount() {
		return history.size();
	}

	public int getId() {
		return id;
	}

	public UUID getOwner() {
		return owner;
	}

}