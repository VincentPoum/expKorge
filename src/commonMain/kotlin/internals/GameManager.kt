package internals

import cellSize
import com.soywiz.klock.milliseconds
import com.soywiz.klogger.Logger
import com.soywiz.korge.tween.hide
import com.soywiz.korge.tween.moveTo
import com.soywiz.korge.tween.show
import com.soywiz.korge.view.image
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.asBitmapSlice
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import com.soywiz.korma.interpolation.Easing
import components.*
import errValid
import facePion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class GameManager(private val bus: EventBus, private val board: Board, private val reglette: Reglette, private val xchgZone: xchgZone, private val basket: Basket) {
    var leTour = Tour()
    val log = Logger("Manager")
    var xchgFlag = false

    init {
        bus.register<PosePion> { if (it.pion.face < 26) posePion(it, false) else posePion(it, true) }
        bus.register<Annulation> { annule() }
        bus.register<Shuffle> { shuffle() }
        bus.register<Validation> { valide() }
        bus.register<WaitForJoker> { regToBoardJok(it.poseJoker) }
        bus.register<Xchange> { xchange() }
        log.info { "in" }
    }

    fun quidValid() {
        errValid = if (leTour.positions.groupBy { it.first }.count() == 1 || leTour.positions.groupBy { it.second }.count() == 1) 0 else 1
        errValid += if (leTour.mots.count() > 0) 0 else 1
        errValid += if (lignes[7].lettres[7] in 'a'..'z') 0 else 1
        var somme = 0
        leTour.mots.forEach { somme += it.score }
        errValid += if (somme > 0) 0 else 1
        bus.send(MajPlateau(dessinPlateau(), dessinTour()))
        bus.send(OkValid)
    }

    fun valide() {
        //TODO fixation des pions
        //TODO score
        //TODO passage au joueur suivant
    }

    fun annulePlateau() {
        while (leTour.positions.count() > 0) {
            val lig = leTour.positions.first().first.first
            val col = leTour.positions.first().first.second
            val char = leTour.positions.first().second
            removeLettre(lig, col)
            leTour.positions.remove(Pair(Pair(lig, col), char))
        }
        leTour.lettres = tirage
        log.info { "après annulation: ${leTour.lettres}" }
        quidValid()
    }

    suspend fun annule() {
        if (!xchgFlag) {
            annulePlateau()
            reglette.content.forEachIndexed { index, pion ->
                if (pion.face == 26) {
                    pion.laFace.name = "{"
                    pion.laFace.image(facePion[26])
                }
                pion.moveTo(2 + reglette.bgReglette.x + (cellSize + 2) * index, reglette.bgReglette.y + 2, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
            }
        } else xchgFlag = false
    }

    suspend fun shuffle() {
        annulePlateau()
        val alea = listOf<Int>(0, 1, 2, 3, 4, 5, 6).shuffled()
        log.info { "alea : $alea" }
        reglette.content.forEachIndexed { index, pion ->
            pion.moveTo(2 + reglette.bgReglette.x + (cellSize + 2) * index, reglette.bgReglette.y + 2 - cellSize, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
            pion.moveTo(2 + reglette.bgReglette.x + (cellSize + 2) * alea[index], reglette.bgReglette.y + 2, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        }
    }

    suspend fun xchange() {
        annulePlateau()
        xchgFlag = true
    }

    private fun addMot(leMot: Mot) {
        leMot.verif()
        val posFin = leMot.position + leMot.longueur
        val ok = leTour.mots.firstOrNull { it.sens == leMot.sens && it.ligOuCol == leMot.ligOuCol && it.position in leMot.position..posFin }
        if (ok == null)
            leTour.mots.add(leMot)
        else
            leTour.mots[leTour.mots.indexOf(ok)] = leMot

    }

    fun placeLettreH(carac: String, posLig: Int, posCol: Int) {
        val laLig = lignes[posLig]
        laLig.place(carac, posCol)
        val leMotH = laLig.isoleMot(posCol)
        leMotH.sens = 'h'
        leMotH.ligOuCol = posLig
        if (leMotH.longueur > 1) addMot(leMotH)
    }

    fun placeLettreV(carac: String, posLig: Int, posCol: Int) {
        val laCol = colonnes[posCol]
        laCol.place(carac, posLig)
        val leMotV = laCol.isoleMot(posLig)
        leMotV.sens = 'v'
        leMotV.ligOuCol = posCol
        if (leMotV.longueur > 1) addMot(leMotV)
    }

    fun placeLettre(carac: String, posLig: Int, posCol: Int) {
        placeLettreH(carac, posLig, posCol)
        placeLettreV(carac, posLig, posCol)
        quidValid()
    }

    fun removeLettre(posLig: Int, posCol: Int) {
        print("rm : ")
        val laLig = lignes[posLig]
        val laCol = colonnes[posCol]
        laLig.place(" ", posCol)
        laCol.place(" ", posLig)
        val motH = leTour.mots.firstOrNull { it.sens == 'h' && it.ligOuCol == posLig && posCol in it.position..(it.position + it.longueur) }
        val motV = leTour.mots.firstOrNull { it.sens == 'v' && it.ligOuCol == posCol && posLig in it.position..(it.position + it.longueur) }
        if (motH != null) {
            val posFin = posCol - motH.position
            val okF = motH.chaine.take(posFin)
            val okL = motH.chaine.takeLast(motH.longueur - posFin - 1)
            print("horiz $okF -- $okL ")
            leTour.mots.removeAt(leTour.mots.indexOf(motH))
            if (okF.isNotEmpty()) placeLettreH(okF.takeLast(1), posLig, posCol - 1)
            if (okL.isNotEmpty()) placeLettreH(okL.take(1), posLig, posCol + 1)
        }
        if (motV != null) {
            val posFin = posLig - motV.position
            val okF = motV.chaine.take(posFin)
            val okL = motV.chaine.takeLast(motV.longueur - posFin - 1)
            print("vertic $okF -- $okL ")
            leTour.mots.removeAt(leTour.mots.indexOf(motV))
            if (okF.isNotEmpty()) placeLettreV(okF.takeLast(1), posLig - 1, posCol)
            if (okL.isNotEmpty()) placeLettreV(okL.take(1), posLig + 1, posCol)
        }
        print(" rmFin")
    }

    fun fillReglette(nbLettres: Int) {
        basket.tirageBasket(nbLettres)
        leTour.lettres = tirage
        reglette.fill(tirage)
    }

    suspend fun regToBoard(boardEnd: WherePion, lePion: PosePion) {
        val char = (lePion.pion.face + 97).toChar()
        leTour.positions.add(Pair(Pair(boardEnd.laCase.first, boardEnd.laCase.second), char))
        val pos = leTour.lettres.indexOf(char.toString())
        log.info { "pos de $char ds leTour.lettres : $pos" }
        leTour.lettres = leTour.lettres.removeRange(pos..pos)
        lePion.pion.moveTo(boardEnd.coOrdAbs.x, boardEnd.coOrdAbs.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        placeLettre((lePion.pion.laFace.name!!), boardEnd.laCase.first, boardEnd.laCase.second)
    }

    suspend fun regToBoardJok(wfJoker: PoseJoker) {
        regToBoard(wfJoker.place, wfJoker.pion)
        quidValid()
    }

    suspend fun boardToBoard(boardStart: WherePion, boardEnd: WherePion, lePion: PosePion) {
        val char = (lePion.pion.face + 97).toChar()
        removeLettre(boardStart.laCase.first, boardStart.laCase.second)
        leTour.positions.remove(Pair(Pair(boardStart.laCase.first, boardStart.laCase.second), char))
        lePion.pion.moveTo(boardEnd.coOrdAbs.x, boardEnd.coOrdAbs.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        leTour.positions.add(Pair(Pair(boardEnd.laCase.first, boardEnd.laCase.second), char))
        placeLettre((lePion.pion.laFace.name!!), boardEnd.laCase.first, boardEnd.laCase.second)
    }

    suspend fun boardToReg(boardStart: WherePion, regEnd: WherePion, lePion: PosePion) {
        val char = (lePion.pion.face + 97).toChar()
        removeLettre(boardStart.laCase.first, boardStart.laCase.second)
        leTour.positions.remove(Pair(Pair(boardStart.laCase.first, boardStart.laCase.second), char))
        lePion.pion.moveTo(regEnd.coOrdAbs.x, regEnd.coOrdAbs.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        leTour.lettres += (lePion.pion.face + 97).toChar().toString()
    }

    suspend fun regToReg(regStart: WherePion, regEnd: WherePion, lePion: PosePion) {
        lePion.pion.moveTo(lePion.start.x, lePion.start.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        // en attendant arrange reglette
    }

    suspend fun posePion(lePion: PosePion, jokOuNon: Boolean) {

        val regStart = reglette.onReglette(lePion.start)
        val regEnd = reglette.onReglette(lePion.end)
        val xchgStart = xchgZone.onXchgZone(lePion.start)
        val xchgEnd = xchgZone.onXchgZone(lePion.end)
        val boardStart = board.onBoard(lePion.start)
        val boardEnd = board.onBoard(lePion.end)
        val oqpBoard =
                if (boardEnd.laCase.first in 0..14 && boardEnd.laCase.second in 0..14)
                    lignes[boardEnd.laCase.first].lettres[boardEnd.laCase.second] != ' '
                else false
        when {
            oqpBoard -> lePion.pion.moveTo(lePion.start.x, lePion.start.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
            !xchgFlag && regStart.orgOk && boardEnd.orgOk -> {
                if (jokOuNon) bus.send(PoseJoker(lePion, boardEnd)) else regToBoard(boardEnd, lePion)
            }
            boardStart.orgOk && boardEnd.orgOk -> {
                boardToBoard(boardStart, boardEnd, lePion)
            }
            boardStart.orgOk && regEnd.orgOk -> {
                if (jokOuNon) {
                    lePion.pion.laFace.name = "{"
                    lePion.pion.laFace.image(facePion[26])
                }
                boardToReg(boardStart, regEnd, lePion)
            }
            regStart.orgOk && regEnd.orgOk -> {
                regToReg(regStart, regEnd, lePion)
            }
            xchgStart.orgOk && regEnd.orgOk ->
                lePion.pion.moveTo(regEnd.coOrdAbs.x, regEnd.coOrdAbs.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
            xchgFlag && regStart.orgOk && xchgEnd.orgOk ->
                lePion.pion.moveTo(xchgEnd.coOrdAbs.x, xchgEnd.coOrdAbs.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
            else -> lePion.pion.moveTo(lePion.start.x, lePion.start.y, 260.milliseconds, Easing.EASE_IN_OUT_QUAD)
        }
        println("après pose ${leTour.positions}")
        quidValid()
    }

    fun dessinPlateau(): String {
        var accu = ""
        for (i in 0..14) accu += "${lignes[i].lettres}\n"
        return accu
    }

    fun dessinTour(): String {
        var retour = "sens : ${leTour.sens} - lettres : ${leTour.lettres}}\n"
        leTour.positions.forEach { retour += "${it}\n" }
        retour += "${leTour.mots}"
        return retour
    }
}