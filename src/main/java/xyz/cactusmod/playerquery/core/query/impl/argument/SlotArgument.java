package xyz.cactusmod.playerquery.core.query.impl.argument;

public class SlotArgument extends IntegerArgument {

	public SlotArgument(String name, boolean optional) {
		super(name, optional, 1, 9);
	}

}