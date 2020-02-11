package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import java.util.*

class SimpleResident(
    override val uniqueId: UUID,
    override val name: String,
    override var town: Town?
) : Resident