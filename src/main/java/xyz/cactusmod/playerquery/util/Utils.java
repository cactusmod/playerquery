package xyz.cactusmod.playerquery.util;

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

}
