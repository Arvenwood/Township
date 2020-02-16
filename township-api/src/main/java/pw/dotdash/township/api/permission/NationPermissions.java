package pw.dotdash.township.api.permission;

import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public final class NationPermissions {

    public static final NationPermission NATION_BANK_DEPOSIT = DummyObjectProvider.createFor(NationPermission.class, "NATION_BANK_DEPOSIT");

    public static final NationPermission NATION_BANK_WITHDRAW = DummyObjectProvider.createFor(NationPermission.class, "NATION_BANK_WITHDRAW");

    public static final NationPermission NATION_CHAT = DummyObjectProvider.createFor(NationPermission.class, "NATION_CHAT");

    public static final NationPermission NATION_DEPUTY = DummyObjectProvider.createFor(NationPermission.class, "NATION_DEPUTY");

    public static final NationPermission KICK_TOWNS = DummyObjectProvider.createFor(NationPermission.class, "KICK_TOWNS");

    public static final NationPermission MANAGE_NATION_PERMISSIONS = DummyObjectProvider.createFor(NationPermission.class, "MANAGE_NATION_PERMISSIONS");

    private NationPermissions() {
        throw new AssertionError("Don't instantiate me!");
    }
}