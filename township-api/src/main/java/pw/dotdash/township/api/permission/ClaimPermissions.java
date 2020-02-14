package pw.dotdash.township.api.permission;

import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public final class ClaimPermissions {

    /**
     * Allow players to break blocks in the claim.
     */
    public static final ClaimPermission BREAK = DummyObjectProvider.createFor(ClaimPermission.class, "BREAK");

    /**
     * Allows players to interact with containers in the claim.
     */
    public static final ClaimPermission CONTAINER_USE = DummyObjectProvider.createFor(ClaimPermission.class, "CONTAINER_USE");

    /**
     * Allows entities to spawn in the claim.
     */
    public static final ClaimPermission ENTITY_SPAWNING = DummyObjectProvider.createFor(ClaimPermission.class, "ENTITY_SPAWNING");

    /**
     * Allows explosions in the claim.
     */
    public static final ClaimPermission EXPLOSIONS = DummyObjectProvider.createFor(ClaimPermission.class, "EXPLOSIONS");

    /**
     * Allows players to use items in the claim.
     */
    public static final ClaimPermission ITEM_USE = DummyObjectProvider.createFor(ClaimPermission.class, "ITEM_USE");

    /**
     * Allows players to modify blocks in the claim.
     */
    public static final ClaimPermission MODIFY = DummyObjectProvider.createFor(ClaimPermission.class, "MODIFY");

    /**
     * Allows players to place blocks in the claim.
     */
    public static final ClaimPermission PLACE = DummyObjectProvider.createFor(ClaimPermission.class, "PLACE");

    /**
     * Allows players to attack other players in the claim.
     */
    public static final ClaimPermission PVP = DummyObjectProvider.createFor(ClaimPermission.class, "PVP");

    /**
     * Allows players to attack non-player entities in the claim.
     */
    public static final ClaimPermission PVE = DummyObjectProvider.createFor(ClaimPermission.class, "PVE");

    /**
     * Allows players to open doors, click buttons, flip switches, etc., in the claim.
     */
    public static final ClaimPermission SWITCH = DummyObjectProvider.createFor(ClaimPermission.class, "SWITCH");

    private ClaimPermissions() {
        throw new AssertionError("Don't instantiate me!");
    }
}