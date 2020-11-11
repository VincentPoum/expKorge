package internals

import components.Mot
import components.Tirage

data class Tour(
        var sens: Char = 'h',
        var lettres: String = "",
        var positions: MutableSet<Pair<Int, Int>> = mutableSetOf(),
        var mots: MutableList<Mot> = mutableListOf()
)