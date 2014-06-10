var vertx = require('vertx')
var console = require('vertx/console')
var container = require('vertx/container')
var logger = container.logger;
var config = container.config;

logger.info(JSON.stringify(config));

container.deployVerticle("accuweather.py");
//container.deployVerticle("com.pason.chatapp.MotD", config.MotDConfig);
container.deployVerticle("com.pason.chatapp.MessageFilterVerticle",config.MessageFilterVerticleConfig);
container.deployVerticle("com.pason.chatapp.WebserverVerticle", config.WebserverVerticleConfig);