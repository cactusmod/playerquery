package xyz.cactusmod.playerquery.core.query.impl.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.cactusmod.playerquery.core.query.QueryArgument;

public class IntegerArgument extends QueryArgument<Integer> {

	private final int min;
	private final int max;

	public IntegerArgument(String name, boolean optional, int min, int max) {
		super(name, optional);
		this.min = min;
		this.max = max;
	}

	@Override
	public ArgumentType<?> brigadierType() {
		return IntegerArgumentType.integer(min, max);
	}

	@Override
	public Integer parse(CommandContext<CommandSourceStack> ctx) {
		return IntegerArgumentType.getInteger(ctx, getName());
	}

}