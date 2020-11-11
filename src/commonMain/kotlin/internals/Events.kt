package internals

import com.soywiz.korma.geom.Point
import components.Pion

data class PosePion(val pion: Pion, val start: Point, val end: Point)
data class PoseJoker(val pion: PosePion, val place: WherePion)
data class WherePion(val orgOk: Boolean, val laCase: Pair<Int, Int>, val coOrdAbs: Point)
object Annulation
object Shuffle
object Validation
data class Xchange(val tirage: String)
data class MajPlateau(val texte: String, val tour: String)
object OkValid
data class WaitForJoker(val poseJoker: PoseJoker)
object WaitForXchg