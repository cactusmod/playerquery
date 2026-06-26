package xyz.cactusmod.playerquery.core.query.impl.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.util.BrigadierUtils;

public class BiomeArgument extends QueryArgument<Biome> {

    public BiomeArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public ArgumentType<?> brigadierType() {
        return StringArgumentType.string();
    }

    @Override
    public Biome parse(CommandContext<CommandSourceStack> ctx) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(NamespacedKey.minecraft(StringArgumentType.getString(ctx, getName())));
    }

    @Override
    public SuggestionProvider<CommandSourceStack> suggestions() {
        return (context, builder) -> {
            return BrigadierUtils.suggestMatching(RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).keyStream().map(NamespacedKey::getKey), builder);
        };
    }

}