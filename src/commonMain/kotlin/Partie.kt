package fr.lapoumerole

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTime.Companion.now
import components.Mot
import internals.Tour

data class Partie(var joueurs: List<Joueur>, var tours: List<Tour>, var debut: DateTime = now()) {
    data class Joueur(var index: Int = 0, var nom: String = "", var email: String = "")

}