package arvenwood.towns.plugin.event.town

import arvenwood.towns.api.event.town.ChangeTownEvent
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import org.spongepowered.api.event.Cancellable
import org.spongepowered.api.event.cause.Cause

abstract class ChangeTownEventImpl(
    private val town: Town,
    private val cause: Cause
) : ChangeTownEvent, Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = this.cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getTown(): Town = this.town

    override fun getCause(): Cause = this.cause

    class Name(
        private val oldName: String,
        private var newName: String,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Name {

        override fun getOldName(): String = this.oldName

        override fun getNewName(): String = this.newName

        override fun setNewName(name: String) {
            this.newName = name
        }
    }

    class Owner(
        private val oldOwner: Resident,
        private var newOwner: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Owner {

        override fun getOldOwner(): Resident = this.oldOwner

        override fun getNewOwner(): Resident = this.newOwner

        override fun setNewOwner(resident: Resident) {
            this.newOwner = resident
        }
    }

    class Join(
        private val resident: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Join {

        override fun getResident(): Resident = this.resident
    }

    class Leave(
        private val resident: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Leave {

        override fun getResident(): Resident = this.resident
    }
}