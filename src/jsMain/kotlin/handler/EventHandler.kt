package handler

import FileCompanion.Companion.isValid
import kotlinx.browser.window
import kotlinx.css.Color
import kotlinx.css.Float
import kotlinx.css.color
import kotlinx.css.float
import react.*
import react.dom.*
import kotlinx.html.js.*
import kotlinx.html.InputType
import org.w3c.dom.events.Event
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get
import styled.css
import styled.styledButton
import styled.styledInput

external interface InputProps : RProps {
    var onSubmit: (File) -> Unit
    var generateFile: () -> Unit
    var downloadFile: () -> Unit
}

val InputComponent = functionalComponent<InputProps> { props ->
    val initValue = File(emptyArray(), "dummyFile.txt")
    val (file, setFile) = useState(initValue)

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        if (isValid(file.type)) {
            setFile(initValue)
            props.onSubmit(file)
        } else {
            window.alert("Not a valid file.")
        }
    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement)
        value.files!![0]?.let { f -> setFile(f) }
    }

    styledButton {
        css {
            float = Float.right
        }
        attrs {
            name = "download-file"
            value = "download-file"
            onClickFunction = {
                it.preventDefault()
                props.downloadFile()
            }
        }
        +"Download File"
    }

    styledButton {
        css {
            float = Float.right
        }
        attrs {
            name = "generate-file"
            value = "generate-file"
            onClickFunction = {
                it.preventDefault()
                props.generateFile()
            }
        }
        +"Generate File"
    }

    form {
        attrs.onSubmitFunction = submitHandler
        styledInput(InputType.file) {
            css.color = Color.whiteSmoke
            attrs.onChangeFunction = changeHandler
        }
        input(InputType.submit) {}
    }
}
