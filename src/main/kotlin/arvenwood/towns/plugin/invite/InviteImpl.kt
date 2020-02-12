package arvenwood.towns.plugin.invite

import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.event.invite.AcceptInviteEventImpl
import arvenwood.towns.plugin.util.tryPost
import org.spongepowered.api.Sponge
import java.time.Instant

data class InviteImpl(
    private val sender: Resident,
    private val receiver: Resident,
    private val town: Town,
    private val creationTime: Instant
) : Invite {

    private var accepted: Boolean = false

    override fun getSender(): Resident = this.sender

    override fun getReceiver(): Resident = this.receiver

    override fun getTown(): Town = this.town

    override fun getCreationTime(): Instant = this.creationTime

    override fun isAccepted(): Boolean = this.accepted

    override fun setAccepted(accepted: Boolean) {
        this.accepted = accepted
    }

    class Builder : Invite.Builder {

        private var sender: Resident? = null
        private var receiver: Resident? = null
        private var town: Town? = null
        private var creationTime: Instant? = null

        override fun sender(sender: Resident): Invite.Builder {
            this.sender = sender
            return this
        }

        override fun receiver(receiver: Resident): Invite.Builder {
            this.receiver = receiver
            return this
        }

        override fun town(town: Town): Invite.Builder {
            this.town = town
            return this
        }

        override fun creationTime(creationTime: Instant): Invite.Builder {
            this.creationTime = creationTime
            return this
        }

        override fun from(value: Invite): Invite.Builder {
            this.sender = value.sender
            this.receiver = value.receiver
            this.town = value.town
            this.creationTime = value.creationTime
            return this
        }

        override fun reset(): Invite.Builder {
            this.sender = null
            this.receiver = null
            this.town = null
            this.creationTime = null
            return this
        }

        override fun build(): Invite = InviteImpl(
            sender = checkNotNull(this.sender),
            receiver = checkNotNull(this.receiver),
            town = checkNotNull(this.town),
            creationTime = this.creationTime ?: Instant.now()
        )
    }
}