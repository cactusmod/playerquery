package xyz.cactusmod.playerquery.core.query;

import xyz.cactusmod.playerquery.core.query.impl.argument.BiomeArgument;
import xyz.cactusmod.playerquery.core.query.impl.argument.IntegerArgument;
import xyz.cactusmod.playerquery.core.query.impl.argument.MaterialArgument;
import xyz.cactusmod.playerquery.core.query.impl.argument.SlotArgument;

public final class QueryArguments {

    public static MaterialArgument material(String name, boolean optional) {
        return new MaterialArgument(name, optional);
    }

    public static IntegerArgument integer(String name, boolean optional, int min, int max) {
        return new IntegerArgument(name, optional, min, max);
    }

    public static SlotArgument slot(String name, boolean optional) {
        return new SlotArgument(name, optional);
    }

    public static BiomeArgument biome(String name, boolean optional) {
        return new BiomeArgument(name, optional);
    }

}