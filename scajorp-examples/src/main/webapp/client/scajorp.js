function ScajorpRPC(url) {

    this.url = url

    this.xhr = document.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");

    this.systemList = function() {

    }

    //this.onStateChange /
    var params = '{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}'
    xhr.open('POST', 'scajorp', true);
    req.setRequestHeader("Content-Type","application/json-rpc");
    xhr.setRequestHeader("Content-length", params.length);
    xhr.onreadystatechange = function() {
	if(http.readyState == 4 && http.status == 200) {
		alert(http.responseText);
	}

    xhr.send(params)
}

function sendRequest(url,params,HttpMethod){
  if (!HttpMethod){
    HttpMethod="POST";
  }
  var req=getXMLHTTPRequest();
  if (req){
    req.open(HttpMethod,url,true);

    req.send(params);
  }
}
