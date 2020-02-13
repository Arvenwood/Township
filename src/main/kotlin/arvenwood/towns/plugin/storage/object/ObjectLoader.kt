package arvenwood.towns.plugin.storage.`object`

interface ObjectLoader<in Input, out Output> {

    fun load(): Output

    fun save(input: Input)
}