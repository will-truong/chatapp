import vertx
import urllib
import urllib2
import re
from core.event_bus import EventBus

def msg_handler(message):
  txt = urllib2.urlopen("http://www.accuweather.com/en/us/houston-tx/77002/weather-forecast/351197").read()
  cond = re.search("<span class=\"cond\">(.+?)</span>",txt).group(1)
  temp = re.search("<strong class=\"temp\">(.+?)<span>",txt).group(1)
  message.reply(cond + "," + temp)

id = EventBus.register_handler('request.weather', handler=msg_handler)