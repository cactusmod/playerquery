package xyz.cactusmod.playerquery.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.ThrowingConsumer;

public class CommandExecuteWrapper implements Command<CommandSourceStack> {

	private final ThrowingConsumer<CommandContext<CommandSourceStack>, ?> callback;

	public CommandExecuteWrapper(ThrowingConsumer<CommandContext<CommandSourceStack>, ?> callback) {
		this.callback = callback;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> commandContext) {
		CommandSender sender = commandContext.getSource().getSender();
		try {
			callback.apply(commandContext);
			return SINGLE_SUCCESS;
		} catch (CommandExecuteException e) {
			Message.error(sender, MiniMessage.miniMessage().escapeTags(e.getMessage()), e.getArgs());
		} catch (Throwable e) {
			Message.send(sender, "Command failed: " + MiniMessage.miniMessage().escapeTags(e.getMessage()));
		}

		return 0;
	}

}
