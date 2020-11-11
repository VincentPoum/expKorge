package components

import com.soywiz.korim.color.Colors

val let1 = arrayOf(1,1,1,2,1,1,1,1,1,1,1,2,1,1,1)
val let2 = arrayOf(1,1,1,1,1,3,1,1,1,3,1,1,1,1,1)
val let3 = arrayOf(1,1,1,1,1,1,2,1,2,1,1,1,1,1,1)
val let4 = arrayOf(2,1,1,1,1,1,1,2,1,1,1,1,1,1,2)
val let5 = arrayOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
val let6 = arrayOf(1,3,1,1,1,3,1,1,1,3,1,1,1,3,1)
val let7 = arrayOf(1,1,2,1,1,1,2,1,2,1,1,1,2,1,1)
val let8 = arrayOf(1,1,1,2,1,1,1,1,1,1,1,2,1,1,1)

val mot1 = arrayOf(3,1,1,1,1,1,1,3,1,1,1,1,1,1,3)
val mot2 = arrayOf(1,2,1,1,1,1,1,1,1,1,1,1,1,2,1)
val mot3 = arrayOf(1,1,2,1,1,1,1,1,1,1,1,1,2,1,1)
val mot4 = arrayOf(1,1,1,2,1,1,1,1,1,1,1,2,1,1,1)
val mot5 = arrayOf(1,1,1,1,2,1,1,1,1,1,2,1,1,1,1)
val mot6 = arrayOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
val mot7 = arrayOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
val mot8 = arrayOf(3,1,1,1,1,1,1,2,1,1,1,1,1,1,3)

val letPlateau = arrayOf(let1, let2, let3, let4, let5, let6, let7, let8, let7, let6, let5, let4, let3, let2, let1)
val motPlateau = arrayOf(mot1, mot2, mot3, mot4, mot5, mot6, mot7, mot8, mot7, mot6, mot5, mot4, mot3, mot2, mot1)
val colCS = Colors["#007427"]
val colLD = Colors["#33c0ff"]
val colLT = Colors["#0937ff"]
val colMD = Colors["#ff889a"]
val colMT = Colors["#ff0000"]
val colPlateau = mapOf(11 to colCS, 12 to colLD, 13 to colLT, 21 to colMD, 31 to colMT)

const val uneLigne: String = "               "
val lignes = mutableListOf<Ligne>()
val colonnes = mutableListOf<Ligne>()
var tirage = ""