package coolaid.moresearchbars.keybindsAPI.events;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.gui.GuiGraphics;

public interface IKeyEntryRenderEvent {

    IKeyEntry getEntry();

    GuiGraphics getGuiGraphics();

    int getY();

    int getX();

    int getRowLeft();

    int getRowWidth();

    boolean isHovered();

    float getPartialTicks();

}