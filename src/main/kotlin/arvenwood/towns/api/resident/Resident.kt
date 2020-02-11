package arvenwood.towns.api.resident

import arvenwood.towns.api.town.Town
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import java.util.*

interface Resident {

    val uniqueId: UUID

    val name: String

    var town: Town?

    val isOwner: Boolean
        get() = this.town?.owner == this

    val player: Player?
        get() = Sponge.getServer().getPlayer(this.uniqueId).orElse(null)
}