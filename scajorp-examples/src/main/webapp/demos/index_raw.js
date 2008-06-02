var xhr = window.XMLHttpRequest ? new XMLHttpRequest(): new ActiveXObject("Msxml2.XMLHTTP");

function retrieveBlogs() {
    
    var params = '{\"jsonrpc\": \"2.0\", \"method\": \"blogEntryService.getAll\", \"params\": [], \"id\": 1}';
    xhr.open('POST', '/scajorp/', true);
    xhr.setRequestHeader("Content-Type", "application/json-rpc");
    xhr.setRequestHeader("Content-length", params.length);
    xhr.onreadystatechange = parseBlogEntries
    document.getElementById('jsonRequest').innerHTML = 'Request POST-Body ==> ' + params;
    xhr.send(params)

}

function parseBlogEntries( ) {
    if (xhr.readyState == 4) {
        if (xhr.status == 200) {
            var jsonString = xhr.responseText;
            document.getElementById('jsonResponse').innerHTML = 'Response String ==> ' + jsonString ;
            var response = eval('(' + jsonString + ')');
            var blogEntries = response.result;
            var out = '<table><tr><th>Title</th><th>Author</th></tr>';            
            for (i = 0; i < blogEntries.length; i++) {                
                var blog = blogEntries[i];
                out += '<tr>';
                out += '<td>'+ blog.title +'</td><td>' + blog.author + '</td>';
                out += '</tr>';
            }
            out += '</table>';
            document.getElementById('blogentries').innerHTML = out;
        } else
            alert('There was a problem retrieving the data: \n' + xhr.statusText);       
    }
}