package business

import java.util.*

/**
 * Holds game data and meta-data.
 *
 * @author Guillaume Chanson
 * @version 1.0
 */
data class LeCompteEstBon(val toGuess: Int, val numbers: Array<Int>) {

    /**
     * Companion object to provide an useful factory for creating a game with values.
     * Values are generated randomly but according to game specification :
     *  - discrete universe
     *  - weighted universe
     */
    companion object Factory {
        val universe: Array<Int> = arrayOf(100, 75, 50, 25, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val weights = Array(universe.size, { i -> if (universe[i] > 9) 1.0 else (2.0 + (5.0 / 9.0)) }).map { i -> i / 28.0 }
        val nbNumbers = 6

        /**
         * Creates a returns the new game.
         */
        fun create(): LeCompteEstBon {
            val randomUniverse = weights
                    .map { i -> Math.round(i * 28.0).toInt() }
                    .withIndex()
                    .fold(emptyArray(), { acc: Array<Int>, (index, value): IndexedValue<Int> ->
                        acc.plus(Array(value) { universe[index] })
                    })

            return LeCompteEstBon(
                    Random().nextInt(900) + 100,
                    Array(nbNumbers, { randomUniverse[Random().nextInt(randomUniverse.size)] })
            )
        }
    }

    /**
     * Prints the game statement.
     * Have fun !
     */
    fun printStatement() {
        println("*** Game initialized ***")
        println("[$toGuess] ${numbers.joinToString(" ")}")
    }
}