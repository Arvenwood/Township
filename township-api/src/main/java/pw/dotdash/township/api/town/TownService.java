package pw.dotdash.township.api.town;

import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a service for managing towns.
 */
public interface TownService {

    /**
     * The singleton service instance.
     *
     * @return The service instance
     * @throws org.spongepowered.api.service.ProvisioningException If the service isn't registered
     */
    static TownService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(TownService.class);
    }

    /**
     * Gets all registered towns.
     *
     * @return All registered towns
     */
    Collection<Town> getTowns();

    /**
     * Gets the registered town with the specified unique id.
     *
     * @param uniqueId The UUID to find
     * @return The town, if available
     */
    Optional<Town> getTown(UUID uniqueId);

    /**
     * Gets the registered town with the specified name.
     *
     * @param name The name to find
     * @return The town, if available
     */
    Optional<Town> getTown(String name);

    /**
     * Checks whether the specified town has been registered to the service.
     *
     * @param town The town to check
     * @return True if the town has been registered
     */
    boolean contains(Town town);

    /**
     * Registers the specified town to the service.
     *
     * @param town The town to register
     * @return True if the town was successfully registered
     */
    boolean register(Town town);

    /**
     * Unregisters the specified town from the service.
     *
     * @param town The town to unregister
     * @return True if the town was successfully unregistered
     */
    boolean unregister(Town town);
}