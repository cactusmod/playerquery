package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandExecuteException;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.core.query.impl.argument.IntegerArgument;
import xyz.cactusmod.playerquery.util.BrigadierUtils;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.stream.Stream;

public class SelectSessionCommand extends CommandNode {

	private static final SuggestionProvider<CommandSourceStack> IDS = (c, b) -> BrigadierUtils.suggestMatching(PlayerQuery.getInstance().getQueryHandler().getSessionIds().stream().map(String::valueOf), b);

	public SelectSessionCommand() {
		super("select");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.then(argument("session", IntegerArgumentType.integer())
						.suggests(IDS)
						.executes(exc(context -> {
							int id = IntegerArgumentType.getInteger(context, "session");
							QuerySession session = PlayerQuery.getInstance().getQueryHandler().getSession(id);

							if(session == null) {
								throw new CommandExecuteException("No Session with ID <arg:0> found.", Integer.toString(id));
							}

							PlayerQuery.getInstance().getQueryHandler().selectSession(session.getId(), Utils.audienceToUUID(context.getSource().getSender()));
							Message.success(context.getSource().getSender(), "Session <arg:0> selected.", Integer.toString(session.getId()));
						}))
				);
	}

}
