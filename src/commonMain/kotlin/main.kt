import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.BitmapFont
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import components.*
import scenes.MainScene
import scenes.SplashScreen
import util.LoadingProxyScene
import util.Resolution
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
val resLettres = arrayOf("lettres/imgA.png","lettres/imgB.png","lettres/imgC.png","lettres/imgD.png","lettres/imgE.png","lettres/imgF.png","lettres/imgG.png","lettres/imgH.png","lettres/imgI.png","lettres/imgJ.png","lettres/imgK.png","lettres/imgL.png","lettres/imgM.png","lettres/imgN.png","lettres/imgO.png","lettres/imgP.png","lettres/imgQ.png","lettres/imgR.png","lettres/imgS.png","lettres/imgT.png","lettres/imgU.png","lettres/imgV.png","lettres/imgW.png","lettres/imgX.png","lettres/imgY.png","lettres/imgZ.png","lettres/img0.png")
var dico = listOf<String>()
lateinit var fontG: BitmapFont


suspend fun initialisation(){
	suspend fun getResourceText(path: String): String { return resourcesVfs[path].readString() }
	val deux = getResourceText("dico/deux.txt")
	val trois = getResourceText("dico/trois.txt")
	val quatre = getResourceText("dico/quatre.txt")
	val cinq = getResourceText("dico/cinq.txt")
	val six = getResourceText("dico/six.txt")
	val sept = getResourceText("dico/sept.txt")
	val huit = getResourceText("dico/huit.txt")
	val neuf = getResourceText("dico/neuf.txt")
	val dix = getResourceText("dico/dix.txt")
	val onze = getResourceText("dico/onze.txt")
	val douze = getResourceText("dico/douze.txt")
	val treize = getResourceText("dico/treize.txt")
	val quatorze = getResourceText("dico/quatorze.txt")
	val quinze = getResourceText("dico/quinze.txt")
	dico = listOf(deux,trois,quatre,cinq,six,sept,huit,neuf,dix,onze,douze,treize,quatorze,quinze)
	println("dico : ${dico[0]}")
	for (i in 0..26) {
		facePion.add(i, resourcesVfs[resLettres[i]].readBitmap())
	}
	for (i in 0..14) {
		lignes.add(Ligne())
		colonnes.add(Ligne())
	}
	fontG = resourcesVfs["fonts/georgia.fnt"].readBitmapFont()
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
