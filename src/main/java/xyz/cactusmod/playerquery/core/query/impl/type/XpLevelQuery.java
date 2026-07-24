package xyz.cactusmod.playerquery.core.query.impl.type;

import org.bukkit.entity.Player;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.core.query.QueryArguments;
import xyz.cactusmod.playerquery.core.query.QueryContext;

import java.util.List;

public class XpLevelQuery extends AbstractQuery {

	public XpLevelQuery(PlayerQueryHandler queryHandler) {
		super(queryHandler, "level");
	}

	@Override
	protected void defineArguments(List<QueryArgument<?>> args) {
		args.add(QueryArguments.integer("level", false));
	}

	@Override
	protected Lookup<?> createLookup(QueryContext ctx) {
		Integer level = ctx.get("level");
		return new XpLevelLookup(this, level);
	}

	public static class XpLevelLookup extends Lookup<XpLevelQuery> {

		private final int level;

		public XpLevelLookup(XpLevelQuery query, int level) {
			super(query);
			this.level = level;
		}

		@Override
		public String describe() {
			return LookupDescriptionBuilder.create()
					.with("level", level)
					.build();
		}

		@Override
		public boolean test(Player player) {
			return player.getLevel() == this.level;
		}

	}

}
