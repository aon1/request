package utils

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat

object Utils {
	def saveToFile(filename: String, text: String) {

        val file = new PrintWriter(new File(filename))
        file.write(text)
        file.close
    }

    def getDate(): String = {
        
        val dateFormatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm")
        val now = Calendar.getInstance().getTime()

        dateFormatter.format(now)
    }
}