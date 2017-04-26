package web

import business.LeCompteEstBon
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Spark.*

/**
 * Front controller for the API.
 *
 * @author Guillaume Chanson
 * @version 1.0
 */
fun main(args: Array<String>) {
    port(4613)

    get("/hey") { req, res -> "Hello World" }

    path("/game") {
        get("/new") { req, res ->
            jacksonObjectMapper().writeValueAsString(LeCompteEstBon.create())
        }
    }
}