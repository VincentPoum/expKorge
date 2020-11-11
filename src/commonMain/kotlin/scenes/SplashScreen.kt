package scenes

import cellSize
import com.soywiz.klock.seconds
import com.soywiz.klogger.Logger
import com.soywiz.korau.sound.NativeSoundChannel
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.image
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.delay
import com.soywiz.korio.file.std.resourcesVfs
import fieldSize
import initialisation
import leftIndent
import topIndent
import util.LoadingProxyScene
import virtualResolution
import windowResolution

class SplashScreen : Scene() {

    private lateinit var bg: Image
    //private lateinit var bgMusic: NativeSoundChannel
    val log = Logger("Splash")
    override suspend fun Container.sceneInit() {
        //bgMusic = resourcesVfs["sounds/intro_loop.wav"].readMusic().play()
        bg = image(resourcesVfs["graphics/splash_scene/intro_bg.png"].readBitmap()) {
            smoothing = false
            alpha = 0.0
        }
        Logger.defaultLevel = Logger.Level.DEBUG

        log.info { "Initialisation ... " }
    }

    override suspend fun sceneAfterInit() {
        bg.tween(bg::alpha[1.0], time = 1.seconds)
        //delay(2.seconds)

        initialisation()
        cellSize = virtualResolution.width / 18.0
        fieldSize = 32 + 15 * cellSize
        leftIndent = (virtualResolution.width - fieldSize) / 2
        topIndent = 150.0
        log.info {"virtual : ${virtualResolution.width}/${virtualResolution.height}"}
        log.info {"window : ${windowResolution.width}/${windowResolution.height}"}
        log.info { "cell : $cellSize, field : $fieldSize, left : $leftIndent, top : $topIndent" }
        bg.tween(bg::alpha[0.0], time = .5.seconds)
        sceneContainer.changeTo<LoadingProxyScene>(LoadingProxyScene::class::class,
                LoadingProxyScene.NextScreen(MainScene::class),
                time = .5.seconds)
    }

/*
    override suspend fun sceneBeforeLeaving() {
        sceneContainer.tween(bgMusic::volume[0.0], time = .4.seconds)
        bgMusic.stop()
    }
*/

}