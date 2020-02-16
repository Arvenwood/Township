package pw.dotdash.township.plugin.command.special

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors.*
import org.spongepowered.api.text.format.TextStyles.BOLD
import pw.dotdash.director.core.HNil
import pw.dotdash.director.core.component1
import pw.dotdash.director.core.exception.CommandException
import pw.dotdash.director.core.parameter.uuid
import pw.dotdash.director.core.tree.RootCommandTree
import pw.dotdash.director.sponge.SpongeCommandTree
import pw.dotdash.director.sponge.setPermission
import pw.dotdash.township.plugin.util.unwrap
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object CommandPlayTheSpacesGame {

    val TREE: RootCommandTree<CommandSource, HNil, CommandResult> =
        SpongeCommandTree.root("playthecharactergame")
            .setPermission("township.games.character")
            .addChild("found") {
                setArgument(uuid() key "key") {
                    setExecutor { src, args ->
                        val (key: UUID) = args

                        val game: Game = games.getIfPresent(key)
                            ?: throw CommandException("Sorry! That game already expired.", showUsage = false)
                        games.invalidate(key)

                        for (uuid: UUID in game.players) {
                            val player: Player = Sponge.getServer().getPlayer(uuid).unwrap() ?: continue

                            player.sendMessage(Text.of(YELLOW, "${src.name} found the hidden character in ", game.elapsed(Instant.now()), "!"))
                        }

                        CommandResult.success()
                    }
                }
            }
            .setExecutor { src, args ->
                if (src is Player) {
                    startGame(listOf(src))
                } else {
                    startGame(Sponge.getServer().onlinePlayers)
                }

                CommandResult.success()
            }
            .build()

    private val games: Cache<UUID, Game> =
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build<UUID, Game>()

    private fun startGame(players: Collection<Player>) {
        val game = Game(UUID.randomUUID(), Instant.now(), players.map { it.uniqueId })
        val board: Text = createBoard(game.key, 10, 10)

        this.games.put(game.key, game)

        for (player: Player in players) {
            player.sendMessage(Text.of(RED, "ATTENTION! ", AQUA, "Be the first to find and click the hidden character!"))
            player.sendMessage(board)
            player.sendMessage(Text.of(AQUA, "It's somewhere above here! You have 1 minute, by the way!"))
        }
    }

    private fun createBoard(key: UUID, numRows: Int, numColumns: Int): Text {
        val random = Random(System.currentTimeMillis())
        val char: String = random.nextInt(33, 127).toChar().toString()
        val targetRow: Int = random.nextInt(numRows)
        val targetColumn: Int = random.nextInt(numColumns)

        val emptyRow: Text = Text.of(char.repeat(numColumns))
        val leftColumn: Text = Text.of(char.repeat(targetColumn - 1))
        val rightColumn: Text = Text.of(char.repeat(numColumns - targetColumn))
        val keySpace: Text = Text.builder(char).onClick(TextActions.runCommand("/playthecharactergame found $key")).build()

        val builder: Text.Builder = Text.builder()
        for (i in 0 until targetRow) {
            builder.append(emptyRow, Text.NEW_LINE)
        }
        builder.append(leftColumn, keySpace, rightColumn)
        for (i in targetRow + 1 until numRows) {
            builder.append(Text.NEW_LINE, emptyRow)
        }
        return builder.build()
    }

    private data class Game(val key: UUID, val startedAt: Instant, val players: List<UUID>) {

        fun elapsed(now: Instant): Text = Text.of(BOLD, Duration.between(this.startedAt, now).seconds, " seconds")
    }
}