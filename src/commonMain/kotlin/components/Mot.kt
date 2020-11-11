package components

import dico

data class Mot(var chaine: String = "",var sens: Char = 'h', var ligOuCol: Int = 0, var position: Int = 0, var longueur: Int = 0, var score: Int = 0){

    fun calcScore(){
        var valMot = 1
        chaine.forEachIndexed { index, it ->
            val valLet = letPlateau[ligOuCol][position + index]
            valMot *= motPlateau[ligOuCol][position + index]
            score += ptsABC[it.toInt() - 97] * valLet
            print("=L$valLet=M$valMot=S$score==")
        }
        score *= valMot
    }
    fun verif(){
        if (dico[longueur-2].contains(chaine)) calcScore() else score = 0

    }

    override fun toString(): String {
        return ("Mot.${if (sens == 'h') '-' else '|'}(${ligOuCol},${position})${score}pts")
    }
}
