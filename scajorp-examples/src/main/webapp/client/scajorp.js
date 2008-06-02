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
    var xhr = this.xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");

    this.post = function(data, callback) {
        xhr.open('POST', url, true);
        xhr.setRequestHeader("Content-Type", "application/json-rpc");
        xhr.setRequestHeader("Content-length", data.length);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var jsonString = xhr.responseText;
                callback(eval('(' + jsonString + ')'), jsonString);
            }
        };
        xhr.send(data);
    };

    this.init = function() {
        var data = '{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}';
        this.post(data, poop);
    }
    this.init();

}

function poop() {
    alert('muh');
}