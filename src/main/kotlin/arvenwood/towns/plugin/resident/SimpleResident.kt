package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import java.util.*

class SimpleResident(
    private val uniqueId: UUID,
    private val name: String,
    private var town: Town?
) : Resident {

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getTown(): Optional<Town> =
        Optional.ofNullable(this.town)

    override fun setTown(town: Town?) {
        this.town?.removeResident(this)

        this.town = town
        town?.addResident(this)
    }
}