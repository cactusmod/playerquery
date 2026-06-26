package xyz.cactusmod.playerquery.core.query;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class QueryArgument<T> {

    private final String name;
    private final boolean optional;

    public QueryArgument(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public boolean optional() {
        return optional;
    }

    public boolean isPresent(CommandContext<CommandSourceStack> ctx) {
        return ctx.getNodes()
                .stream()
                .anyMatch(node -> node.getNode() instanceof ArgumentCommandNode && node.getNode().getName().equals(name));
    }

    public abstract ArgumentType<?> brigadierType();

    public abstract T parse(CommandContext<CommandSourceStack> ctx);

    public SuggestionProvider<CommandSourceStack> suggestions() {
        return null;
    }

}