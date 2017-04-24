/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mavenproject1;

import static spark.Spark.*;
import static test.mavenproject1.JsonUtil.json;

public class HelloWorld {

    public static void main(String[] args) {
        UserService us = new UserService();

        us.createUser("Clark", "cKent@planet.new");
        us.createUser("Bruce", "nana@wayne.go");

        get("hello/:name", (req, res) -> {
            return "Hello: " + req.params(":name");
        });

        get("/hello", (req, res) -> "Hello World");

        get("/users", (req, res) -> us.getAllUsers(), json());

        get("/users/:id", (req, res) -> {
            String id = req.params(":id");
            return us.getUser(id);
        }, json());

        post("/users", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");
            return us.createUser(name, email);
        }, json());

        put("/users", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");
            return us.createUser(name, email);
        }, json());

        after((req, res) -> {
            res.type("application/json");
        });

    }
}
