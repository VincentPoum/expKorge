package internals

import com.soywiz.korma.geom.Point
import components.Mot
import components.Tirage

data class Tour(
        var sens: Char = 'h',
        var lettres: String = "",
        var positions: MutableSet<Pair<Pair<Int, Int>, Char>> = mutableSetOf(),
        var mots: MutableList<Mot> = mutableListOf()
)