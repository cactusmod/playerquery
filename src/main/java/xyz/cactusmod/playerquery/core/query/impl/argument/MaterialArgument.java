package xyz.cactusmod.playerquery.core.query.impl.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.util.BrigadierUtils;

import java.util.Arrays;

public class MaterialArgument extends QueryArgument<Material> {

    public MaterialArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public ArgumentType<?> brigadierType() {
        return StringArgumentType.string();
    }

    @Override
    public Material parse(CommandContext<CommandSourceStack> ctx) {

        Material material = Material.matchMaterial(StringArgumentType.getString(ctx, getName()));

        if (material == null)
            throw new IllegalArgumentException("Unknown material");

        return material;
    }

    @Override
    public SuggestionProvider<CommandSourceStack> suggestions() {
        return (context, builder) -> {
            return BrigadierUtils.suggestMatching(Arrays.stream(Material.values()).map(m -> m.name().toLowerCase()), builder);
        };
    }

}