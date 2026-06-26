package xyz.cactusmod.playerquery;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.cactusmod.playerquery.command.CommandManager;
import xyz.cactusmod.playerquery.command.impl.PlayerQueryCommand;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;

public final class PlayerQuery extends JavaPlugin {

	private static PlayerQuery instance;

	private PlayerQueryHandler queryHandler;
	private CommandManager commandManager;

	@Override
	public void onEnable() {
		instance = this;

		queryHandler = new PlayerQueryHandler();
		commandManager = new CommandManager(queryHandler);

		commandManager.add(new PlayerQueryCommand(queryHandler));

		getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			this.commandManager.register(commands.registrar());
		});
	}

	@Override
	public void onDisable() {
		queryHandler.clearSessions();
	}

	public PlayerQueryHandler getQueryHandler() {
		return queryHandler;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public static PlayerQuery getInstance() {
		return instance;
	}

}
