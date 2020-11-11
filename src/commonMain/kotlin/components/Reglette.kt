package components

import cellSize
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import errValid
import fieldSize
import internals.*
import leftIndent
import topIndent

class Reglette(private val bus: EventBus) : Container() {
    lateinit var bgReglette: RoundRect
    lateinit var content: MutableList<Pion>
    lateinit var undoBtn: RoundRect
    lateinit var shuffleBtn: RoundRect
    lateinit var validBtn: RoundRect
    lateinit var xchgBtn: RoundRect
    suspend fun init() {
        bgReglette = roundRect(2 + (2 + cellSize) * 7, 4 + cellSize, 5.0, color = Colors["#bbae9e"]) {
            position(leftIndent, topIndent + fieldSize + 100)
            centerXOnStage()
        }
        content = mutableListOf()
        undoBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["undo.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alignTopToTopOf(bgReglette)
            alignLeftToRightOf(bgReglette, cellSize)
            onClick {
                bus.send(Annulation)
            }
        }.addTo(this)
        shuffleBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["undo.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alignTopToTopOf(bgReglette)
            alignLeftToRightOf(bgReglette, cellSize * 3)
            onClick {
                bus.send(Shuffle)
            }
        }.addTo(this)
        validBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["valid.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alpha(0.5)
            alignTopToTopOf(bgReglette)
            alignRightToLeftOf(bgReglette, cellSize)
            onClick {
                if (errValid == 0) bus.send(Validation)
            }
        }.addTo(this)
        xchgBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["valid.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alpha(0.5)
            alignTopToTopOf(bgReglette)
            alignRightToLeftOf(bgReglette, cellSize * 3)
            onClick {
                bus.send(Xchange(""))
            }
        }.addTo(this)
        bus.register<OkValid> { okValid() }
    }

    fun fill(tirage: String) {
        tirage.mapIndexed { index: Int, i: Char -> content.add(Pion(bus, i.toInt() - 97).addTo(this).position(2 + bgReglette.x + (cellSize + 2) * index, bgReglette.y + 2).name("pion$index")) }
    }

    fun onReglette(lePoint: Point): WherePion {
        val abs = ((lePoint.x - bgReglette.x - 2) / cellSize).toInt()
        val posx = bgReglette.x + 2 + (2 + cellSize) * abs
        val ord = ((lePoint.y - bgReglette.y - 2) / cellSize).toInt()
        val posy = bgReglette.y + 2 + (2 + cellSize) * ord
        val whereOk = (abs in 0..6 && ord == 0)
        return WherePion(whereOk, Pair(abs, ord), Point(posx, posy))
    }

    fun okValid() {
        if (errValid == 0) {
            validBtn.alpha = 1.0
            validBtn.color = RGBA(0, 255, 0)
        } else {
            validBtn.alpha = 0.5
            validBtn.color = RGBA(255, 0, 0)
        }

    }
}