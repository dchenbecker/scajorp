/*
Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

toJsonString = function(arg) {
    return toJsonStringArray(arg).join('');
}

toJsonStringArray = function(arg, out) {
    out = out || new Array();
    var u; // undefined

    switch (typeof arg) {
    case 'object':
        if (arg) {
            if (arg.constructor == Array) {
                out.push('[');
                for (var i = 0; i < arg.length; ++i) {
                    if (i > 0)
                        out.push(',\n');
                    toJsonStringArray(arg[i], out);
                }
                out.push(']');
                return out;
            } else if (typeof arg.toString != 'undefined') {
                out.push('{');
                var first = true;
                for (var i in arg) {
                    var curr = out.length; // Record position to allow undo when arg[i] is undefined.
                    if (!first)
                        out.push(',\n');
                    toJsonStringArray(i, out);
                    out.push(':');
                    toJsonStringArray(arg[i], out);
                    if (out[out.length - 1] == u)
                        out.splice(curr, out.length - curr);
                    else
                        first = false;
                }
                out.push('}');
                return out;
            }
            return out;
        }
        out.push('null');
        return out;
    case 'unknown':
    case 'undefined':
    case 'function':
        out.push(u);
        return out;
    case 'string':
        out.push('"')
        out.push(arg.replace(/(["\\])/g, '\\$1').replace(/\r/g, '').replace(/\n/g, '\\n'));
        out.push('"');
        return out;
    default:
        out.push(String(arg));
        return out;
    }
}


if (Function.prototype.bind == null) {
    Function.prototype.bind = function(object) {
        var method = this;
        return function() {
            return method.apply(object, arguments);
        };
    }
}


function Scajorp(url) {
    this.url = url;
    this.requestId = 1;
    this.init();
}


Scajorp.prototype.init = function() {

    var data = {
        method: 'system.listMethods',
        params: []
    }
    this.post(data, this.addMethods.bind(this));
}


Scajorp.prototype.post = function(request, callback) {

    if (typeof request != 'string') {
        request.id = this.requestId++;
        request.jsonrpc = "2.0";
        request = toJsonString(request);
    }

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
    xhr.open('POST', this.url, true);
    xhr.setRequestHeader("Content-Type", "application/json-rpc");
    xhr.setRequestHeader("Content-length", request.length);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var jsonString = xhr.responseText;
            if (callback) callback(eval('(' + jsonString + ')'), jsonString);
        }
    };
    xhr.send(request);
}


Scajorp.prototype.createMethod = function(name) {
    
    var fn = function() {

        var parameters = Array.prototype.slice.call(arguments);

        var callback = (typeof parameters[0] == 'function') ? parameters.shift() : null;
        
        var request = {'method': fn.methodName, 'params': parameters};
        
        fn.client.post(request, callback);

    };
    fn.client = this;
    fn.methodName = name;
    return fn;

}

Scajorp.prototype.addMethods = function(jsonResponse) {

    var methods = jsonResponse.result;

    for (var i in methods) {
        var name = methods[i].split(".");
        var objectName = name[0];
        var methodName = name[1];

        if (!this[objectName]) this[objectName] = {};

        this[objectName][methodName] = this.createMethod(objectName + "." + methodName);
    }


}











