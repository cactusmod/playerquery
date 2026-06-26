package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

public class NewSessionCommand extends CommandNode {

	public NewSessionCommand() {
		super("new");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.executes(exc(context -> {
					QuerySession session = PlayerQuery.getInstance().getQueryHandler().createSession(Utils.audienceToUUID(context.getSource().getSender()));
					Message.success(context.getSource().getSender(), "Session <arg:0> created.", Integer.toString(session.getId()));
				}));
	}

}
