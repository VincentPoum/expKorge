package components


val faceABC = arrayOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','{')
var ptsABC = arrayOf(1,3,3,2,1,4,2,4,1,8,10,1,2,1,1,3,8,1,1,1,1,4,10,10,10,10,0)
var nbABC = arrayOf(9,2,2,3,15,2,2,2,8,1,1,5,3,6,6,2,1,6,6,6,6,2,1,1,1,1,2)


data class Basket(val listLettres: MutableList<Char> = mutableListOf(), val laPioche: MutableList<Pion> = mutableListOf()) {

    init {
        nbABC.forEachIndexed { index, i -> for (inc in 1..i) listLettres.add(faceABC[index]) }
        listLettres.shuffle()
        listLettres.clear()
        listLettres.addAll(listOf('l','e','b','a','r','s','{'))
        println(listLettres)
    }
    fun tirageBasket(nb:Int):String{
        tirage = listLettres.joinToString("", limit = nb, truncated = "")
        return tirage
    }
}