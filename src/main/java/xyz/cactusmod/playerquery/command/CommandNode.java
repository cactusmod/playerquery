package xyz.cactusmod.playerquery.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import xyz.cactusmod.playerquery.util.ThrowingConsumer;
import xyz.cactusmod.playerquery.util.ThrowingFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class CommandNode {

    private final String name;
    private final List<CommandNode> children = new ArrayList<>();

    protected CommandNode(String name) {
        this.name = name;
    }

    public void addChild(CommandNode child) {
        children.add(child);
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(name);

        configure(builder);

        for (CommandNode child : children) {
            builder.then(child.build());
        }

        return builder;
    }

    public Predicate<CommandSourceStack> requiresPermission(String permission) {
        return stack -> stack.getSender().hasPermission(permission);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
        return Commands.literal(name);
    }

    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return Commands.argument(name, type);
    }

    public static Command<CommandSourceStack> exc(ThrowingConsumer<CommandContext<CommandSourceStack>, ?> consumer) {
        return new CommandExecuteWrapper(consumer);
    }

    public String getName() {
        return name;
    }

    protected abstract void configure(LiteralArgumentBuilder<CommandSourceStack> builder);

}