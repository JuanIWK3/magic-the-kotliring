package model

import tools.Colors

class Card(
    var name: String,
    private var description: String,
    var attack: Int,
    var defense: Int,
    var type: String
) {
    var position: Int = 0

    override fun toString(): String {
        return "$name, ${Colors.RED}Atk=$attack${Colors.RESET}, ${Colors.GREEN}Def=$defense${Colors.RESET}, '$description', ${if (type == "monstro") Colors.RED else Colors.GREEN}$type${Colors.RESET}"
    }

    fun equip(card: Card) {
        if (card.type == "equipamento") {
            this.attack += card.attack
            this.defense += card.defense
        }
    }
}