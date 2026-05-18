package me.coolaid.moresearchbars.util;

public interface GameRuleSearchAccess {

    default void moresearchbars$applyFilter() {
    }

    default boolean moresearchbars$matchesGameRuleSearch(String query) {
        return false;
    }

    default boolean moresearchbars$isGameRuleCategory() {
        return false;
    }

    default void moresearchbars$appendGameRuleSearchText(String text) {
    }
}
