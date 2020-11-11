package scenes

import com.soywiz.klogger.Logger
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import components.*
import internals.GameManager
import internals.EventBus

class MainScene() : Scene() {
    override suspend fun Container.sceneInit() {
        val bus = EventBus(this@MainScene)
        val board = Board(bus)
        val reglette = Reglette(bus)
        val basket = Basket()
        val assignJoker = AssignJoker(bus)
        val xchgZone = xchgZone(bus, reglette)
        val game = GameManager(bus, board, reglette, xchgZone, basket)
        Logger.defaultLevel = Logger.Level.DEBUG
        val log = Logger("Main")
        log.info { "board ... " }
        addChild(board)
        board.init()
        addChild(reglette)
        reglette.init()
        addChild(assignJoker)
        assignJoker.init()
        addChild(xchgZone)
        xchgZone.init()
        game.fillReglette(7)
    }
}
