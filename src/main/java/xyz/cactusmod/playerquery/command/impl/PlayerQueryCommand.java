package xyz.cactusmod.playerquery.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.command.RootCommandNode;
import xyz.cactusmod.playerquery.command.impl.query.QueryCommand;
import xyz.cactusmod.playerquery.command.impl.session.SessionCommand;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;

public class PlayerQueryCommand extends RootCommandNode {

	public PlayerQueryCommand(PlayerQueryHandler queryHandler) {
		super("playerquery", "pq");
		addChild(new SessionCommand());
		addChild(new QueryCommand(queryHandler));
	}

	@Override
	public void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder.requires(requiresPermission("playerquery.use"));
	}

}
