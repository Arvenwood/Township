package pw.dotdash.township.api.permission;

import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public final class TownPermissions {

    /**
     * Allows the resident to deposit money into the town bank.
     */
    public static final TownPermission BANK_DEPOSIT = DummyObjectProvider.createFor(TownPermission.class, "BANK_DEPOSIT");

    /**
     * Allows the resident to withdraw money from the town bank.
     */
    public static final TownPermission BANK_WITHDRAW = DummyObjectProvider.createFor(TownPermission.class, "BANK_WITHDRAW");

    /**
     * Allows the resident to kick other residents from the town.
     */
    public static final TownPermission KICK_RESIDENTS = DummyObjectProvider.createFor(TownPermission.class, "KICK_RESIDENTS");

    /**
     * Allows the resident to create and delete claims for the town.
     */
    public static final TownPermission MANAGE_CLAIMS = DummyObjectProvider.createFor(TownPermission.class, "MANAGE_CLAIMS");

    /**
     * Allows the resident to change role permissions for the town's roles.
     */
    public static final TownPermission MANAGE_PERMISSIONS = DummyObjectProvider.createFor(TownPermission.class, "MANAGE_PERMISSIONS");

    /**
     * Allows the resident to talk in the town chat channel.
     */
    public static final TownPermission TOWN_CHAT = DummyObjectProvider.createFor(TownPermission.class, "TOWN_CHAT");

    private TownPermissions() {
        throw new AssertionError("Don't instantiate me!");
    }
}