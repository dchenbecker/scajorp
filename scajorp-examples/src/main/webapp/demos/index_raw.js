

var scajorp = new Scajorp("/scajorp/");

function retrieveBlogs() {
  //  document.getElementById('jsonRequest').innerHTML = 'Request POST-Body ==> ' + data;
    scajorp.blogEntryService.getAll(parseBlogEntries);  
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