package coolaid.moresearchbars.util;

public interface GameRuleListMixinInvoker {

    void moresearchbars$captureEntries();

    void moresearchbars$applyFilter();

    int moresearchbars$getX();

    void moresearchbars$setPosition(int x, int y);

    void moresearchbars$setHeight(int height);

    void moresearchbars$setScrollAmount(double amount);
}