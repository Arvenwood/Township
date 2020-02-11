package arvenwood.towns.api.event.claim;

import arvenwood.towns.api.claim.Claim;
import arvenwood.towns.api.town.Town;
import org.spongepowered.api.event.Event;

public interface TargetClaimEvent extends Event {

    Claim getClaim();

    default Town getTown() {
        return getClaim().getTown();
    }
}