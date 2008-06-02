function test() {
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest(): new ActiveXObject("Msxml2.XMLHTTP");
    //this.onStateChange /

    var params = '{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}';
    xhr.open('POST', 'scajorp/', true);
    xhr.setRequestHeader("Content-Type", "application/json-rpc");
    xhr.setRequestHeader("Content-length", params.length);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            alert(xhr.responseText);
        }


    }
     xhr.send(params)

}





