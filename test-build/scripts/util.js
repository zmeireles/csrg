var loaded = false;
/*
try{
	if(self.parent.frames.length == 0){
		//alert("in");
		url = location.href
		s1 = url.substring(url.indexOf("//")+2);
		s2 = s1.substring(s1.indexOf("/")+1);
		s3 = "/"+s2.substring(0,s2.indexOf("/"));
		location=s3+"/index.seam";
	}
}catch(Exception){
	location='/csrg/index.seam';
} 
*/
function setLoaded(){
	loaded = true;
}

function setLoginFocus(){
	if(document.getElementById("login:username")){
		if(document.getElementById("login:username").value == "")
			document.getElementById("login:username").focus();
		else
			document.getElementById("login:password").focus();
	}
}

function hideId(id){
	if(loaded) document.getElementById(id).style.display = 'none';
}

function showId(id){
	if(loaded) document.getElementById(id).style.display = '';
}