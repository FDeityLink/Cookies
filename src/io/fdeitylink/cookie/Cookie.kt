package io.fdeitylink.cookie

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

fun main(args: Array<String>) = Application.launch(Cookie::class.java, *args)

class Cookie : Application() {
    private var hitInfinity = false
    private var cookies = SimpleDoubleProperty(0.0)

    private var factories = SimpleDoubleProperty(1.0)

    override fun start(primaryStage: Stage) {
        val root = VBox(10.0)

        val cookiesLabel = Text("🍪: ${cookies.value}")
        cookiesLabel.font = Font.font(20.0)
        cookies.addListener { _, _, newValue ->
            cookiesLabel.text = "🍪: $newValue"
            if (hitInfinity || Double.POSITIVE_INFINITY == newValue) {
                hitInfinity = true
                cookiesLabel.text += ". Feeling depressed yet?"
            }
        }

        val factoriesLabel = Text("🏭 : ${factories.value}")
        factoriesLabel.font = Font.font(20.0)
        factories.addListener { _, _, newValue -> factoriesLabel.text = "🏭 : $newValue" }

        val buttons = Array(3) { Button() }

        buttons[0].graphic = ImageView(ResourceManager.getImage("Cookie.png"))
        buttons[0].tooltip = Tooltip("Produce ${factories.value} cookies")
        factories.addListener { _, _, newValue -> buttons[0].tooltip.text = "Produce $newValue cookies" }
        buttons[0].setOnAction { cookies.value += factories.value }

        buttons[1].graphic = ImageView(ResourceManager.getImage("Factory.png"))
        buttons[1].tooltip = Tooltip("""Trade all your cookies for ${Math.pow(cookies.value, 1.1)} more factories
Allows you to attain 100% depression faster""")
        cookies.addListener { _, _, newValue ->
            buttons[1].tooltip.text = """Trade all your cookies for ${Math.pow(newValue.toDouble(), 1.1)} more factories
Allows you to attain 100% depression faster"""
        }
        buttons[1].setOnAction {
            factories.value += Math.pow(cookies.value, 1.1)
            cookies.value = 0.0
        }

        buttons[2].graphic = ImageView(ResourceManager.getImage("Restart.png"))
        buttons[2].tooltip = Tooltip("Restart")
        buttons[2].setOnAction {
            hitInfinity = false
            cookies.value = 0.0
            factories.value = 1.0

            cookiesLabel.text = "🍪: ${cookies.value}"
            factoriesLabel.text = "🏭 : ${factories.value}"
        }

        val hbox = HBox(10.0)
        hbox.children.addAll(*buttons)

        root.children.addAll(hbox, cookiesLabel, factoriesLabel)

        root.padding = Insets(10.0, 10.0, 10.0, 10.0)

        primaryStage.scene = Scene(root)

        primaryStage.title = "Cookies!"
        primaryStage.icons += ResourceManager.getImage("Cookie.png")

        primaryStage.show()
        primaryStage.width = root.width + 50.0
        primaryStage.height = root.height + 50.0
        primaryStage.requestFocus()
    }
}