package xyz.darke.DimensionBridgeBC.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import xyz.darke.DimensionBridgeBC.DimensionBridgeBC;
import xyz.darke.DimensionBridgeBC.utils.Portal;

import java.util.List;

public class OnPortalCreateEvent implements Listener {
    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {

        if (event.getReason() == PortalCreateEvent.CreateReason.NETHER_PAIR) {
            event.setCancelled(true);
            return;
        } else if (event.getReason() == PortalCreateEvent.CreateReason.END_PLATFORM) {
            return;
        }

        assert event.getReason() == PortalCreateEvent.CreateReason.FIRE;

        List<BlockState> blocks = event.getBlocks();
        Location location = blocks.get(0).getLocation();
        World world = event.getWorld();
        World.Environment environment = world.getEnvironment();
        if (environment == World.Environment.NORMAL) {
            location.setX(location.getX() / 8);
            location.setX(location.getZ() / 8);
            Portal.requestCreateOrLinkPortal(location, DimensionBridgeBC.world_nether);
        } else {
            location.setX(location.getX() * 8);
            location.setX(location.getZ() * 8);
            Portal.requestCreateOrLinkPortal(location, DimensionBridgeBC.world_overworld);
        }


    }

}
