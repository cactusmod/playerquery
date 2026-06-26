package xyz.cactusmod.playerquery.util;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class BrigadierUtils {

	public static CompletableFuture<Suggestions> suggestMatching(Stream<String> candidates, SuggestionsBuilder builder) {
		String string = builder.getRemaining().toLowerCase(Locale.ROOT);
		Stream<String> filteredStream = candidates.filter((candidate) -> shouldSuggest(string, candidate.toLowerCase(Locale.ROOT)));
		Objects.requireNonNull(builder);
		filteredStream.forEach(builder::suggest);
		return builder.buildFuture();
	}

	public static boolean shouldSuggest(String remaining, String candidate) {
		for(int i = 0; !candidate.startsWith(remaining, i); ++i) {
			int j = candidate.indexOf(46, i);
			int k = candidate.indexOf(95, i);
			if (Math.max(j, k) < 0) {
				return false;
			}

			if (j >= 0 && k >= 0) {
				i = Math.min(k, j);
			} else {
				i = j >= 0 ? j : k;
			}
		}

		return true;
	}

}