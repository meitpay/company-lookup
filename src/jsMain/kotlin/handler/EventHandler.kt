package handler

import FileCompanion.Companion.isValid
import kotlinx.browser.window
import kotlinx.css.*
import react.*
import kotlinx.html.js.*
import kotlinx.html.InputType
import org.w3c.dom.events.Event
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get
import styled.*

external interface InputProps : RProps {
    var onUploadFile: (File) -> Unit
    var onGenerateFile: () -> Unit
    var onDownloadFile: () -> Unit
}

val InputComponent = functionalComponent<InputProps> { props ->
    val initValue = File(emptyArray(), "dummyFile.txt")
    val (file, setFile) = useState(initValue)

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        if (isValid(file.type)) {
            setFile(initValue)
            props.onUploadFile(file)
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
            cursor = Cursor.pointer
        }
        attrs {
            name = "download-file"
            value = "download-file"
            onClickFunction = {
                it.preventDefault()
                props.onDownloadFile()
            }
        }
        +"Download File"
    }

    styledButton {
        css {
            float = Float.right
            cursor = Cursor.pointer
        }
        attrs {
            name = "generate-file"
            value = "generate-file"
            onClickFunction = {
                it.preventDefault()
                props.onGenerateFile()
            }
        }
        +"Generate File"
    }

    styledForm {
        attrs.onSubmitFunction = submitHandler

        styledInput(InputType.file) {
            css {
                cursor = Cursor.pointer
                color = Color.whiteSmoke
            }
            attrs.onChangeFunction = changeHandler
        }
        styledInput(InputType.submit) {
            css {
                cursor = Cursor.pointer
            }
        }
    }
}
