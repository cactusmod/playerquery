package xyz.cactusmod.playerquery.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class Paginator {

    private final String command;
    private final int pageSize;

    public Paginator(String command, int pageSize) {
        this.command = command;
        this.pageSize = pageSize;
    }

    public void send(Audience audience, List<UUID> entries, int page) {
        int pages = Math.max(1, (int) Math.ceil(entries.size() / (double) pageSize));
        page = Math.max(0, Math.min(page, pages - 1));

        int start = page * pageSize;
        int end = Math.min(start + pageSize, entries.size());

        if(entries.isEmpty()) {
            Message.error(audience, "No candidates found.");
            return;
        }

        TextComponent.Builder builder = Component.text().append(Component.text("Candidates (" + entries.size() + ") (" + (page + 1) + "/" + pages + ")\n"));

        for (int i = start; i < end; i++) {
            UUID uuid = entries.get(i);
            Player player = Bukkit.getPlayer(uuid);
            String name = player != null ? player.getName() : uuid.toString();

            builder.append(Component.text((i + 1) + ". "))
                    .append(Component.text(name)
                            .color(NamedTextColor.AQUA)
                            .clickEvent(ClickEvent.runCommand("/tp " + name))
                            .hoverEvent(HoverEvent.showText(Component.text("Teleport to " + name))))
                    .append(Component.newline());
        }

        if (pages > 1) {
            builder.append(Component.text("\n"));

            if (page > 0) {
                builder.append(Component.text("« Previous")
                        .color(NamedTextColor.YELLOW)
                        .clickEvent(ClickEvent.runCommand(command + " " + page)));
            }

            builder.append(Component.text("  "));

            if (page + 1 < pages) {
                builder.append(Component.text("Next »")
                        .color(NamedTextColor.YELLOW)
                        .clickEvent(ClickEvent.runCommand(command + " " + (page + 2))));
            }
        }

        Message.send(audience, builder.build());
    }
}