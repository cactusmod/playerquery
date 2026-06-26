package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.Collection;

public class ListSessionCommand extends CommandNode {

	public ListSessionCommand() {
		super("list");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.executes(exc(context -> {
					Collection<QuerySession> sessions = PlayerQuery.getInstance().getQueryHandler().getSessions();
					CommandSender sender = context.getSource().getSender();

					if(sessions.isEmpty()) {
						Message.error(sender, "There are no sessions.");
						return;
					}

					Message.info(sender, "There are <arg:0> sessions:", Integer.toString(sessions.size()));
					for (QuerySession session : sessions) {
						String creatorName = Utils.getPlayerName(session.getOwner());
						Message.info(
								sender,
								"#<arg:0> - <arg:1> candidates, <arg:2> queries, created by <arg:3>",
								Integer.toString(session.getId()),
								Integer.toString(session.getCandidateCount()),
								Integer.toString(session.getQueryCount()),
								creatorName
						);
					}
				}));
	}

}
