package pw.dotdash.township.plugin.util

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers

fun String.ampersand(): Text =
    TextSerializers.FORMATTING_CODE.deserialize(this)