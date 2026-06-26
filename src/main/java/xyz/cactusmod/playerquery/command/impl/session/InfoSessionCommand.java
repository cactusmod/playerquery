package xyz.cactusmod.playerquery.command.impl.session;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import xyz.cactusmod.playerquery.PlayerQuery;
import xyz.cactusmod.playerquery.command.CommandExecuteException;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.UUID;

public class InfoSessionCommand extends CommandNode {

	public InfoSessionCommand() {
		super("info");
	}

	@Override
	protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder
				.executes(exc(context -> {
					PlayerQueryHandler queryHandler = PlayerQuery.getInstance().getQueryHandler();
					CommandSender sender = context.getSource().getSender();
					UUID uuid = Utils.audienceToUUID(sender);

					QuerySession session = queryHandler.getSession(uuid);
					if(session == null) {
						throw CommandExecuteException.NO_ACTIVE_SESSION;
					}

					Message.info(sender, "Info on Session <arg:0>:", Integer.toString(session.getId()));
					Message.info(sender, "Creator: <arg:0>", Utils.getPlayerName(session.getOwner()));
					Message.info(sender, "Candidates: <arg:0>", Integer.toString(session.getCandidateCount()));
					Message.info(sender, "Queries: <arg:0>", Integer.toString(session.getQueryCount()));
					Message.info(sender, Component.text()
							.append(Component
									.text("[CANDIDATES]")
									.color(NamedTextColor.GREEN)
									.clickEvent(ClickEvent.runCommand("/pq session candidates"))
							)
							.append(Component.space())
							.append(Component
									.text("[DELETE]")
									.color(NamedTextColor.RED)
									.clickEvent(ClickEvent.runCommand("/pq session delete"))
							)
							.append(Component.space())
							.append(Component
									.text("[UNDO QUERY]")
									.color(NamedTextColor.YELLOW)
									.clickEvent(ClickEvent.runCommand("/pq query undo"))
							)
							.build()
					);
				}));
	}

}
