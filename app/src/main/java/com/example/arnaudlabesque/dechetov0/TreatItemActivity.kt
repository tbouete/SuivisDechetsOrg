package com.example.arnaudlabesque.dechetov0

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import elements.*
import org.w3c.dom.Text
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream


/**
 * Created by arnaud.labesque on 08/01/2018.
 */

class TreatItemActivity : AppCompatActivity() {
    var stockMeal = StockMeals()
    var nbElements = 0

    fun createElement(aliment: Element, elementName: String, idBg: Int) {
        var id = 0
        nbElements++
        var currentLine = LinearLayout(this)
        currentLine.gravity = Gravity.CENTER
        findViewById<LinearLayout>(R.id.treatableItem).addView(currentLine)

        var linearLayout = LinearLayout(this)
        if (idBg == 0) {
            linearLayout.background = getDrawable(R.drawable.rounded_square)
        } else {
            linearLayout.background = getDrawable(idBg)
        }

        val v = Math.round(100 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        linearLayout.layoutParams = ViewGroup.LayoutParams(v, v)
        linearLayout.gravity = Gravity.CENTER
        linearLayout.tag = elementName
        var tv = TextView(this)
        tv.id = id++
        tv.text = elementName
        tv.gravity = Gravity.CENTER
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.setTextColor(Color.BLACK)
        linearLayout.addView(tv)


        linearLayout.setOnClickListener {
            var inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var popUpView = inflater.inflate(R.layout.layout_pop_up_treat_item, null)

            var tvTitrePopup = popUpView.findViewById<TextView>(R.id.tvTitrePopupTreatItem)
            tvTitrePopup.text = "Gestion de l'aliment " + elementName


            var popup = PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popup.showAtLocation(findViewById<LinearLayout>(R.id.globalLayout), Gravity.BOTTOM, 0, 0)

            popUpView.findViewById<LinearLayout>(R.id.btnComposte).setOnClickListener{handleCompostedWaste(aliment,linearLayout,popup)}
            popUpView.findViewById<LinearLayout>(R.id.btnFrigo).setOnClickListener{handleStockedWaste(aliment,linearLayout,popup)}
            popUpView.findViewById<LinearLayout>(R.id.btnAssiette).setOnClickListener{handleEatenWaste(aliment,linearLayout,popup)}
            popUpView.findViewById<LinearLayout>(R.id.btnChien).setOnClickListener{handleFedWaste(aliment,linearLayout,popup)}
            popUpView.findViewById<LinearLayout>(R.id.btnPoubelle).setOnClickListener{handleThrowedWaste(aliment,linearLayout,popup)}
        }

        currentLine.addView(linearLayout)
        var space = Space(this)
        space.layoutParams = TableLayout.LayoutParams(0, Math.round(1 * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), 1f)
        currentLine.addView(space)
    }

    fun handleCompostedWaste(aliment: Element, layout: LinearLayout, popup : PopupWindow){
        stockMeal.listSEM.get( stockMeal.listSEM.lastIndex ).addToComposted(aliment)
        layout.visibility = View.INVISIBLE
        popup.dismiss()
        nbElements--
        if(nbElements==0){
            var btnValidate = Button(this)
            btnValidate.text = "Valider"
            btnValidate.background = getDrawable(R.drawable.green_button_border)
            btnValidate.setOnClickListener{
                var myIntent = Intent(this, MealCompositionActivity::class.java)
                myIntent.putExtra("stockMeal", stockMeal)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityIfNeeded(myIntent,0)
            }
            findViewById<LinearLayout>(R.id.treatableItem).addView(btnValidate)
        }
    }
    fun handleStockedWaste(aliment: Element, layout: LinearLayout, popup : PopupWindow){
        stockMeal.listSEM.get( stockMeal.listSEM.lastIndex ).addToStocked(aliment)
        layout.visibility = View.INVISIBLE
        popup.dismiss()
        nbElements--
        if(nbElements==0){
            var btnValidate = Button(this)
            btnValidate.text = "Valider"
            btnValidate.background = getDrawable(R.drawable.green_button_border)
            btnValidate.setOnClickListener{
                var myIntent = Intent(this, MealCompositionActivity::class.java)
                myIntent.putExtra("stockMeal", stockMeal)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityIfNeeded(myIntent,0)
            }
            findViewById<LinearLayout>(R.id.treatableItem).addView(btnValidate)
        }
    }
    fun handleFedWaste(aliment: Element, layout: LinearLayout, popup : PopupWindow){
        stockMeal.listSEM.get( stockMeal.listSEM.lastIndex ).addToFed(aliment)
        layout.visibility = View.INVISIBLE
        popup.dismiss()
        nbElements--
        if(nbElements==0){
            var btnValidate = Button(this)
            btnValidate.text = "Valider"
            btnValidate.background = getDrawable(R.drawable.green_button_border)
            btnValidate.setOnClickListener{
                var myIntent = Intent(this, MealCompositionActivity::class.java)
                myIntent.putExtra("stockMeal", stockMeal)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityIfNeeded(myIntent,0)
            }
            findViewById<LinearLayout>(R.id.treatableItem).addView(btnValidate)
        }
    }
    fun handleEatenWaste(aliment: Element, layout: LinearLayout, popup : PopupWindow){
        stockMeal.listSEM.get( stockMeal.listSEM.lastIndex ).addToEaten(aliment)
        layout.visibility = View.INVISIBLE
        popup.dismiss()
        nbElements--
        if(nbElements==0){
            var btnValidate = Button(this)
            btnValidate.text = "Valider"
            btnValidate.background = getDrawable(R.drawable.green_button_border)
            btnValidate.setOnClickListener{
                var myIntent = Intent(this, MealCompositionActivity::class.java)
                myIntent.putExtra("stockMeal", stockMeal)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityIfNeeded(myIntent,0)
            }
            findViewById<LinearLayout>(R.id.treatableItem).addView(btnValidate)
        }
    }
    fun handleThrowedWaste(aliment: Element, layout: LinearLayout, popup : PopupWindow){
        stockMeal.listSEM.get( stockMeal.listSEM.lastIndex ).addToThrowed(aliment)
        layout.visibility = View.INVISIBLE
        popup.dismiss()
        nbElements--
        if(nbElements==0){
            var btnValidate = Button(this)
            btnValidate.text = "Valider"
            btnValidate.background = getDrawable(R.drawable.green_button_border)
            btnValidate.setOnClickListener{
                var myIntent = Intent(this, MealCompositionActivity::class.java)
                myIntent.putExtra("stockMeal", stockMeal)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityIfNeeded(myIntent,0)
            }
            findViewById<LinearLayout>(R.id.treatableItem).addView(btnValidate)
        }
    }

    fun treatItem() {
        val aliment = intent.getSerializableExtra("item") as Element
        val idBG = intent.getIntExtra("idColorBG", 0)
        stockMeal = intent.getSerializableExtra("stockMeal") as StockMeals
        createElement(aliment, aliment.basicName, idBG)

        val tl = findViewById<Toolbar>(R.id.my_toolbar)
        tl.title = "Traitement de l'aliment " + aliment.basicName + " - " + SimpleDateFormat("dd-MMMM-yy", Locale.FRENCH).format(Date()).replace("-", " ")
        tl.inflateMenu(R.menu.menu_settings)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        findViewById<View>(R.id.itemSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<View>(R.id.btnCancelTreat).setOnClickListener {
            finish()
        }

        checkWaste(aliment, idBG)
    }

    fun treatRecipe() {
        val recipe = intent.getSerializableExtra("recipe") as ComposedElement
        stockMeal = intent.getSerializableExtra("stockMeal") as StockMeals

        createElement(recipe, recipe.basicName,0)

        val tl = findViewById<Toolbar>(R.id.my_toolbar)
        tl.title = "Traitement de la recette " + recipe.basicName + " - " + SimpleDateFormat("dd-MMMM-yy", Locale.FRENCH).format(Date()).replace("-", " ")
        tl.inflateMenu(R.menu.menu_settings)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        findViewById<View>(R.id.itemSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<View>(R.id.btnCancelTreat).setOnClickListener {
            finish()
        }

        for(e in recipe.composition) {
            checkWaste(e,0)
        }

    }

    fun checkWaste(aliment : Element, idBG : Int) {

        if (aliment.isGeneratingBone) {
            createElement(aliment, "Os de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingCore) {
            when (aliment.basicName){
                "Pomme" -> createElement(aliment, "Trognon de " + aliment.basicName, idBG)
                "Poire" -> createElement(aliment, "Trognon de " + aliment.basicName, idBG)
                "Ananas" -> createElement(aliment, "Trognon d' " + aliment.basicName, idBG)
                else ->createElement(aliment, "Noyau de " + aliment.basicName, idBG)
            }
        }

        if (aliment.isGeneratingCrust) {
            createElement(aliment, "Croûte de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingFat) {
            createElement(aliment, "Gras de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingFilter) {
            createElement(aliment, "Filtre de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingPeel) {
            createElement(aliment, "Peau de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingMeatSkin) {
            createElement(aliment, "Peau de " + aliment.basicName, idBG)
        }
        if (aliment.isGeneratingTail) {
            createElement(aliment, "Queue de " + aliment.basicName, idBG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treat_item)

        if(intent.getBooleanExtra("isRecipe", false)) {
            treatRecipe()
        } else {
            treatItem()
        }

    }

}
