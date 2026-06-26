package xyz.cactusmod.playerquery.core.query;

import java.util.BitSet;

public record QueryOperation(AbstractQuery.Lookup<?> lookup, BitSet removed) {

}