package arvenwood.towns.plugin.town

import arvenwood.towns.api.town.Town
import org.spongepowered.api.text.channel.MessageChannel
import org.spongepowered.api.text.channel.MessageReceiver

data class TownMessageChannel(private val town: Town) : MessageChannel {

    override fun getMembers(): Collection<MessageReceiver> = town.residents
}