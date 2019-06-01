package name.shokred.demo.kafka.dto

class Pair<F : Any, S : Any> constructor(){
    lateinit var first: F
    lateinit var second: S

    constructor(f: F, s: S) : this() {
        first = f
        second = s
    }

    override fun equals(other: Any?): Boolean {
        if (other is Pair<*, *>) {
            return other.first == this.first && other.second == this.second
        }

        return false
    }

    override fun hashCode(): Int {
        var result = first.hashCode()
        result = 31 * result + second.hashCode()
        return result
    }

    override fun toString(): String {
        return "Pair(first=$first, second=$second)"
    }
}
