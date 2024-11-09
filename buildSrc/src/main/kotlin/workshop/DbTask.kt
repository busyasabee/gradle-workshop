package workshop

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

abstract class DbTask : DefaultTask() {
    @TaskAction
    fun select() {
        val connection = connectToDatabase()
        try {
            connection?.apply { executeQuery(this) }
        } finally {
            connection?.close()
        }
    }

    fun connectToDatabase(): Connection? {
        val url = "jdbc:postgresql://localhost:5433/demo"
        val user = "postgres"
        val password = "postgres"
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(url, user, password)
            println("Connected to the database successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return connection
    }

    fun executeQuery(connection: Connection) {
        val statement: Statement = connection.createStatement()
        val resultSet = statement.executeQuery("select * from bookings.tickets")
        while (resultSet.next()) {
            println("ticket_no = " + resultSet.getString("ticket_no"))
        }
    }
}