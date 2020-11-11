package components

data class Ligne(var lettres: String = uneLigne) {

    fun place(carac: String, pos: Int) {
        lettres = lettres.replaceRange(pos, pos + carac.length, carac)
    }

    fun isoleMot(pos: Int): Mot {
        val leMot = Mot()
        var dernier = lettres.indexOf(" ", pos) - 1
        if (dernier < 0) dernier = 14
        leMot.position = lettres.substring(0..dernier).lastIndexOf(' ', pos) + 1
        leMot.chaine = lettres.substring(leMot.position..dernier)
        leMot.longueur = dernier - leMot.position + 1
        println(leMot)
        return leMot
    }
}