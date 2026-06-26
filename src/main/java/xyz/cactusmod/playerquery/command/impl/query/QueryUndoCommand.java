package xyz.cactusmod.playerquery.command.impl.query;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandExecuteException;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.UUID;

public class QueryUndoCommand extends CommandNode {

	public QueryUndoCommand() {
		super("undo");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.executes(exc(context -> {
					PlayerQueryHandler queryHandler = PlayerQuery.getInstance().getQueryHandler();
					UUID uuid = Utils.audienceToUUID(context.getSource().getSender());

					QuerySession session = queryHandler.getSession(uuid);
					if(session == null) {
						throw CommandExecuteException.NO_ACTIVE_SESSION;
					}

					AbstractQuery.Lookup<?> lookup = session.undo();
					if(lookup == null) {
						throw new CommandExecuteException("No queries to undo.");
					}

					Message.success(context.getSource().getSender(), "Undid query \"<arg:0>\"", lookup.describe());
				}));
	}

}
