package business

/**
 * main.kotlin.Solver for {@link business.LeCompteEstBon} game.
 *
 * @author Guillaume Chanson
 * @version 1.0
 */
class Solver(val toGuess: Int, val numbers: IntArray) {
    // L'ensemble des partitions de plaquettes
    private var nbCounter = 0
    private val nbNumbers = numbers.size
    private val allNbrPossibilities = Array(factoriel(nbNumbers)) { IntArray(nbNumbers) }

    // L'alphabet des opérateurs
    private var opCounter = 0
    private val alphabet = charArrayOf('+', '-', '*', '/')
    private val tempAllOpPossibilities = Array(Math.pow(alphabet.size.toDouble(), nbNumbers - 1.0).toInt()) { CharArray(nbNumbers - 1) }

    // Best solution
    private var closest = 0
    private var closestPath = ""

    /**
     * A first naive algorithm to "brute-force" find a solution (or closest one).
     */
    fun naiveAlgorithm() {
        println("\n*** Solving ... ***")

        // On trouve toutes les permutations
        permute(numbers, 0)

        // On calcule toutes les combinaison de signe
        possibleStrings(nbNumbers - 1, alphabet, "")
        // TODO : enlever les doublons (priorités, parenthèses ...)

        // On rajoute un "+" devant toutes les combinaisons calculées (afin de calculer facilement toutes les possibilités plus tard)
        val allOpPossibilities = Array(Math.pow(alphabet.size.toDouble(), (nbNumbers - 1).toDouble()).toInt()) { CharArray(nbNumbers) }
        for (i in allOpPossibilities.indices) {
            for (j in 0..allOpPossibilities[0].size - 1) {
                if (j == 0)
                    allOpPossibilities[i][0] = '+'
                else
                    allOpPossibilities[i][j] = tempAllOpPossibilities[i][j - 1]
            }
        }

        var tries = 0
        var rejected = 0

        // Calcule toutes les possibilités de jeu
        mainloop@ for (op in allOpPossibilities) {
            triesloop@ for (i in allNbrPossibilities.indices) {

                var tmpResult = 0
                var tmpPath = ""

                for (j in 0..nbNumbers - 1) {
                    tries++

                    val result = calcule(op[j], tmpResult, allNbrPossibilities[i][j])
                    if (result - Math.round(result) != 0.0F) {
                        rejected++
                        break@triesloop
                    }

                    tmpResult = Math.round(result)
                    tmpPath = tmpPath + op[j] + Integer.toString(allNbrPossibilities[i][j])

                    if (Math.abs(tmpResult - toGuess) < Math.abs(closest - toGuess)) {
                        closest = tmpResult
                        closestPath = tmpPath
                        if (closest == toGuess) {
                            break@mainloop
                        }
                    }
                }
            }
        }

        if (closest == toGuess) {
            println("Le compte est bon ! $closestPath")
        } else {
            println("Le plus proche : $closest avec $closestPath")
        }

        println("\nOverall, $tries tries have been attempted, of which $rejected have been rejected")
    }

    /**
     * Calcule le factoriel du nombre entier positif donné.
     *
     * @param n paramètre de la fonction factoriel
     * @return le factoriel de {@code n}
     */
    private fun factoriel(n: Int): Int = if (n <= 0) 1 else (1..n).reduce({ a, b -> a * b })

    /**
     * Calcule toutes les combinaisons possibles et les ajoute dans {@code allNbrPossibilities}.
     *
     * @param input      Le tableau de nombre dont on cherche les combinaisons
     * @param startindex Un index de départ (mettre 0 par défaut)
     */
    private fun permute(input: IntArray, startindex: Int) {
        // End of recursion
        if (input.size == startindex + 1) {
            allNbrPossibilities[nbCounter] = input.copyOf()
            nbCounter++
            return
        }

        for (i in startindex..input.size - 1) {
            val tmp = input.copyOf()
            tmp[i] = input[startindex]
            tmp[startindex] = input[i]

            permute(tmp, startindex + 1)
        }
    }

    /**
     * Prend le caractère d'un opérateur et deux nombres, puis calcule le resultat.
     *
     * @param op   L'opérateur
     * @param nbr1 Nombre 1
     * @param nbr2 Nombre 2
     * @return Le résultat de l'opération
     */
    private fun calcule(op: Char, nbr1: Int, nbr2: Int): Float {
        val n1 = nbr1.toFloat()
        val n2 = nbr2.toFloat()

        return when (op) {
            '+' -> n1 + n2
            '-' -> n1 - n2
            '*' -> n1 * n2
            '/' -> n1 / n2
            else -> throw IllegalArgumentException(op + " is not recognized")
        }
    }

    /**
     * Prend un alphabet, la longeur maximum des combinaisons, une chaine de caractère vide et modifie tempAllOpPossibilities
     * pour qu'il devienne un tableau de toutes les combinaisons de l'alphabet.
     * {a,b} , 3, "" => [aaa, aab, abb, bbb, bba, baa].
     *
     * @param maxLength La longueur maximales des combinaisons
     * @param alphabet  Un alphabet de symbole
     * @param curr      Une chaine vide (utile pour la recursion)
     */
    private fun possibleStrings(maxLength: Int, alphabet: CharArray, curr: String) {
        // End of recursion
        if (curr.length == maxLength) {
            tempAllOpPossibilities[opCounter] = curr.toCharArray()
            opCounter++
            return
        }

        // Add each letter from the alphabet and process again
        for (a in alphabet) {
            possibleStrings(maxLength, alphabet, curr + a)
        }
    }
}