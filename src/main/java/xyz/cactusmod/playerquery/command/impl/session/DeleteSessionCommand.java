package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandExecuteException;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.UUID;

public class DeleteSessionCommand extends CommandNode {

	public DeleteSessionCommand() {
		super("delete");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.executes(exc(context -> {
					PlayerQueryHandler queryHandler = PlayerQuery.getInstance().getQueryHandler();
					UUID uuid = Utils.audienceToUUID(context.getSource().getSender());

					QuerySession session = queryHandler.getSession(uuid);
					if(session == null) {
						throw new CommandExecuteException("You don't have a session selected.");
					}

					queryHandler.deleteSession(session.getId());
					Message.success(context.getSource().getSender(), "Session <arg:0> deleted", Integer.toString(session.getId()));
				}));
	}

}
