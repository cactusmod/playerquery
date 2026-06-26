package xyz.cactusmod.playerquery.core.query;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.cactusmod.playerquery.command.CommandNode;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.QuerySession;
import xyz.cactusmod.playerquery.util.Message;
import xyz.cactusmod.playerquery.util.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQuery extends CommandNode {

    private final PlayerQueryHandler queryHandler;
    private final List<QueryArgument<?>> arguments;
    private final int lastLiteralArgumentIndex;

    protected AbstractQuery(PlayerQueryHandler queryHandler, String name) {
        super(name);
        this.queryHandler = queryHandler;

        ArrayList<QueryArgument<?>> arguments = new ArrayList<>();
        defineArguments(arguments);

        boolean foundOptional = false;

        int literalIndex = -1;
        for (QueryArgument<?> arg : arguments) {
            if (arg.optional()) {
                foundOptional = true;
            } else if (foundOptional) {
                throw new IllegalStateException("Required arguments cannot follow optional arguments.");
            } else {
                literalIndex++;
            }
        }

        this.arguments = arguments;
        this.lastLiteralArgumentIndex = literalIndex;
    }

    protected abstract void defineArguments(List<QueryArgument<?>> arguments);
    protected abstract Lookup<?> createLookup(QueryContext context);

    @Override
    protected void configure(LiteralArgumentBuilder<CommandSourceStack> builder) {
        build(builder, 0);
    }

    private void build(ArgumentBuilder<CommandSourceStack, ?> builder, int index) {
        if (index >= arguments.size()) {
            attachExecute(builder);
            return;
        }

        QueryArgument<?> arg = arguments.get(index);
        RequiredArgumentBuilder<CommandSourceStack, ?> next = Commands.argument(arg.getName(), arg.brigadierType());

        if (arg.suggestions() != null) {
            next.suggests(arg.suggestions());
        }

        if (index >= lastLiteralArgumentIndex) {
            attachExecute(next);
        }

        build(next, index + 1);
        builder.then(next);
    }

    private void attachExecute(ArgumentBuilder<CommandSourceStack, ?> builder) {
        builder.executes(exc(ctx -> {
            QueryContext parsed = new QueryContext();
            CommandSender sender = ctx.getSource().getSender();

            for (QueryArgument<?> arg : arguments) {
                if (arg.isPresent(ctx)) {
                    try {
                        Object result = Objects.requireNonNull(arg.parse(ctx));
                        parsed.put(arg.getName(), result);
                    } catch (ArgumentParseException e) {
                        Message.error(sender, MiniMessage.miniMessage().escapeTags(e.getMessage()));
                        return;
                    } catch (Throwable t) {
                        Message.error(sender, "An unexpected error occurred while parsing argument <arg:0>: <arg:1>", arg.getName(), MiniMessage.miniMessage().escapeTags(t.getMessage()));
                        return;
                    }
                }
            }

            Lookup<?> lookup = createLookup(parsed);
            UUID uuid = Utils.audienceToUUID(sender);

            QuerySession session = queryHandler.getSession(uuid);

            if (session == null) {
                session = queryHandler.createSession(uuid);
                Message.success(
                        sender,
                        "Created new session <arg:0>",
                        Integer.toString(session.getId())
                );
            }

            Message.info(sender, "Updating candidates..");

            session.narrow(lookup).thenAccept(result -> {
				Message.success(
						sender,
						"Evicted <arg:0> candidates, <arg:1> left.",
						Integer.toString(result.evicted()),
						Integer.toString(result.left())
				);

				if(result.left() == 0) {
					Message.warning(sender, "No candidates left after applying query \"<arg:0>\". Use <arg:1> to undo this if it was an error", lookup.describe(), "/pq query undo");
				}
            });
        }));
    }

    public abstract static class Lookup<T extends AbstractQuery> implements Predicate<Player> {
        private final T query;

        protected Lookup(T query) {
            this.query = query;
        }

        public abstract String describe();

        public T getQuery() {
            return query;
        }

        protected static class LookupDescriptionBuilder {

            public static LookupDescriptionBuilder create() {
                return new LookupDescriptionBuilder();
            }

            private final List<Entry<?>> entries = new ArrayList<>();

            public <T> LookupDescriptionBuilder with(String name, T value) {
                return with(name, value, Objects::toString);
            }

            public <T> LookupDescriptionBuilder with(String name, T value, Function<T, String> mapper) {
                entries.add(new Entry<>(name, value, mapper));
                return this;
            }

            public String build() {
                Collection<String> displays = new ArrayList<>();
                for (Entry<?> entry : entries) {
                    if(entry.value() != null) {
                        displays.add(entry.display());
                    }
                }
                return String.join(", ", displays);
            }

            private record Entry<T>(String name, T value, Function<T, String> mapper) {
                public String display() {
                    String display;
                    try {
                        display = mapper.apply(value);
                    } catch (Throwable t) {
                        display = Objects.toString(value);
                    }

                    return name + "=" + display;
                }
            }

        }

    }

}