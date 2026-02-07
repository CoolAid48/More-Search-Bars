package coolaid.moresearchbars.mixin.multiplayer;

import coolaid.moresearchbars.util.INamedServerEntry;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class ServerSelectionListEntryMixin implements INamedServerEntry {

    @Shadow @Final private ServerData serverData;

    @Override
    public ServerData moresearchbars$getServerData() {
        return this.serverData;
    }
}