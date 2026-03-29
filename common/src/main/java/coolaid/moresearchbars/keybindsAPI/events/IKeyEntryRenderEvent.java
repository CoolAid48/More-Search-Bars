package coolaid.moresearchbars.keybindsAPI.events;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface IKeyEntryRenderEvent {

    IKeyEntry getEntry();

    GuiGraphicsExtractor getGuiGraphics();

    int getY();

    int getX();

    int getRowLeft();

    int getRowWidth();

    boolean isHovered();

    float getPartialTicks();

}