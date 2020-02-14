package pw.dotdash.township.api.event.claim;

import pw.dotdash.township.api.claim.Claim;
import pw.dotdash.township.api.town.Town;
import org.spongepowered.api.event.Event;

public interface TargetClaimEvent extends Event {

    Claim getClaim();

    default Town getTown() {
        return getClaim().getTown();
    }
}