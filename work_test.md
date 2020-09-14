
This is a web app for managing smart light bulbs in your home network, that offers the following functionality:
* You can list devices in your network
* You can connect to a device to get its state (brightness, color, name, etc)
* You can change the name, brightness and color of the device you're connected to
* You need to disconnect from a device before you can connect to another device
* Changes to a device are persistent over connect/disconnect events, but reset if you restart the server.

The app offers the following JSON API calls for this functionality:

* List all available devices: GET `/devices`:
    - Returns a list of JSON objects, with `name` (the name of the device) and `ip` (the ip address)
    - Example call: `curl localhost:8080/devices`
    - Example response: `[{"name": "bulb1", "ip": "192.168.100.10"}, {"name": "bulb2", "ip": "192.168.100.11"}]`
* Connect to a device: POST `/connect`:
    - Expects a parameter `ip` (string) - the ip address of the device
    - Returns success boolean
    - Example call: `curl localhost:8080/connect -X POST -d '{"ip":"192.168.100.10"}' -H "Content-type:application/json"`
    - Example response: `{"success": true}`
* Get the state of a device: GET `/state`:
    - Requires that you are connected to a device.
    - Returns a JSON object with all information about that device:
`name`, `ip`, `color` as hex code, e.g. `#ffffff` for white, `brightness` as a float value between 0 and 10
    - Example call: `curl localhost:8080/state`
    - Example response: `{"name": "bulb1", "ip": "192.168.100.10", "color": "#ffffff", "brightness": 10.0}`
    - Example response when not connected to a device: `{"success": false}`
* Set the brightness of a device: POST `/brightness`:
    - Requires that you are connected to a device.
    - Expects a numeric parameter `brightness` within the range `0-10`
    - Example call: `curl localhost:8080/brightness -X POST -d '{"brightness":4}' -H "Content-type:application/json"`
    - Example response: `{"success": true}`
    - Example response when not connected to a device: `{"success": false}`
* Set the color of a device: POST `/color`:
    - Requires that you are connected to a device.
    - Expects a parameter `color` as hex code. e.g. `#00ff00` for red
    - Example call: `curl localhost:8080/color -X POST -d '{"color":"#336699"}' -H "Content-type:application/json"`
    - Example response: `{"success": true}`
    - Example response when not connected to a device: `{"success": false}`
* Set the name of a device: POST `/name`:
    - Requires that you are connected to a device.
    - Expects a parameter `name`
    - Example call: `curl localhost:8080/name -X POST -d '{"name":"foobar"}' -H "Content-type:application/json`
    - Example response: `{"success": true}`
    - Example response when not connected to a device: `{"success": false}`
* Disconnect from any device: POST `/disconnect`:
    - Example call: `curl -X POST localhost:8080/disconnect`
    - Example response: `{"success": true}`

Parameters are accepted as `application/x-www-form-urlencoded`, or as `application/json` data, decide on one format at your discretion.
example curl calls:
`curl localhost:8080/connect -X POST -d '{"ip":"192.168.100.10"}' -H "Content-type:application/json"`
`curl localhost:8080/connect -X POST -d "ip=192.168.100.10" -H "Content-type:application/x-www-form-urlencoded"`

Responses the app sends are either json data (for e.g. `list_devices`) or a simple JSON object that indicates success or failure: `{"success": true}`  or ` {"success": false}`
Any exceptions (http status 500) should be considered bugs.

The app can be started with `python3 app.py` - it has no dependencies and should work with python 3.5, 3.6, 3.7.

Your task is to
* write API Tests: Create a script in any language and framework of your preference that tests the correct behaviour of all API calls by calling them.
* list any bugs you find.
* discuss any other testing techniques that you would apply to this kind of app.
