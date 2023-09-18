package model

import tools.CardsReader

class Player(private var name: String) {
    private var cards: List<Card> = mutableListOf()

    init {
        this.cards = CardsReader.get5RandomCards()
    }

    fun getCards(): List<Card> {
        return this.cards
    }
}