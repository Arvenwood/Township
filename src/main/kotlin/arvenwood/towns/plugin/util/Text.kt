package arvenwood.towns.plugin.util

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers

fun String.text(): Text =
    TextSerializers.FORMATTING_CODE.deserialize(this)