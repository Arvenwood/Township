package pw.dotdash.township.api.warp;

import org.spongepowered.api.Sponge;
import pw.dotdash.township.api.town.Town;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a service for managing town warps.
 */
public interface WarpService {

    /**
     * The singleton service instance.
     *
     * @return The service instance
     * @throws org.spongepowered.api.service.ProvisioningException If the service isn't registered
     */
    static WarpService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(WarpService.class);
    }

    /**
     * Gets all registered warps.
     *
     * @return All registered warps
     */
    Collection<Warp> getWarps();

    /**
     * Gets the registered warp with the specified unique id.
     *
     * @param uniqueId The UUID to find
     * @return The warp, if available
     */
    Optional<Warp> getWarp(UUID uniqueId);

    /**
     * Gets all warps registered to the specified town.
     *
     * @param town The town to look under
     * @return All warps registered to the town
     */
    Collection<Warp> getWarpsByTown(Town town);

    /**
     * Gets the registered warp in the specified town with the specified name.
     *
     * @param town The town to look under
     * @param name The name to find
     * @return The warp, if available
     */
    Optional<Warp> getWarp(Town town, String name);

    /**
     * Checks whether the specified warp has been registered to the service.
     *
     * @param warp The warp to check
     * @return True if the warp has been registered
     */
    boolean contains(Warp warp);

    /**
     * Registers the specified warp to the service.
     *
     * @param warp The warp to register
     * @return True if the warp was successfully registered
     */
    boolean register(Warp warp);

    /**
     * Unregisters the specified warp from the service.
     *
     * @param warp The warp to unregister
     * @return True if the warp was successfully unregistered
     */
    boolean unregister(Warp warp);
}