package components

import cellSize
import com.soywiz.klock.milliseconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.tween.hide
import com.soywiz.korge.tween.show
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.cos
import com.soywiz.korma.geom.sin
import facePion
import fieldSize
import internals.*
import leftIndent
import topIndent
import kotlin.math.cos
import kotlin.math.sin

data class AssignJoker(private val bus: EventBus) : Container() {
    lateinit var poseJoker: PoseJoker
    lateinit var leJoker: PosePion
    lateinit var laPlace: WherePion
    fun init() {
        solidRect(fieldSize+10,fieldSize+150,color = Colors["#777777"]){
            position(leftIndent-5, topIndent-5)
            alpha = 0.5}
        roundRect(fieldSize , fieldSize / 2, 5.0, color = Colors["#b9aea0"]) {
            position(leftIndent , topIndent + fieldSize / 4)
            for (i in 'a'..'z') {
                var accu = i.toInt() - 97
                image(facePion[accu]) {
                    size(cellSize , cellSize )
                    position(cellSize * 1.4 * accu.rem(9) + 2, (cellSize * 1.4 * (accu - accu % 9) / 9 + 2))
                    //position((cellSize * (if (accu < 17) 4 else 2) * cos(accu.toDouble())), (cellSize * (if (accu < 17) 4 else 2) * sin(accu.toDouble())))
                    onClick {
                        leJoker.pion.laFace.name = i.toString()
                        leJoker.pion.laFace.image(facePion[i.toInt() - 97])
                        bus.send(WaitForJoker(PoseJoker(leJoker, laPlace)))
                    }
                }
            }
        }
        this.visible = false
        bus.register<PoseJoker> {
            this.show(250.milliseconds)
            leJoker = it.pion
            laPlace = it.place
        }
        bus.register<WaitForJoker> {
            this.hide(250.milliseconds)
            this.visible = false
        }
    }
}