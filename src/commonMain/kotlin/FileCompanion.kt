class FileCompanion {
    companion object {
        private const val path = "/api/file"
        const val uploadPath = "$path/upload"
        const val downloadPath = "$path/download"
        const val generatePath = "$path/generate"
        private const val validFileType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

        fun isValid(fileType: String): Boolean {
            return validFileType == fileType
        }
    }
}
