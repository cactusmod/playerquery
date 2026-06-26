package xyz.cactusmod.playerquery.command;

public class CommandExecuteException extends RuntimeException {

	public static final CommandExecuteException NO_ACTIVE_SESSION = new CommandExecuteException("You don't have a session selected.");

	private final String[] args;

	public CommandExecuteException(String message, String... args) {
		super(message);
		this.args = args;
	}

	public String[] getArgs() {
		return args;
	}
}
