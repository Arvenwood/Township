package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import java.util.*

class ResidentImpl(
    private val uniqueId: UUID,
    private val name: String,
    private var town: Town?
) : Resident {

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getTown(): Optional<Town> =
        Optional.ofNullable(this.town)

    override fun setTown(town: Town?): Boolean {
        this.town?.removeResident(this)

        this.town = town
        return town?.addResident(this) == true
    }

    override fun sendMessage(message: Text) {
        this.player.ifPresent {
            it.sendMessage(message)
        }
    }

    override fun setMessageChannel(channel: MessageChannel) {
        this.player.ifPresent {
            it.messageChannel = channel
        }
    }

    override fun getMessageChannel(): MessageChannel =
        this.player.map(Player::getMessageChannel).orElse(MessageChannel.TO_NONE)
}