package workshop

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DbPlugin : Plugin<Project> {
    override fun apply(target: Project)/*: Unit = with(target) */{
//        val url = "jdbc:postgresql://db:5432/demo"
//        val user = "postgres"
//        val password = "postgres"

//        val connection = connectToDatabase()
//        connection?.apply { executeQuery(this) }
        target.tasks.register<DbTask>("select"){

        }


    }

    fun connectToDatabase(): Connection? {
        val url = "jdbc:postgresql://db:5432/demo"
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
            println(resultSet.getString("ticket_no"))
        }
    }
}