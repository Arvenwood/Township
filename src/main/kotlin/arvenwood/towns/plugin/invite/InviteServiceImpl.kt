package arvenwood.towns.plugin.invite

import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.event.invite.AcceptInviteEventImpl
import arvenwood.towns.plugin.event.invite.CreateInviteEventImpl
import arvenwood.towns.plugin.event.invite.DeleteInviteEventImpl
import arvenwood.towns.plugin.util.tryPost
import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.Sponge
import java.util.*
import kotlin.collections.HashMap

class InviteServiceImpl : InviteService {

    private val senderReceiverTable: Table<UUID, UUID, Invite> =
        Tables.newCustomTable(HashMap()) { HashMap<UUID, Invite>() }

    override fun getInvitesBySender(sender: Resident): Collection<Invite> =
        this.senderReceiverTable.row(sender.uniqueId).values.toSet()

    override fun getInvitesByReceiver(receiver: Resident): Collection<Invite> =
        this.senderReceiverTable.column(receiver.uniqueId).values.toSet()

    override fun getInvitesByTown(town: Town): Collection<Invite> =
        this.senderReceiverTable.values().filter { it.town == town }

    override fun contains(invite: Invite): Boolean =
        this.senderReceiverTable.contains(invite.sender.uniqueId, invite.receiver.uniqueId)

    override fun register(invite: Invite): Boolean {
        if (this.senderReceiverTable.contains(invite.sender.uniqueId, invite.receiver.uniqueId)) {
            return false
        }

        Sponge.getEventManager().tryPost(CreateInviteEventImpl(invite, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.senderReceiverTable.put(invite.sender.uniqueId, invite.receiver.uniqueId, invite)
        return true
    }

    override fun unregister(invite: Invite): Boolean {
        if (!this.senderReceiverTable.contains(invite.sender.uniqueId, invite.receiver.uniqueId)) {
            return false
        }

        Sponge.getEventManager().tryPost(DeleteInviteEventImpl(invite, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.senderReceiverTable.remove(invite.sender.uniqueId, invite.receiver.uniqueId)
        return true
    }

    override fun accept(invite: Invite): Boolean {
        if (!this.senderReceiverTable.contains(invite.sender.uniqueId, invite.receiver.uniqueId)) {
            return false
        }

        if (invite.isAccepted) {
            return false
        }

        if (invite.receiver.town.isPresent) {
            return false
        }

        Sponge.getEventManager().tryPost(AcceptInviteEventImpl(invite, Sponge.getCauseStackManager().currentCause))
            ?: return false

        invite.isAccepted = true
        this.senderReceiverTable.remove(invite.sender.uniqueId, invite.receiver.uniqueId)

        return invite.receiver.setTown(invite.town)
    }
}