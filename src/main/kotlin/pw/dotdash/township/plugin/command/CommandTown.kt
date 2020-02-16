package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import pw.dotdash.director.core.HNil
import pw.dotdash.director.core.parameter.enum
import pw.dotdash.director.core.parameter.optional
import pw.dotdash.director.core.parameter.optionalOrElse
import pw.dotdash.director.core.parameter.string
import pw.dotdash.director.core.tree.RootCommandTree
import pw.dotdash.director.sponge.SpongeCommandTree
import pw.dotdash.director.sponge.parameter.permission
import pw.dotdash.director.sponge.parameter.player
import pw.dotdash.director.sponge.setPermission
import pw.dotdash.township.plugin.command.parameter.resident
import pw.dotdash.township.plugin.command.parameter.town

object CommandTown {

    val TREE: RootCommandTree<CommandSource, HNil, CommandResult> =
        SpongeCommandTree.root("town", "t")
            .setPermission("township.town.base")
            .addChild("claim", "c") {
                setPermission("township.town.claim.base")
                setExecutor(CommandTownClaim::claim)
            }
            .addChild("create") {
                setPermission("township.town.create.base")
                setArgument(string() key "name") {
                    setExecutor(CommandTownCreate::create)
                }
            }
            .addChild("delete") {
                setPermission("township.town.delete.base")
                setArgument(town().permission("township.town.delete.other").optional() key "town") {
                    setExecutor(CommandTownDelete::delete)
                }
            }
            .addChild("here") {
                setPermission("township.town.here.base")
                setArgument(player().permission("township.town.here.other").optional() key "player") {
                    setExecutor(CommandTownHere::here)
                }
            }
            .addChild("info", "i") {
                setPermission("township.town.info.base")
                setArgument(town().optional() key "town") {
                    setExecutor(CommandTownInfo::info)
                }
            }
            .addChild("invite") {
                setPermission("township.town.invite.base")
                setArgument(resident() key "receiver") {
                    setArgument(town().permission("township.town.invite.other").optional() key "town") {
                        setExecutor(CommandTownInvite::invite)
                    }
                }
            }
            .addChild("join") {
                setPermission("township.town.join.base")
                setArgument(town() key "town") {
                    setArgument(player().permission("township.town.join.other").optional() key "player") {
                        setExecutor(CommandTownJoin::join)
                    }
                }
            }
            .addChild("leave") {
                setPermission("township.town.leave.base")
                setArgument(player().permission("township.town.leave.other").optional() key "player") {
                    setExecutor(CommandTownLeave::leave)
                }
            }
            .addChild("list", "l") {
                setPermission("township.town.list.base")
                setArgument(enum<CommandTownList.SortBy>().optionalOrElse { CommandTownList.SortBy.NAME } key "sortBy") {
                    setExecutor(CommandTownList::list)
                }
            }
            .addChild("residents", "res") {
                setPermission("township.town.residents.base")
                setArgument(town().optional() key "town") {
                    setArgument(enum<CommandTownResidents.Filter>().optionalOrElse { CommandTownResidents.Filter.NONE } key "filter") {
                        setExecutor(CommandTownResidents::residents)
                    }
                }
            }
            .addChild("unclaim") {
                setPermission("township.town.unclaim.base")
                setExecutor(CommandTownUnclaim::unclaim)
            }
            .build()
}