package arvenwood.towns.api.event.town;

import arvenwood.towns.api.town.Town;
import org.spongepowered.api.event.Event;

public interface TargetTownEvent extends Event {

    Town getTown();
}