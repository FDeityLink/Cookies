package io.fdeitylink.cookie

import java.util.EnumMap

import javafx.application.Application

import javafx.stage.Stage
import javafx.scene.Scene

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import javafx.geometry.Insets

import javafx.scene.control.Button

import javafx.scene.image.ImageView

import javafx.scene.text.Text
import javafx.scene.text.Font

import javafx.scene.control.Tooltip

import javafx.beans.property.SimpleDoubleProperty

fun main(args: Array<String>) = Application.launch(Cookies::class.java, *args)

class Cookies : Application() {
    private val font = Font.font(20.0)

    private var hitInfinity = false
    private var cookies = SimpleDoubleProperty(0.0)

    private var factories = SimpleDoubleProperty(1.0)

    private lateinit var cookiesLabel: Text
    private lateinit var factoriesLabel: Text

    override fun start(primaryStage: Stage) {
        initCookiesLabel()
        initFactoriesLabel()

        val hbox = HBox(10.0)
        hbox.children.addAll(*initButtons())

        val root = VBox(10.0)
        root.children.addAll(hbox, cookiesLabel, factoriesLabel)
        root.padding = Insets(10.0, 10.0, 10.0, 10.0)

        primaryStage.scene = Scene(root)

        primaryStage.title = "Cookies!"
        primaryStage.icons += ResourceManager.getImage("Cookie.png")

        primaryStage.sizeToScene()
        primaryStage.show()
        // primaryStage.width = root.width + 50.0
        // primaryStage.height = root.height + 50.0
        primaryStage.requestFocus()
    }

    private fun initCookiesLabel() {
        cookiesLabel = Text("🍪: ${cookies.value}")
        cookiesLabel.font = font

        cookies.addListener { _, _, newValue ->
            cookiesLabel.text = "🍪: $newValue"
            if (hitInfinity || Double.POSITIVE_INFINITY == newValue) {
                hitInfinity = true
                cookiesLabel.text += ". Feeling depressed yet?"
            }
        }
    }

    private fun initFactoriesLabel() {
        factoriesLabel = Text("🏭 : ${factories.value}")
        factoriesLabel.font = font
        factories.addListener { _, _, newValue -> factoriesLabel.text = "🏭 : $newValue" }
    }

    private fun initButtons(): Array<Button> {
        val buttons = EnumMap<CookiesOption, Button>(CookiesOption::class.java)
        enumValues<CookiesOption>().forEach { buttons.put(it, Button()) }

        buttons[CookiesOption.COOKIE]!!.graphic = ImageView(ResourceManager.getImage("Cookie.png"))
        buttons[CookiesOption.COOKIE]!!.tooltip = Tooltip("Produce ${factories.value} cookies")
        buttons[CookiesOption.COOKIE]!!.setOnAction { cookies.value += factories.value }

        factories.addListener { _, _, newValue ->
            buttons[CookiesOption.COOKIE]!!.tooltip.text = "Produce $newValue cookies"
        }

        buttons[CookiesOption.FACTORY]!!.graphic = ImageView(ResourceManager.getImage("Factory.png"))
        buttons[CookiesOption.FACTORY]!!.tooltip =
                Tooltip("""Trade all your cookies for ${Math.pow(cookies.value, 1.1)} more factories
Allows you to attain 100% depression faster""")
        buttons[CookiesOption.FACTORY]!!.setOnAction {
            factories.value += Math.pow(cookies.value, 1.1)
            cookies.value = 0.0
        }
        cookies.addListener { _, _, newValue ->
            buttons[CookiesOption.FACTORY]!!.tooltip.text = """Trade all your cookies for ${Math.pow(newValue.toDouble(), 1.1)} more factories
Allows you to attain 100% depression faster"""
        }

        buttons[CookiesOption.RESTART]!!.graphic = ImageView(ResourceManager.getImage("Restart.png"))
        buttons[CookiesOption.RESTART]!!.tooltip = Tooltip("Restart")
        buttons[CookiesOption.RESTART]!!.setOnAction {
            hitInfinity = false
            cookies.value = 0.0
            factories.value = 1.0

            cookiesLabel.text = "🍪: ${cookies.value}"
            factoriesLabel.text = "🏭 : ${factories.value}"
        }

        return buttons.values.toTypedArray()
    }

    private enum class CookiesOption {
        COOKIE,
        FACTORY,
        RESTART
    }
}