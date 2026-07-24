package xyz.cactusmod.playerquery.core.query.impl.type;

import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.core.query.QueryContext;
import xyz.cactusmod.playerquery.core.query.impl.argument.DimensionTypeArgument;

import java.util.List;

public class DimensionTypeQuery extends AbstractQuery {

	public DimensionTypeQuery(PlayerQueryHandler queryHandler) {
		super(queryHandler, "dimension_type");
	}

	@Override
	protected void defineArguments(List<QueryArgument<?>> args) {
		args.add(new DimensionTypeArgument("dimension", false));
	}

	@Override
	protected Lookup<?> createLookup(QueryContext ctx) {
		World.Environment environment = ctx.get("dimension");
		return new DimensionTypeLookup(this, environment);
	}

	public static class DimensionTypeLookup extends Lookup<DimensionTypeQuery> {

		private final World.Environment environment;

		public DimensionTypeLookup(DimensionTypeQuery query, World.Environment environment) {
			super(query);
			this.environment = environment;
		}

		@Override
		public String describe() {
			return LookupDescriptionBuilder.create()
					.with("dimension", environment)
					.build();
		}

		@Override
		public boolean test(Player player) {
			return player.getWorld().getEnvironment() == environment;
		}

	}

}
