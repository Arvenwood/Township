package pw.dotdash.township.api.event.resident;

import pw.dotdash.township.api.resident.Resident;
import org.spongepowered.api.event.Event;

public interface TargetResidentEvent extends Event {

    Resident getResident();
}