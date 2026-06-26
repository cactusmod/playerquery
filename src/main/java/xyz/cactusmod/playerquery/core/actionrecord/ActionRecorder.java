package xyz.cactusmod.playerquery.core.actionrecord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ActionRecorder implements Listener {

	private static final Duration WINDOW = Duration.ofMinutes(1);

	private static final int MAX_BLOCK_ACTIONS = 20;
	private static final int MAX_COMMAND_ACTIONS = 10;

	private final Map<UUID, PlayerActions> actions = new ConcurrentHashMap<>();

	public PlayerActions get(Player player) {
		return actions.computeIfAbsent(player.getUniqueId(), ignored -> new PlayerActions());
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		PlayerActions pa = get(player);
		pa.add(new Action(Instant.now(), event.getMessage()), pa.commands);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PlayerActions pa = get(player);
		pa.add(new Action(Instant.now(), event.getBlockPlaced().getType().getId()), pa.blocksPlaced);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerActions pa = get(player);
		pa.add(new Action(Instant.now(), event.getBlock().getType().getId()), pa.blocksBroken);
	}


	public static class PlayerActions {

		private final Deque<Action> commands = new ArrayDeque<>();
		private final Deque<Action> blocksPlaced = new ArrayDeque<>();
		private final Deque<Action> blocksBroken = new ArrayDeque<>();

		private synchronized void prune() {
			Instant cutoff = Instant.now().minus(WINDOW);

			prune(commands, cutoff, MAX_COMMAND_ACTIONS);
			prune(blocksPlaced, cutoff, MAX_BLOCK_ACTIONS);
			prune(blocksBroken, cutoff, MAX_BLOCK_ACTIONS);
		}

		private void add(Action action, Collection<Action> to) {
			to.add(action);
			prune();
		}

		private void prune(Deque<Action> actions, Instant cutoff, int max) {
			while (!actions.isEmpty()
					&& actions.peekLast().time().isBefore(cutoff)) {
				actions.removeLast();
			}

			while (actions.size() > max) {
				actions.removeLast();
			}
		}

		public synchronized List<Action> getCommands() {
			prune();
			return List.copyOf(commands);
		}

		public synchronized List<Action> getBlocksPlaced() {
			prune();
			return List.copyOf(blocksPlaced);
		}

		public synchronized List<Action> getBlocksBroken() {
			prune();
			return List.copyOf(blocksBroken);
		}
	}


	public record Action(Instant time, Object data) {

	}
}