package pw.dotdash.township.api.event.town;

import pw.dotdash.township.api.town.Town;
import org.spongepowered.api.event.Event;

public interface TargetTownEvent extends Event {

    Town getTown();
}