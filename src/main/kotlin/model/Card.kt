package model

class Card(
    var name: String,
    private var description: String,
    private var attack: Int,
    private var defense: Int
) {
    override fun toString(): String {
        return "Carta(name='$name', description='$description', n1=$attack, n2=$defense)"
    }
}