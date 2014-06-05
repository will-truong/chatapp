var vertx = require('vertx')
var console = require('vertx/console')
var container = require('vertx/container')

container.deployVerticle("com.pason.chatapp.WebserverVerticle");
container.deployVerticle("com.pason.chatapp.MotD");
