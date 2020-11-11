package components

import cellSize
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.vector.roundRect
import fieldSize
import internals.*
import leftIndent
import topIndent

class Board(private val bus: EventBus) : Container() {

    lateinit var regBg: RoundRect
    lateinit var fieldBg: RoundRect
    lateinit var fieldTxt: Text
    lateinit var regTxt: Text

    suspend fun init() {
        roundRect(fieldSize, fieldSize, 5.0, color = Colors["#b9aea0"]) {
            position(leftIndent, topIndent)
        }
        for (i in 0..14) {
            for (j in 0..14) {
                val col = colPlateau.getValue(motPlateau[i][j] * 10 + letPlateau[i][j])
                var cellField = roundRect(cellSize, cellSize, 5.0, color = col) {
                    position(leftIndent + 2 + (2 + cellSize) * i, topIndent + 2 + (2 + cellSize) * j)
                }
            }
        }
        fieldBg = roundRect(5 * cellSize,5 * cellSize,5.0, color = Colors["#b9aea0"])
        fieldTxt = text("") {
            textSize = 10.0
            filtering = false
        }.alignTopToTopOf(fieldBg)
        regBg = roundRect(5 * cellSize,5 * cellSize,5.0, color = Colors["#b9aea0"]){
            alignLeftToRightOf(fieldBg, cellSize)
            centerYOn(fieldBg)
        }
        regTxt = text("") {
            textSize = 10.0
            filtering = false
        }.alignTopToTopOf(regBg).alignLeftToLeftOf(regBg)
        bus.register<MajPlateau> { majPlateau(it) }
    }

    fun laCase(lePoint: Point): Pair<Int, Int> {
        val col = ((lePoint.x - leftIndent - 2) / (cellSize + 2)).toInt()
        val lig = ((lePoint.y - topIndent - 2) / (cellSize + 2)).toInt()
        return Pair(lig, col)
    }

    fun onBoard(lePoint: Point): WherePion {
        val laCase = laCase(lePoint)
        val posx = leftIndent + 2 + (2 + cellSize) * laCase.second
        val posy = topIndent + 2 + (2 + cellSize) * laCase.first
        val whereOk = (laCase.first in 0..14 && laCase.second in 0..14)
        return WherePion(whereOk, laCase, Point(posx, posy))
    }

    fun majPlateau(majPlateau: MajPlateau) {
        fieldTxt.setText(majPlateau.texte)
        regTxt.setText(majPlateau.tour)
    }

}