package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.storage.DataQueries
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.economy.EconomyService
import org.spongepowered.api.service.economy.account.Account
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import java.util.*

data class ResidentImpl(
    private val uniqueId: UUID,
    private val name: String,
    private var townUniqueId: UUID?
) : Resident {
    constructor(player: Player) : this(player.uniqueId, player.name, null)

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getTownUniqueId(): Optional<UUID> =
        Optional.ofNullable(this.townUniqueId)

    override fun setTown(town: Town?): Boolean {
        getTown().ifPresent {
            it.removeResident(this)
        }

        this.townUniqueId = town?.uniqueId
        return town?.addResident(this) == true
    }

    override fun getAccount(): Optional<out Account> =
        Sponge.getServiceManager().provide(EconomyService::class.java)
            .flatMap { it.getOrCreateAccount(this.uniqueId) }

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

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer {
        val container: DataContainer = DataContainer.createNew()
            .set(DataQueries.UNIQUE_ID, this.uniqueId.toString())
            .set(DataQueries.NAME, this.name)

        this.townUniqueId?.let { container.set(DataQueries.TOWN_UNIQUE_ID, it.toString()) }

        return container
    }
}