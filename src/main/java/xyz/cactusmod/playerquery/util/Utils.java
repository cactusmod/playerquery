package xyz.cactusmod.playerquery.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;

import java.util.UUID;

public class Utils {

	public static UUID audienceToUUID(Object object) {
		UUID uuid;
		if(object instanceof Player player) {
			uuid = player.getUniqueId();
		} else {
			uuid = PlayerQueryHandler.CONSOLE_UUID;
		}

		return uuid;
	}

	public static String getPlayerName(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if(player.getName() != null) {
			return player.getName();
		} else if(PlayerQueryHandler.CONSOLE_UUID.equals(uuid)) {
			return "CONSOLE";
		} else {
			return uuid.toString();
		}
	}
}
