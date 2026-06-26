package xyz.cactusmod.playerquery.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class Message {

	private static final Component PREFIX = MiniMessage.miniMessage().deserialize("<color:#03fc88>P</color><color:#57e843>Q</color> <gray>»</gray> ");

	public static final ArgumentResolver ARG_RESOLVER = (args, color, onlyIfAbsent) -> TagResolver.resolver("arg", (resolveArgs, context) -> {
		int i = resolveArgs.popOr("index expected").asInt().orElseThrow(() -> context.newException("invalid integer"));
		TextComponent text = Component.text(args[i]);
		if(onlyIfAbsent) {
			text = text.colorIfAbsent(color);
		} else {
			text = text.color(color);
		}

		return Tag.selfClosingInserting(text);
	});

	public static void error(Audience audience, String message, String... args) {
		error(audience, MiniMessage.miniMessage().deserialize(message, ARG_RESOLVER.build(args, NamedTextColor.WHITE, true)));
	}

	public static void error(Audience audience, Component message) {
		send(audience, message.colorIfAbsent(NamedTextColor.RED));
	}

	public static void warning(Audience audience, String message, String... args) {
		warning(audience, MiniMessage.miniMessage().deserialize(message, ARG_RESOLVER.build(args, NamedTextColor.WHITE, true)));
	}

	public static void warning(Audience audience, Component message) {
		send(audience, message.colorIfAbsent(NamedTextColor.YELLOW));
	}

	public static void info(Audience audience, String message, String... args) {
		info(audience, MiniMessage.miniMessage().deserialize(message, ARG_RESOLVER.build(args, NamedTextColor.WHITE, true)));
	}

	public static void info(Audience audience, Component message) {
		send(audience, message.colorIfAbsent(NamedTextColor.GRAY));
	}

	public static void success(Audience audience, String message, String... args) {
		success(audience, MiniMessage.miniMessage().deserialize(message, ARG_RESOLVER.build(args, NamedTextColor.WHITE, true)));
	}

	public static void success(Audience audience, Component message) {
		send(audience, message.colorIfAbsent(NamedTextColor.GREEN));
	}

	public static void send(Audience audience, String message) {
		send(audience, Component.text(message));
	}

	public static void send(Audience audience, Component message) {
		audience.sendMessage(Component.text().append(PREFIX).append(message).build());
	}

	public interface ArgumentResolver {
		TagResolver build(String[] args, TextColor textColor, boolean onlyIfAbsent);
	}

}
