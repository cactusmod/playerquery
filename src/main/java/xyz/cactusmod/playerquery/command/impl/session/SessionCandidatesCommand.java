package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandExecuteException;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Paginator;
import xyz.cactusmod.playerquery.util.Utils;

public class SessionCandidatesCommand extends CommandNode {

	private static final Paginator PAGINATOR = new Paginator("/pq candidates", 10);

	public SessionCandidatesCommand() {
		super("candidates");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.then(Commands.argument("page", IntegerArgumentType.integer(1))
						.executes(exc(ctx -> {
							int page = IntegerArgumentType.getInteger(ctx, "page") - 1;
							PAGINATOR.send(ctx.getSource().getSender(), getSession(ctx).getCandidates(), page);
						}))
				)
				.executes(exc(ctx -> {
					PAGINATOR.send(ctx.getSource().getSender(), getSession(ctx).getCandidates(), 0);
				}));
	}

	private QuerySession getSession(CommandContext<CommandSourceStack> context) {
		QuerySession session = PlayerQuery.getInstance().getQueryHandler().getSession(Utils.audienceToUUID(context.getSource().getSender()));

		if (session == null) {
			throw new CommandExecuteException("No active session.");
		}

		return session;
	}
}