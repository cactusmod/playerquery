package xyz.cactusmod.playerquery.core.query.impl.type;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.block.Biome;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.core.query.QueryArguments;
import xyz.cactusmod.playerquery.core.query.QueryContext;

import java.util.List;

public class BiomeQuery extends AbstractQuery {

    public BiomeQuery(PlayerQueryHandler queryHandler) {
        super(queryHandler, "biome");
    }

    @Override
    protected void defineArguments(List<QueryArgument<?>> args) {
        args.add(QueryArguments.biome("biome", false));
    }

    @Override
    protected Lookup<?> createLookup(QueryContext ctx) {
        Biome biome = ctx.get("biome");

        return new BiomeLookup(this, biome);
    }

    public static class BiomeLookup extends Lookup<BiomeQuery> {

        private final Biome biome;

        protected BiomeLookup(BiomeQuery query, Biome biome) {
            super(query);
            this.biome = biome;
        }

        @Override
        public String describe() {
            return LookupDescriptionBuilder.create()
                    .with("biome", biome, b -> RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).getKey(b).getKey())
                    .build();
        }

        @Override
        public boolean test(Player player) {
            Location location = player.getLocation();

            return location.getBlock().getBiome() == biome;
        }
    }
}