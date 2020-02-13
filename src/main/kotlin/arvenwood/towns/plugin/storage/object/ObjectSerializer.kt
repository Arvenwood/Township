package arvenwood.towns.plugin.storage.`object`

interface ObjectSerializer<T, in Input, out Output> {

    fun deserialize(input: Input): T?

    fun serialize(value: T): Output
}