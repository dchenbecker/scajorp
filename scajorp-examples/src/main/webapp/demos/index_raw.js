

function retrieveBlogs() {
    var data = '{\"jsonrpc\": \"2.0\", \"method\": \"blogEntryService.getAll\", \"params\": [], \"id\": 1}';
    document.getElementById('jsonRequest').innerHTML = 'Request POST-Body ==> ' + data;
    var scajorp = new Scajorp("/scajorp/");
    scajorp.post(data, parseBlogEntries);

}

function parseBlogEntries(jsonObj, jsonString) {
    document.getElementById('jsonResponse').innerHTML = 'Response String ==> ' + jsonString;
    var blogEntries = jsonObj.result;
    var out = '<table><tr><th>Title</th><th>Author</th></tr>';
    for (i = 0; i < blogEntries.length; i++) {
        var blog = blogEntries[i];
        out += '<tr>';
        out += '<td>' + blog.title + '</td><td>' + blog.author + '</td>';
        out += '</tr>';
    }
    out += '</table>';
    document.getElementById('blogentries').innerHTML = out;

}