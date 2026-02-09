package coolaid.moresearchbars.api.events;

import coolaid.moresearchbars.api.entries.IKeyEntry;
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