package me.coolaid.moresearchbars.mixin.multiplayer;

import me.coolaid.moresearchbars.MoreSearchBars;
import me.coolaid.moresearchbars.Constants;
import me.coolaid.moresearchbars.util.INamedServerEntry;
import me.coolaid.moresearchbars.util.ServerSelectionListMixinInvoker;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerSelectionList.class)
public abstract class ServerSelectionListMixin extends AbstractSelectionList<ServerSelectionList.Entry> implements ServerSelectionListMixinInvoker {

    @Unique
    private final List<ServerSelectionList.Entry> moresearchbars$allEntries = new ArrayList<>();

    public ServerSelectionListMixin(net.minecraft.client.Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    // Capture the full list whenever vanilla updates it
    @Inject(method = "updateOnlineServers", at = @At("TAIL"))
    private void onUpdateTail(ServerList servers, CallbackInfo ci) {
        moresearchbars$allEntries.clear();
        moresearchbars$allEntries.addAll(this.children());
        moresearchbars$applyFilter();
    }

    @Override
    public void moresearchbars$applyFilter() {
        String query = MoreSearchBars.getServerSearchString();

        // Clear visible entries only, which avoids a weird icon flickering when server icons are refreshed
        this.clearEntries();

        for (ServerSelectionList.Entry entry : moresearchbars$allEntries) {
            if (query.isEmpty()) {
                this.addEntry(entry);
                continue;
            }

            if (entry instanceof INamedServerEntry namedEntry) {
                ServerData data = namedEntry.moresearchbars$getServerData();
                if (data != null) {
                    boolean matches = Constants.containsNormalized(data.name, query) ||
                            Constants.containsNormalized(data.ip, query);
                    if (matches) this.addEntry(entry);
                }
            } else {
                this.addEntry(entry); // Keeps LAN servers and status visible
            }
        }

        // Add this line: Reset scroll to top after filtering
        this.setScrollAmount(0);
    }
}
