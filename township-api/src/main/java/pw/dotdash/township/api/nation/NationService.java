package pw.dotdash.township.api.nation;

import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a service for managing nations.
 */
public interface NationService {

    /**
     * The singleton service instance.
     *
     * @return The service instance
     * @throws org.spongepowered.api.service.ProvisioningException If the service isn't registered
     */
    static NationService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(NationService.class);
    }

    /**
     * Gets all registered nations.
     *
     * @return All registered nations
     */
    Collection<Nation> getNations();

    /**
     * Gets the registered nation with the specified unique id.
     *
     * @param uniqueId The UUID to find
     * @return The nation, if available
     */
    Optional<Nation> getNation(UUID uniqueId);

    /**
     * Gets the registered nation with the specified name.
     *
     * @param name The name to find
     * @return The nation, if available
     */
    Optional<Nation> getNation(String name);

    /**
     * Checks whether the specified nation has been registered to the service.
     *
     * @param nation The nation to check
     * @return True if the nation has been registered
     */
    boolean contains(Nation nation);

    /**
     * Registers the specified nation to the service.
     *
     * @param nation The nation to register
     * @return True if the nation was successfully registered
     */
    boolean register(Nation nation);

    /**
     * Unregisters the specified nation from the service.
     *
     * @param nation The nation to unregister
     * @return True if the nation was successfully unregistered
     */
    boolean unregister(Nation nation);
}