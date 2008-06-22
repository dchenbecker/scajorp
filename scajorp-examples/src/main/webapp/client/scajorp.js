/*
JSONstring v 1.01
copyright 2006 Thomas Frank
(small sanitizer added to the toObject-method, May 2008)

This EULA grants you the following rights:

Installation and Use. You may install and use an unlimited number of copies of the SOFTWARE PRODUCT.

Reproduction and Distribution. You may reproduce and distribute an unlimited number of copies of the SOFTWARE PRODUCT either in whole or in part; each copy should include all copyright and trademark notices, and shall be accompanied by a copy of this EULA. Copies of the SOFTWARE PRODUCT may be distributed as a standalone product or included with your own product.

Commercial Use. You may sell for profit and freely distribute scripts and/or compiled scripts that were created with the SOFTWARE PRODUCT.

Based on Steve Yen's implementation:
http://trimpath.com/project/wiki/JsonLibrary

Sanitizer regExp:
Andrea Giammarchi 2007

*/

JSONstring={
	compactOutput:false,
	includeProtos:false,
	includeFunctions: false,
	detectCirculars:true,
	restoreCirculars:true,
	make:function(arg,restore) {
		this.restore=restore;
		this.mem=[];this.pathMem=[];
		return this.toJsonStringArray(arg).join('');
	},
	toObject:function(x){
		if(!this.cleaner){
			try{this.cleaner=new RegExp('^("(\\\\.|[^"\\\\\\n\\r])*?"|[,:{}\\[\\]0-9.\\-+Eaeflnr-u \\n\\r\\t])+?$')}
			catch(a){this.cleaner=/^(true|false|null|\[.*\]|\{.*\}|".*"|\d+|\d+\.\d+)$/}
		};
		if(!this.cleaner.test(x)){return {}};
		eval("this.myObj="+x);
		if(!this.restoreCirculars || !alert){return this.myObj};
		if(this.includeFunctions){
			var x=this.myObj;
			for(var i in x){if(typeof x[i]=="string" && !x[i].indexOf("JSONincludedFunc:")){
				x[i]=x[i].substring(17);
				eval("x[i]="+x[i])
			}}
		};
		this.restoreCode=[];
		this.make(this.myObj,true);
		var r=this.restoreCode.join(";")+";";
		eval('r=r.replace(/\\W([0-9]{1,})(\\W)/g,"[$1]$2").replace(/\\.\\;/g,";")');
		eval(r);
		return this.myObj
	},
	toJsonStringArray:function(arg, out) {
		if(!out){this.path=[]};
		out = out || [];
		var u; // undefined
		switch (typeof arg) {
		case 'object':
			this.lastObj=arg;
			if(this.detectCirculars){
				var m=this.mem; var n=this.pathMem;
				for(var i=0;i<m.length;i++){
					if(arg===m[i]){
						out.push('"JSONcircRef:'+n[i]+'"');return out
					}
				};
				m.push(arg); n.push(this.path.join("."));
			};
			if (arg) {
				if (arg.constructor == Array) {
					out.push('[');
					for (var i = 0; i < arg.length; ++i) {
						this.path.push(i);
						if (i > 0)
							out.push(',\n');
						this.toJsonStringArray(arg[i], out);
						this.path.pop();
					}
					out.push(']');
					return out;
				} else if (typeof arg.toString != 'undefined') {
					out.push('{');
					var first = true;
					for (var i in arg) {
						if(!this.includeProtos && arg[i]===arg.constructor.prototype[i]){continue};
						this.path.push(i);
						var curr = out.length;
						if (!first)
							out.push(this.compactOutput?',':',\n');
						this.toJsonStringArray(i, out);
						out.push(':');
						this.toJsonStringArray(arg[i], out);
						if (out[out.length - 1] == u)
							out.splice(curr, out.length - curr);
						else
							first = false;
						this.path.pop();
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
			if(!this.includeFunctions){out.push(u);return out};
			arg="JSONincludedFunc:"+arg;
			out.push('"');
			var a=['\n','\\n','\r','\\r','"','\\"'];
			arg+=""; for(var i=0;i<6;i+=2){arg=arg.split(a[i]).join(a[i+1])};
			out.push(arg);
			out.push('"');
			return out;
		case 'string':
			if(this.restore && arg.indexOf("JSONcircRef:")==0){
				this.restoreCode.push('this.myObj.'+this.path.join(".")+"="+arg.split("JSONcircRef:").join("this.myObj."));
			};
			out.push('"');
			var a=['\n','\\n','\r','\\r','"','\\"'];
			arg+=""; for(var i=0;i<6;i+=2){arg=arg.split(a[i]).join(a[i+1])};
			out.push(arg);
			out.push('"');
			return out;
		default:
			out.push(String(arg));
			return out;
		}
	}
};


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

   
Scajorp.prototype.post = function(request, callback)  {


        if (typeof request != 'string') {
            request.id = this.requestId++;
            request.jsonrpc = "2.0";
            request = JSONstring.make(request);
        }

        var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        xhr.open('POST', this.url, true);
        xhr.setRequestHeader("Content-Type", "application/json-rpc");
        xhr.setRequestHeader("Content-length", request.length);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var jsonString = xhr.responseText;
                callback(eval('(' + jsonString + ')'), jsonString);
            }
        };
        xhr.send(request);
}


Scajorp.prototype.createMethod = function(name) {
       var fn = function() {            
            var parameters = [];

            var args = Array.prototype.slice.call(arguments);  

            var callback = (typeof args[0] == 'function') ? args.shift(): null;
            for (var i in arguments) {
                if (typeof arguments[i] !== 'function') parameters.push(args[i]);
            }

           var request = {};           
           request.method = fn.methodName;
           request.params = parameters;
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











