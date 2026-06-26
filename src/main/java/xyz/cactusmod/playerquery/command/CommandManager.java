package xyz.cactusmod.playerquery.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;

import java.util.*;

public class CommandManager implements Iterable<RootCommandNode> {

	private final PlayerQueryHandler queryHandler;
	private final Collection<RootCommandNode> commands;

	public CommandManager(PlayerQueryHandler queryHandler) {
		this.queryHandler = queryHandler;
		this.commands = new HashSet<>();
	}

	public void add(RootCommandNode command) {
		this.commands.add(command);
	}

	public Collection<RootCommandNode> getCommands() {
		return Collections.unmodifiableCollection(this.commands);
	}

	public void register(Commands registrar) {
		getCommands().forEach(command -> {
			String name = command.getName();
			LiteralCommandNode<CommandSourceStack> node = command.build().build();
			registrar.register(node, Arrays.asList(command.getAliases()));
		});
	}

	@Override
	@NotNull
	public Iterator<RootCommandNode> iterator() {
		return this.commands.iterator();
	}

}
