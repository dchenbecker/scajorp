function ScajorpRPC(url) {

    this.url = url

    this.xhr = document.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");

    this.systemList = function() {
        
    }
    
    //this.onStateChange /

    xhr.open('GET', 'servlets/ajax/getItem?id=321', true);
    req.setRequestHeader("Content-Type","application/json-rpc");
    xhr.onreadystatechange = parseResponse;
    xhr.send('')
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
