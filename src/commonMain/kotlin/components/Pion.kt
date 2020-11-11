package components

import cellSize
import com.soywiz.klock.milliseconds
import com.soywiz.korge.input.onMouseDrag
import com.soywiz.korge.tween.moveTo
import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.Point
import com.soywiz.korma.interpolation.Easing
import facePion
import internals.EventBus
import internals.PosePion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class Pion(private val bus: EventBus, val face: Int): Container() {
    var laFace: Image
    init {
        val background = roundRect(cellSize, cellSize, 5.0, color = RGBA(185, 174, 160))
        laFace = image(facePion[face]) {
            size(cellSize, cellSize)
            centerOn(background)
            name((face + 97).toChar().toString())
        }.addTo(background)
        addDragListener()
    }
    private fun addDragListener() {
        var start: Point = pos.copy()
        onMouseDrag { info ->
            if (info.start && !info.end) {
                start = pos.copy()
            } else if (!info.start && !info.end) {
                x = start.x + info.dx
                y = start.y + info.dy
            } else if (info.end) {
                val end = Point(start.x + info.dx + cellSize/2,start.y + info.dy + cellSize/2)
                println("drag pion : face : $face / name : ${laFace.name} $start > $end")
                bus.send(PosePion(this@Pion, start, end))
            }
        }

    }
}