package components

import cellSize
import com.soywiz.klock.milliseconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.tween.hide
import com.soywiz.korge.tween.moveTo
import com.soywiz.korge.tween.show
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import com.soywiz.korma.interpolation.Easing
import facePion
import fieldSize
import internals.*
import leftIndent
import topIndent

data class xchgZone(private val bus: EventBus, private val reglette: Reglette) : Container() {

    lateinit var content: MutableList<Pion>
    lateinit var undoBtn: RoundRect
    lateinit var validBtn: RoundRect

    suspend fun init() {
        solidRect(fieldSize + 10, fieldSize + 10, color = Colors["#777777"]) {
            position(leftIndent - 5, topIndent - 5)
            alpha = 0.5
        }
        undoBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["undo.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alignTopToTopOf(reglette.bgReglette)
            alignLeftToRightOf(reglette.bgReglette, cellSize)
            onClick {
                annule()
            }
        }.addTo(this)
        validBtn = roundRect(cellSize, cellSize, 5, color = RGBA(185, 174, 160))
        {
            image(resourcesVfs["valid.png"].readBitmap()) {
                size(cellSize * 0.6, cellSize * 0.6)
                centerOn(this@roundRect)
            }
            alpha(0.5)
            alignTopToTopOf(reglette.bgReglette)
            alignRightToLeftOf(reglette.bgReglette, cellSize)
            onClick {
                bus.send(Validation)
            }
        }.addTo(this)
        content = mutableListOf()
        this.visible = false
        bus.register<Xchange> {
            this.show(250.milliseconds)
            fillZone()
        }
        bus.register<WaitForXchg> {
            this.hide(250.milliseconds)
            this.visible = false
        }
    }

    fun onXchgZone(lePoint: Point): WherePion {
        val abs = ((lePoint.x - reglette.bgReglette.x - 2) / cellSize).toInt()
        val posx = reglette.bgReglette.x + 2 + (2 + cellSize) * abs
        val ord = ((lePoint.y - reglette.bgReglette.y - 2 * cellSize) / cellSize).toInt()
        val posy = reglette.bgReglette.y + 2 + 2 * cellSize +(2 + cellSize) * ord
        val whereOk = (abs in 0..6 && ord == 0)
        return WherePion(whereOk, Pair(abs, ord), Point(posx, posy))
    }

    suspend fun fillZone() {
        reglette.content.forEachIndexed { index, pion ->
            if (pion.face == 26) {
                pion.laFace.name = "{"
                pion.laFace.image(facePion[26])
            }
            reglette.xchgBtn.visible = false
            reglette.validBtn.visible = false
            reglette.undoBtn.visible = false
            reglette.shuffleBtn.visible = false
            pion.moveTo(2 + reglette.bgReglette.x + (cellSize + 2) * index, reglette.bgReglette.y + 2 * cellSize, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        }
    }

    suspend fun annule() {
        reglette.content.forEachIndexed { index, pion ->
            if (pion.face == 26) {
                pion.laFace.name = "{"
                pion.laFace.image(facePion[26])
            }
            reglette.xchgBtn.visible = true
            reglette.validBtn.visible = true
            reglette.undoBtn.visible = true
            reglette.shuffleBtn.visible = true
            pion.moveTo(2 + reglette.bgReglette.x + (cellSize + 2) * index, reglette.bgReglette.y , 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        }
        this.hide(250.milliseconds)
        this.visible = false
    }
}