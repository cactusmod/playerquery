package xyz.cactusmod.playerquery.core.query.impl.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import org.bukkit.World;
import xyz.cactusmod.playerquery.core.query.ArgumentParseException;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.util.BrigadierUtils;

import java.util.Arrays;

public class DimensionTypeArgument extends QueryArgument<World.Environment> {

    public DimensionTypeArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public ArgumentType<?> brigadierType() {
        return StringArgumentType.string();
    }

    @Override
    public World.Environment parse(CommandContext<CommandSourceStack> ctx) {
        String input = StringArgumentType.getString(ctx, getName());

        return switch (input.toLowerCase()) {
            case "overworld", "normal", "0" -> World.Environment.NORMAL;
            case "nether", "the_nether", "-1" -> World.Environment.NETHER;
            case "end", "the_end", "1" -> World.Environment.THE_END;
            default -> throw new ArgumentParseException("Invalid dimension type '" + input + "'");
        };
    }

    @Override
    public SuggestionProvider<CommandSourceStack> suggestions() {
        return (context, builder) -> {
            return BrigadierUtils.suggestMatching(Arrays.stream(World.Environment.values()).map(m -> m.name().toLowerCase()), builder);
        };
    }

}