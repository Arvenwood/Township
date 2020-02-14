package pw.dotdash.township.api.event.town;

import pw.dotdash.township.api.event.resident.TargetResidentEvent;
import pw.dotdash.township.api.resident.Resident;
import org.spongepowered.api.event.Cancellable;

public interface ChangeTownEvent extends TargetTownEvent {

    interface Name extends ChangeTownEvent, Cancellable {

        String getOldName();

        String getNewName();

        void setNewName(String name);
    }

    interface Open extends ChangeTownEvent, Cancellable {

        /**
         * Gets whether the town is now open to join.
         *
         * @return Whether the town is now open to join
         */
        boolean isOpen();

        /**
         * Sets whether the town is now open to join.
         *
         * @param open Whether players can join without invites
         */
        void setOpen(boolean open);
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