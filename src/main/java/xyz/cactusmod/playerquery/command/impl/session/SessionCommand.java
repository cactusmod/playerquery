package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.command.CommandNode;

public class SessionCommand extends CommandNode {

	public SessionCommand() {
		super("session");
		addChild(new NewSessionCommand());
		addChild(new DeleteSessionCommand());
		addChild(new SessionCandidatesCommand());
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {

	}

}
