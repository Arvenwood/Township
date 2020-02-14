package pw.dotdash.township.plugin.config

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

@ConfigSerializable
class EconomyConfig {

    @Setting
    var enabled: Boolean = false
}