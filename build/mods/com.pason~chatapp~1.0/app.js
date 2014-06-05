var vertx = require('vertx')
var console = require('vertx/console')
var container = require('vertx/container')

container.deployVerticle("accuweather.py");
container.deployVerticle("com.pason.chatapp.WebserverVerticle");
container.deployVerticle("com.pason.chatapp.MotD");
