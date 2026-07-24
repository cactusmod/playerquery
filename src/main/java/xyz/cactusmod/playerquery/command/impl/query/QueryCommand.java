package xyz.cactusmod.playerquery.command.impl.query;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.query.impl.type.BiomeQuery;
import xyz.cactusmod.playerquery.core.query.impl.type.DimensionTypeQuery;
import xyz.cactusmod.playerquery.core.query.impl.type.HotbarQuery;
import xyz.cactusmod.playerquery.core.query.impl.type.XpLevelQuery;

public class QueryCommand extends CommandNode {

	public QueryCommand(PlayerQueryHandler queryHandler) {
		super("query");
		addChild(new QueryUndoCommand());
		addChild(new HotbarQuery(queryHandler));
		addChild(new BiomeQuery(queryHandler));
		addChild(new XpLevelQuery(queryHandler));
		addChild(new DimensionTypeQuery(queryHandler));
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {

	}

}
