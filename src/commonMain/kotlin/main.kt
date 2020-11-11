import com.soywiz.klogger.Logger
import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.geom.vector.roundRect
import com.soywiz.korma.interpolation.Easing
import components.*
import scenes.MainScene
import scenes.SplashScreen
import util.LoadingProxyScene
import util.Resolution
import kotlin.properties.Delegates
import kotlin.reflect.KClass


const val debug = false
const val playBackgroundMusic = false

/**
 * Virtual size which gets projected onto the [windowResolution]
 */
val virtualResolution = Resolution(width = 540, height = 960)
/**
 * Actual window size
 */
val windowResolution = virtualResolution
val backgroundColor = Colors["#2C2C2C"]

var cellSize = 0.0
var fieldSize = 0.0
var leftIndent = 0.0
var topIndent = 0.0
var facePion = mutableListOf<Bitmap>()
val resLettres = arrayOf("imgA.png","imgB.png","imgC.png","imgD.png","imgE.png","imgF.png","imgG.png","imgH.png","imgI.png","imgJ.png","imgK.png","imgL.png","imgM.png","imgN.png","imgO.png","imgP.png","imgQ.png","imgR.png","imgS.png","imgT.png","imgU.png","imgV.png","imgW.png","imgX.png","imgY.png","imgZ.png","img0.png")
var dico = listOf<String>()


suspend fun initialisation(){
	suspend fun getResourceText(path: String): String { return resourcesVfs[path].readString() }
	val deux = getResourceText("deux.txt")
	val trois = getResourceText("trois.txt")
	val quatre = getResourceText("quatre.txt")
	val cinq = getResourceText("cinq.txt")
	val six = getResourceText("six.txt")
	val sept = getResourceText("sept.txt")
	val huit = getResourceText("huit.txt")
	val neuf = getResourceText("neuf.txt")
	val dix = getResourceText("dix.txt")
	val onze = getResourceText("onze.txt")
	val douze = getResourceText("douze.txt")
	val treize = getResourceText("treize.txt")
	val quatorze = getResourceText("quatorze.txt")
	val quinze = getResourceText("quinze.txt")
	dico = listOf(deux,trois,quatre,cinq,six,sept,huit,neuf,dix,onze,douze,treize,quatorze,quinze)
	println("dico : ${dico[0]}")
	for (i in 0..26) {
		facePion.add(i, resourcesVfs[resLettres[i]].readBitmap())
	}
	for (i in 0..14) {
		lignes.add(Ligne())
		colonnes.add(Ligne())
	}
}

suspend fun main() = Korge(Korge.Config(module = MainModule))

object MainModule : Module() {

	override val mainScene: KClass<out Scene>
		get() = SplashScreen::class
	override val title: String
		get() = "Le Bar C S"
	override val windowSize: SizeInt
		get() = SizeInt(windowResolution.width, windowResolution.height)
	override val size: SizeInt
		get() = SizeInt(windowResolution.width, windowResolution.height)
	override val scaleAnchor: Anchor
		get() = Anchor.MIDDLE_CENTER
	override val scaleMode: ScaleMode
		get() = ScaleMode.SHOW_ALL
	override val clipBorders: Boolean
		get() = true
	override val bgcolor: RGBA
		get() = backgroundColor

	override suspend fun AsyncInjector.configure() {
		mapPrototype { MainScene() }
		mapPrototype { SplashScreen() }
		mapPrototype { LoadingProxyScene(get(), getOrNull()) }
	}
}
