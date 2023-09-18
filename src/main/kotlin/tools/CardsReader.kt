package tools

import model.Card
import java.io.File
import java.io.InputStream

class CardsReader {
    companion object {
        private var cards: List<Card>? = null

        fun getCards(): List<Card> {
            if (cards == null) {
                cards = readCardsCSV()
            }
            return cards.orEmpty()
        }

        private fun readCardsCSV(): List<Card> {
            val dataStream: InputStream = File("cartas.csv").inputStream()
            return dataStream.bufferedReader().lineSequence()
                .filter { it.isNotBlank() }
                .map { parseCard(it) }
                .toList()
        }

        private fun parseCard(card: String): Card {
            val (name, description, attack, defense) = card.split(';')
            return Card(name, description, attack.toInt(), defense.toInt())
        }

        fun getCardByName(name: String): Card? {
            return getCards().find { it.name == name }
        }

        fun get5RandomCards(): List<Card> {
            return getCards().shuffled().take(5)
        }
    }
}
