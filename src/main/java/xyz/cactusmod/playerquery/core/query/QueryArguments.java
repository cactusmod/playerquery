package xyz.cactusmod.playerquery.core.query;

import xyz.cactusmod.playerquery.core.query.impl.argument.BiomeArgument;
import xyz.cactusmod.playerquery.core.query.impl.argument.IntegerArgument;
import xyz.cactusmod.playerquery.core.query.impl.argument.MaterialArgument;

public final class QueryArguments {

    public static MaterialArgument material(String name, boolean optional) {
        return new MaterialArgument(name, optional);
    }

    public static IntegerArgument hotbarSlot(String name, boolean optional) {
        return integer(name, optional, 1, 9);
    }

    public static IntegerArgument stackSize(String name, boolean optional) {
        return integer(name, optional, 1, 64);
    }

    public static IntegerArgument integer(String name, boolean optional) {
        return new IntegerArgument(name, optional, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static IntegerArgument integer(String name, boolean optional, int min, int max) {
        return new IntegerArgument(name, optional, min, max);
    }

    public static BiomeArgument biome(String name, boolean optional) {
        return new BiomeArgument(name, optional);
    }

}