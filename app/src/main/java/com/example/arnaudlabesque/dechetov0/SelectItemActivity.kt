package com.example.arnaudlabesque.dechetov0

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import elements.Element
import elements.ElementFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter


/**
 * Created by arnaud.labesque on 20/11/2017.
 */

class SelectItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_item)


        val tl = findViewById<Toolbar>(R.id.my_toolbar)
        tl.title = intent.getStringExtra("titre") + " - " + SimpleDateFormat("dd-MMMM-yy", Locale.FRENCH).format(Date()).replace("-", " ")
        tl.inflateMenu(R.menu.menu_settings)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        findViewById<View>(R.id.itemSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val isRecipe = intent.getBooleanExtra("recipe", false)

        when {
            intent.getStringExtra("categorie") == "Legume" -> createElements(R.array.legumes, R.drawable.rounded_square_legume, isRecipe)
            intent.getStringExtra("categorie") == "Fruit" -> createElements(R.array.fruits, R.drawable.rounded_square_fruit, isRecipe)
            intent.getStringExtra("categorie") == "Feculent" -> createElements(R.array.feculents, R.drawable.rounded_square_feculent, isRecipe)
            intent.getStringExtra("categorie") == "Laitage" -> createElements(R.array.laitages, R.drawable.rounded_square_laitage, isRecipe)
            intent.getStringExtra("categorie") == "Patisserie" -> createElements(R.array.patisseries, R.drawable.rounded_square_patisserie, isRecipe)
            intent.getStringExtra("categorie") == "Sauce" -> createElements(R.array.sauces, R.drawable.rounded_square_sauce, isRecipe)
            intent.getStringExtra("categorie") == "Viande" -> createElements(R.array.viandes, R.drawable.rounded_square_viande, isRecipe)
            intent.getStringExtra("categorie") == "Autre" -> createElements(R.array.autres, R.drawable.rounded_square_autre, isRecipe)
        }

    }

    private fun createElements(idArray: Int, idBg: Int, isRecipe: Boolean) {
        if (idArray == -1) {
            finish()
        }
        val myarray = resources.getStringArray(idArray)
        var id = 0
        var nbLeg = 0
        var currentLine = LinearLayout(this)
        currentLine.gravity = Gravity.CENTER
        findViewById<LinearLayout>(R.id.globalLayout).addView(currentLine)

        for (element in myarray) {
            //Log.d("test",element)
            var roundedSquareLayout = LinearLayout(this)
            roundedSquareLayout.background = getDrawable(idBg)
            val v = Math.round(120 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
            roundedSquareLayout.layoutParams = ViewGroup.LayoutParams(v, v)
            roundedSquareLayout.orientation = LinearLayout.VERTICAL
            roundedSquareLayout.gravity = Gravity.CENTER

            var imageView = ImageView(this)
            imageView.setImageResource(getIdPicture(element))
            imageView.layoutParams = TableRow.LayoutParams(70, 70, 0.5f)
            roundedSquareLayout.addView(imageView)

            var tv = TextView(this)
            tv.id = id++
            tv.text = element
            tv.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.BLACK)
            roundedSquareLayout.addView(tv)
            if (nbLeg % 4 == 0) {
                var myspace = Space(this)
                myspace.layoutParams = TableLayout.LayoutParams(0, Math.round(1 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), 1f)
                currentLine.addView(myspace, 0)
                currentLine = LinearLayout(this)
                findViewById<LinearLayout>(R.id.globalLayout).addView(currentLine)
                currentLine.gravity = Gravity.CENTER
            }

            roundedSquareLayout.setOnClickListener {
                if (idArray == R.array.fruits || idArray == R.array.legumes) {
                    findViewById<LinearLayout>(R.id.globalLayout).removeAllViews()
                    val newName = element.replace(" ", "")
                    //Log.d("test",newName)resources.getIdentifier(newName,"id",packageName)
                    createElements(getIdArray(newName), idBg, isRecipe)
                } else {

                    var inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var popUpView = inflater.inflate(R.layout.layout_pop_up_bio, null)

                    var values = arrayOf("1 unité", "2 moitiés", "3 tiers", "4 quarts", "2 unités", "3 unités", "4 unités", "5 unités", "8 unités", "10 unités")
                    var numberpicker = popUpView.findViewById<NumberPicker>(R.id.nbPickerQuantite)
                    numberpicker.minValue = 0
                    numberpicker.maxValue = (values.size - 1)
                    numberpicker.wrapSelectorWheel = false
                    numberpicker.displayedValues = values


                    var popup = PopupWindow(popUpView, 1000, ViewGroup.LayoutParams.WRAP_CONTENT)
                    popup.showAtLocation(findViewById<LinearLayout>(R.id.globalLayout), Gravity.LEFT, 0, 0)

                    popUpView.findViewById<Button>(R.id.buttonAnnulerBio).setOnClickListener {
                        finish()
                    }

                    popUpView.findViewById<Button>(R.id.buttonValidateBio).setOnClickListener {
                        var myfactory = ElementFactory()
                        var cbBio = popUpView.findViewById<CheckBox>(R.id.checkBoxBio)
                        var cbNonBio = popUpView.findViewById<CheckBox>(R.id.checkBoxNonBio)
                        var valQuantite = numberpicker.value

                        var isBio = false
                        when {
                            cbBio.isChecked && cbNonBio.isChecked -> {
                                var tvErrorBio = popUpView.findViewById<TextView>(R.id.textErrorBio)
                                tvErrorBio.text = """Attention les cases aliment biologique et non biologique sont cochées !"""
                            }
                            else -> {
                                if (cbBio.isChecked) isBio = true

                                var aliment = myfactory.getCustomElement(element, convertString2Double(values[valQuantite]), true, isBio, popUpView.findViewById<CheckBox>(R.id.checkBoxPotager).isChecked)
                                var aliments = checkWaste(aliment, 0, 1.0, true, isBio, popUpView.findViewById<CheckBox>(R.id.checkBoxPotager).isChecked)
                                if (intent.hasExtra("items")) {
                                    val previousAliments = intent?.getSerializableExtra("items") as ArrayList<Element>
                                    for (previousAliment in previousAliments) {
                                        aliments.add(previousAliment)
                                    }
                                }

                                if (isRecipe) {

                                    val previousIntent = Intent(this, FoodChoiceActivity::class.java)
                                    previousIntent.putExtra("items", aliments)
                                    previousIntent.putExtra("item", aliment)
                                    previousIntent.putExtra("idColorBG", idBg)
                                    setResult(Activity.RESULT_OK, previousIntent)
                                    popup.dismiss()
                                    finish()

                                } else {

                                    aliments.add(aliment)
                                    inflater.inflate(R.layout.activity_treat_item, null)
                                    val myIntent = Intent(this, TreatItemActivity::class.java)
                                    myIntent.putExtra("items", aliments)
                                    myIntent.putExtra("idColorBG", idBg)
                                    myIntent.putExtra("stockMeal", intent.getSerializableExtra("stockMeal"))
                                    popup.dismiss()
                                    finish()
                                    startActivity(myIntent)

                                }
                            }
                        }

                    }
                }


            }

            currentLine.addView(roundedSquareLayout)
            var space = Space(this)
            space.layoutParams = TableLayout.LayoutParams(0, Math.round(1 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), 1f)
            currentLine.addView(space)
            nbLeg++
        }
        var myfinalspace = Space(this)
        myfinalspace.layoutParams = TableLayout.LayoutParams(0, Math.round(1 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), 1f)
        currentLine.addView(myfinalspace, 0)
    }

    private fun checkWaste(aliment: Element, idBG: Int, quantity: Double, isQuotient: Boolean, isBio: Boolean, isFromOwnGarden: Boolean): ArrayList<Element> {
        var listeAliments = ArrayList<Element>()
        var myfactory = ElementFactory()
        if (aliment.isGeneratingBone) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Os de " + aliment.basicName
            listeAliments.add(aliment)
        }
        if (aliment.isGeneratingCore) {
            when (aliment.basicName) {
                "Pomme" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Trognon de " + aliment.basicName
                    listeAliments.add(aliment)
                }
                "Poire" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Trognon de " + aliment.basicName
                    listeAliments.add(aliment)
                }
                "Ananas" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Trognon d' " + aliment.basicName
                    listeAliments.add(aliment)
                }
                else -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Noyau de " + aliment.basicName
                    listeAliments.add(aliment)
                }
            }
        }

        if (aliment.isGeneratingCrust) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Croûte de " + aliment.basicName
            listeAliments.add(aliment)
        }
        if (aliment.isGeneratingFat) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Gras de " + aliment.basicName
            listeAliments.add(aliment)
        }
        if (aliment.isGeneratingFilter) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Filtre de " + aliment.basicName
            listeAliments.add(aliment)
        }
        if (aliment.isGeneratingPeel) {
            when (aliment.basicName) {
                "Noix" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Coque de " + aliment.basicName
                    listeAliments.add(aliment)
                }
                "Noisette" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Coque de " + aliment.basicName
                    listeAliments.add(aliment)
                }
                "NoixDeCoco" -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Coque de " + aliment.basicName
                    listeAliments.add(aliment)
                }
                else -> {
                    var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
                    aliment.basicName = "Épluchures de " + aliment.basicName
                    listeAliments.add(aliment)
                }
            }
        }
        if (aliment.isGeneratingMeatSkin) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Peau de " + aliment.basicName
            listeAliments.add(aliment)
        }
        if (aliment.isGeneratingTail) {
            var aliment = myfactory.getCustomElement(aliment.basicName, quantity, isQuotient, isBio, isFromOwnGarden)
            aliment.basicName = "Queue de " + aliment.basicName
            listeAliments.add(aliment)
        }

        return listeAliments
    }

    private fun convertString2Double(str: String): Double =
            when (str) {
                "4 quarts" -> 0.25
                "3 tiers" -> 0.33
                "2 moitiés" -> 0.5
                "1 unité" -> 1.0
                "2 unités" -> 2.0
                "3 unités" -> 3.0
                "4 unités" -> 4.0
                "5 unités" -> 5.0
                "8 unités" -> 8.0
                "10 unités" -> 10.0
                else -> 1.0
            }

    private fun getIdPicture(element: String?): Int =
            when (element) {
                "Légumes Bulbes" -> R.drawable.legumebulbe
                "Légumes Tiges" -> R.drawable.legumetige
                "Légumes Racines" -> R.drawable.legumesracine
                "Légumes Graines" -> R.drawable.legumegraine
                "Légumes Fruitiers" -> R.drawable.legumefruit
                "Légumes Fleurs" -> R.drawable.legumefleur
                "Légumes Feuilles" -> R.drawable.legumefeuille
                "Fruits à pépins" -> R.drawable.fruitpepins
                "Fruits secs" -> R.drawable.fruitsec
                "Fruits rouges" -> R.drawable.fruitrouge
                "Fruits exotiques" -> R.drawable.fruitexotique
                "Fruits à noyau" -> R.drawable.fruitnoyau
                "Agrumes" -> R.drawable.agrume
                "Asperge" -> R.drawable.asperge
                "Rhubarbe" -> R.drawable.rhubarbe
                "Betterave" -> R.drawable.betterave
                "Carotte" -> R.drawable.carotte
                "Champignon" -> R.drawable.champignon
                "Céleri rave" -> R.drawable.celerirave
                "Navet" -> R.drawable.navet
                "Panais" -> R.drawable.panais
                "Patate douce" -> R.drawable.patatedouce
                "Pomme de terre" -> R.drawable.patate
                "Radis rouge" -> R.drawable.radisrouge
                "Radis noir" -> R.drawable.radisnoir
                "Salsifis" -> R.drawable.salsifis
                "Haricot" -> R.drawable.haricot
                "Petit pois" -> R.drawable.petitspois
                "Pois à écosser" -> R.drawable.poisaecosser
                "Aubergine" -> R.drawable.aubergine
                "Avocat" -> R.drawable.avocat
                "Châtaigne" -> R.drawable.chataigne
                "Citrouille,Courge,Potiron" -> R.drawable.courges
                "Concombre" -> R.drawable.concombre
                "Cornichon" -> R.drawable.cornichon
                "Courgette" -> R.drawable.courgette
                "Poivron" -> R.drawable.poivrons
                "Tomate" -> R.drawable.tomate
                "Artichaut" -> R.drawable.artichaux
                "Brocoli" -> R.drawable.brocoli
                "Chou-fleur" -> R.drawable.choufleur
                "Blette" -> R.drawable.blette
                "Céleri branche" -> R.drawable.celeribranche
                "Chou vert" -> R.drawable.chouxdebruxellechouvert
                "Chou de Bruxelles" -> R.drawable.chouxdebruxellechouvert
                "Endive" -> R.drawable.endives
                "Epinard" -> R.drawable.epinard
                "Salade entière" -> R.drawable.salade
                "Salade en sachet" -> R.drawable.saladesachet
                "Poireau" -> R.drawable.poireau
                "Ail" -> R.drawable.ail
                "Fenouil" -> R.drawable.fenouil
                "Oignon" -> R.drawable.oignon
                "Echalote" -> R.drawable.echalotte
                "Melon" -> R.drawable.melon
                "Pastèque" -> R.drawable.pasteque
                "Coing" -> R.drawable.coing
                "Figue" -> R.drawable.figue
                "Kaki" -> R.drawable.kaki
                "Kiwi" -> R.drawable.kiwi
                "Poire" -> R.drawable.poire
                "Pomme" -> R.drawable.pomme
                "Raisin" -> R.drawable.raisin
                "Noix" -> R.drawable.noix
                "Noisette" -> R.drawable.noisette
                "Amande" -> R.drawable.amande
                "Petits fruits" -> R.drawable.petitsfruitsrouges
                "Fraise" -> R.drawable.fraise
                "Noix de coco" -> R.drawable.noixdecoco
                "Ananas" -> R.drawable.ananas
                "Banane" -> R.drawable.banane
                "Fruit de la passion" -> R.drawable.fruitdelapassion
                "Grenade" -> R.drawable.grenade
                "Litchi" -> R.drawable.litchi
                "Mangue" -> R.drawable.mangue
                "Papaye" -> R.drawable.papaye
                "Abricot" -> R.drawable.abricot
                "Cerise" -> R.drawable.cerise
                "Datte" -> R.drawable.dattes
                "Pêche" -> R.drawable.peche
                "Nectarine" -> R.drawable.nectarine
                "Brugnon" -> R.drawable.brugnon
                "Prune" -> R.drawable.prune
                "Citron" -> R.drawable.citron
                "Clémentine" -> R.drawable.clementine
                "Mandarine" -> R.drawable.mandarine
                "Orange" -> R.drawable.orange
                "Pamplemousse" -> R.drawable.pamplemousse
                "Boeuf" -> R.drawable.boeuf
                "Volaille" -> R.drawable.poulet
                "Porc" -> R.drawable.porc
                "Mouton" -> R.drawable.agneau
                "Poisson" -> R.drawable.poisson
                "Œuf" -> R.drawable.oeuf
                "Lait" -> R.drawable.lait
                "Crème" -> R.drawable.creme_fraiche
                "Yaourt" -> R.drawable.yaourt
                "Fromage" -> R.drawable.fromage
                "Pâte" -> R.drawable.pates
                "Riz" -> R.drawable.riz
                "Semoule" -> R.drawable.semoule
                "Lentilles" -> R.drawable.lentilles
                "Pain" -> R.drawable.pain
                "Gâteau" -> R.drawable.gateau
                "Ketchup" -> R.drawable.ketchup
                "Mayonnaise" -> R.drawable.mayonnaise
                "Moutarde" -> R.drawable.moutarde
                "Vinaigrette" -> R.drawable.vinaigrette
                "Sauce tomate" -> R.drawable.sauce_tomate
                "Pizza" -> R.drawable.pizza
                "Plat surgelé" -> R.drawable.plats_surgeles
                "Plat préparé" -> R.drawable.plats_prepares
                "Thé" -> R.drawable.the
                "Café" -> R.drawable.cafe
                else -> -1
            }

    private fun getIdArray(name: String): Int =
            when (name) {
                "LégumesTiges" -> R.array.LégumesTiges
                "LégumesRacines" -> R.array.LégumesRacines
                "LégumesGraines" -> R.array.LégumesGraines
                "LégumesFruitiers" -> R.array.LégumesFruitiers
                "LégumesFleurs" -> R.array.LégumesFleurs
                "LégumesFeuilles" -> R.array.LégumesFeuilles
                "LégumesBulbes" -> R.array.LégumesBulbes
                "Fruitsàpépins" -> R.array.Fruitsàpépins
                "Fruitssecs" -> R.array.Fruitssecs
                "Fruitsrouges" -> R.array.Fruitsrouges
                "Fruitsexotiques" -> R.array.Fruitsexotiques
                "Fruitsànoyau" -> R.array.Fruitsànoyau
                "Agrumes" -> R.array.Agrumes
                else -> -1
            }

}

