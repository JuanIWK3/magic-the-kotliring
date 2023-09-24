package model

class Card(
    var name: String,
    private var description: String,
    var attack: Int,
    var defense: Int,
    var type: String
) {
    var position: Int = 0

    override fun toString(): String {
        return "$name, Atk=$attack, Def=$defense, desc='$description', type='$type'"
    }

    fun equip(card: Card) {
        if (card.type == "equipamento") {
            this.attack += card.attack
            this.defense += card.defense
        }
    }
}