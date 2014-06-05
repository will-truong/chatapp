import vertx
import urllib
import urllib2
import re
from core.event_bus import EventBus

def msg_handler(message):
  txt = urllib2.urlopen("http://www.accuweather.com/en/us/houston-tx/77002/weather-forecast/351197").read()
  result = re.search("<span class=\"cond\">(.+?)</span>",txt)
  result2 = re.search("<strong class=\"temp\">(.+?)<span>",txt)
  message.reply(result.group(1) + "," + result2.group(1))

id = EventBus.register_handler('request.weather', handler=msg_handler)