package arvenwood.towns.api.event.resident;

import arvenwood.towns.api.resident.Resident;
import org.spongepowered.api.event.Event;

public interface TargetResidentEvent extends Event {

    Resident getResident();
}