package xyz.cactusmod.playerquery.core.query.impl.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.cactusmod.playerquery.core.PlayerQueryHandler;
import xyz.cactusmod.playerquery.core.query.AbstractQuery;
import xyz.cactusmod.playerquery.core.query.QueryArgument;
import xyz.cactusmod.playerquery.core.query.QueryArguments;
import xyz.cactusmod.playerquery.core.query.QueryContext;

import java.util.List;

public class HotbarQuery extends AbstractQuery {

    public HotbarQuery(PlayerQueryHandler queryHandler) {
        super(queryHandler, "hotbar");
    }

    @Override
    protected void defineArguments(List<QueryArgument<?>> args) {
        args.add(QueryArguments.material("item", false));
        args.add(QueryArguments.hotbarSlot("slot", true));
        args.add(QueryArguments.stackSize("count", true));
    }

    @Override
    protected Lookup<?> createLookup(QueryContext ctx) {
        Material material = ctx.get("item");
        Integer slot = ctx.get("slot");
        Integer count = ctx.get("count");

        return new HotbarLookup(this, material, slot, count);
    }

    public static class HotbarLookup extends Lookup<HotbarQuery> {

        private final Material material;
        private final Integer count;
        private final Integer slot;

        protected HotbarLookup(HotbarQuery query, Material material, Integer slot, Integer count) {
            super(query);
            this.material = material;
            this.slot = slot;
            this.count = count;
        }

        @Override
        public String describe() {
            return LookupDescriptionBuilder.create()
                    .with("material", material)
                    .with("slot", slot)
                    .with("count", count)
                    .build();
        }

        @Override
        public boolean test(Player player) {
            PlayerInventory inventory = player.getInventory();

            if (slot != null) {
                return matches(inventory.getItem(slot - 1));
            }

            for (int i = 0; i < 9; i++) {
                if (matches(inventory.getItem(i))) {
                    return true;
                }
            }

            return false;
        }

        private boolean matches(ItemStack item) {
            if (item == null || item.getType().isAir()) {
                return false;
            }

            if (item.getType() != material) {
                return false;
            }

            return count == null || item.getAmount() == count;
        }
    }

}