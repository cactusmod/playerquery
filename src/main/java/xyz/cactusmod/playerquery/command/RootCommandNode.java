package xyz.cactusmod.playerquery.command;

public abstract class RootCommandNode extends CommandNode {

    private final String[] aliases;

    protected RootCommandNode(String name, String... aliases) {
        super(name);
		this.aliases = aliases;
	}

    public String[] getAliases() {
        return aliases;
    }

}