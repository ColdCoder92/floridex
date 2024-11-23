package com.example.floridex

import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.DeepTeal80
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Green80
import com.example.floridex.ui.theme.Orange40
import com.example.floridex.ui.theme.Orange80
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.google.gson.Gson
import org.json.JSONObject

data class User(
    val username: String,
    val email: String,
    val password: String
)

class Settings {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://z41sqpegib.execute-api.us-east-1.amazonaws.com/userList"
    //var isPressed = remember { mutableStateOf(false) }
    // using the above to plug Settings with Profile Page

    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun MakeSettingsMenu(modifier: Modifier, context: Context) {
        requestQueue = Volley.newRequestQueue(context)
        textView = TextView(context)

        val hasResponse = remember { mutableStateOf(false) }
        var responseInfo = remember { mutableStateOf(JSONObject()) }

        val stringRequest = StringRequest(
            Request.Method.GET,
            ("$gatewayLINK?key1=value1&key2=value2&key3=value3"),
            { response ->
                textView.text = response
                responseInfo.value = JSONObject(response)
                hasResponse.value = true
            },
            { error ->
                textView.text = when (error) {
                    is TimeoutError -> "Request timed out"
                    is NoConnectionError -> "No internet connection"
                    is AuthFailureError -> "Authentication error"
                    is ServerError -> "Server error"
                    is NetworkError -> "Network error"
                    is ParseError -> "Data parsing error"
                    else -> "Error: ${error.message}"
                }
            }
        )

        requestQueue.add(stringRequest)
        if (hasResponse.value) {
            val gson = Gson()
            val rowValue = responseInfo.value.get("users")

            val users: List<User> = gson.fromJson(
                rowValue.toString(), Array<User>::class.java
            ).toList()


            //println("Response: " + responseInfo)

            println("Response: " + rowValue)
            //println("Response: ${creatures[0].image.type}")

            SettingsMenu()
        }

        //SettingsMenu()
    }

    @Preview
    @Composable
    fun SettingsMenu() {
        var color = DeepTeal40
        if (isSystemInDarkTheme())
        {
            color = DeepTeal80
        }

        Box (modifier = Modifier.fillMaxSize().background(color = color))

        Box (modifier = Modifier.width(425.dp).heightIn(75.dp).background(Green40))
        {
            Text("Settings", modifier = Modifier.offset(167.5.dp, 30.dp), textAlign = TextAlign.Center)
        }

        Box (modifier = Modifier.offset(0.dp, 100.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Username", modifier = Modifier.offset(5.dp, 17.5.dp))

            Image(painter = painterResource(R.drawable.edit_icon), contentDescription = null,
                modifier = Modifier.offset(280.dp, 6.5.dp), colorFilter = ColorFilter.tint(Color.Black))

            Text("Username", modifier = Modifier.offset(320.dp, 17.5.dp), textAlign = TextAlign.End)
            // ^ Trying to get this aligned to the right
        }

        Box (modifier = Modifier.offset(0.dp, 151.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Reset Password", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

        Box (modifier = Modifier.offset(0.dp, 202.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Contact Support", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

        Box (modifier = Modifier.offset(0.dp, 253.dp).width(425.dp).heightIn(50.dp).background(Color(0xffff4444)))
        {
            Text("Delete Account", modifier = Modifier.offset(5.dp, 17.5.dp))
            // want this box in a lighter red
        }

        Box (modifier = Modifier.offset(0.dp, 304.dp).width(425.dp).heightIn(50.dp).background(Color(0xffff4444)))
        {
            Text("Sign Out", modifier = Modifier.offset(5.dp, 17.5.dp))
            // want this box in a lighter red
        }

        fun onBackPressed() {
            // Perform your custom action here


            // Call the default behavior if needed
            super.onBackPressed()
        }


    }
    /*
    @Composable
    fun SettingsButtons(onClick: () -> Unit) {

        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(0.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Info")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(137.5f.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Cry")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(275.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Map")
        }
    }

    @Composable
    fun SettingsArea()
    {

    }

    */

    // Logger for monitoring performance and errors
    val logger: Logger = Logger.getLogger("MySQLConnector")

    fun connectToMySQL(): Connection? {
        val url = "jdbc:mysql://localhost:3306/mydatabase" // Replace with your MySQL database URL
        val username = "root" // Replace with your MySQL username
        val password = "password" // Replace with your MySQL password

        var connection: Connection? = null

        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver")

            // Establish the connection
            connection = DriverManager.getConnection(url, username, password)
            logger.log(Level.INFO, "Connected to MySQL database")

        } catch (e: ClassNotFoundException) {
            logger.log(Level.SEVERE, "MySQL JDBC driver not found", e)
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to connect to MySQL database", e)
        }

        return connection
    }

    fun executeQuery(connection: Connection?, query: String): ResultSet? {
        var resultSet: ResultSet? = null

        try {
            val statement: Statement? = connection?.createStatement()

            // Execute the query
            resultSet = statement?.executeQuery(query)

        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to execute query", e)
        }

        return resultSet
    }

    fun main() {
        val connection = connectToMySQL()

        if (connection != null) {
            val query = "SELECT * FROM mytable" // Replace with your SQL query

            val resultSet = executeQuery(connection, query)

            if (resultSet != null) {
                // Process the result set
                while (resultSet.next()) {
                    val id = resultSet.getInt("id")
                    val name = resultSet.getString("name")
                    val age = resultSet.getInt("age")

                    println("ID: $id, Name: $name, Age: $age")
                }
            } else {
                println("Failed to execute the query.")
            }

            // Close the connection
            try {
                connection.close()
                logger.log(Level.INFO, "Connection closed")
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Failed to close the connection", e)
            }
        } else {
            println("Failed to connect to the MySQL database.")
        }
    }

    // Took some code from Description.kt (I believe was Lucas' work)
}

