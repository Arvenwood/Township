package arvenwood.towns.api.event.town;

import arvenwood.towns.api.event.resident.TargetResidentEvent;
import arvenwood.towns.api.resident.Resident;
import org.spongepowered.api.event.Cancellable;

public interface ChangeTownEvent extends TargetTownEvent {

    interface Name extends ChangeTownEvent, Cancellable {

        String getOldName();

        String getNewName();

        void setNewName(String name);
    }

    interface Owner extends ChangeTownEvent, Cancellable {

        Resident getOldOwner();

        Resident getNewOwner();

        void setNewOwner(Resident resident);
    }

    interface Join extends ChangeTownEvent, TargetResidentEvent, Cancellable {

    }

    interface Leave extends ChangeTownEvent, TargetResidentEvent, Cancellable {

    }
}