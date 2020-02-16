package pw.dotdash.township.api.permission;

import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.util.Tristate;
import pw.dotdash.township.api.claim.Claim;
import pw.dotdash.township.api.permission.Permission;

import java.util.Collection;

/**
 * An object which can hold permission data.
 */
public interface Role extends Identifiable, DataSerializable {

    /**
     * Gets the name of this role.
     *
     * @return The name of this role
     */
    String getName();

    /**
     * Sets the name of this role.
     *
     * @param name The name to set
     * @return Whether the action was successful
     */
    boolean setName(String name);

    /**
     * Gets the priority of this role.
     *
     * @return The priority of this role
     */
    int getPriority();

    /**
     * Sets the priority of this role.
     *
     * @param priority The priority to set
     * @return Whether the action was successful
     */
    boolean setPriority(int priority);

    /**
     * Gets all of this role's permissions.
     *
     * @return All of this role's permissions
     */
    Collection<Permission> getPermissions();

    /**
     * Returns true if this role has the specified permission.
     *
     * @param permission The permission to check
     * @return Whether this role has the permission
     */
    boolean hasPermission(Permission permission);

    /**
     * Adds the specified permission to this role.
     *
     * @param permission The permission to add
     * @return Whether the action was successful
     */
    boolean addPermission(Permission permission);

    /**
     * Removes the specified permission from this role.
     *
     * @param permission The permission to remove
     * @return Whether the action was successful
     */
    boolean removePermission(Permission permission);

    /**
     * Returns the calculated value set for a given permission.
     *
     * @param permission The permission to check
     * @param claim      The claim context, for overrides
     * @return The tristate result of the check
     */
    Tristate getPermissionValue(Permission permission, Claim claim);

    /**
     * Test whether the role is permitted to perform an action corresponding
     * to the given permission catalog type.
     *
     * <p>This must return the same boolean equivalent as
     * {@link #getPermissionValue(Permission, Claim)}</p>
     *
     * @param permission The permission catalog type
     * @param claim      The claim context, for overrides
     * @return True if permission is granted
     */
    default boolean hasPermission(Permission permission, Claim claim) {
        return getPermissionValue(permission, claim).asBoolean();
    }

    interface Builder<T extends Role, B extends Builder<T, B>> extends ResettableBuilder<T, B> {

        /**
         * Sets the name of the role.
         *
         * @param name The name to set
         * @return This builder, for chaining
         */
        B name(String name);

        /**
         * Sets the priority of the role.
         *
         * @param priority The priority to set
         * @return This builder, for chaining
         */
        B priority(int priority);

        B permissions(Iterable<Permission> permissions);

        B permissions(Permission... permissions);

        T build();
    }
}