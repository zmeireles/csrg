var starterUserId = 0;

function startChat(userId,destinationUserId){
	starterUserId = userId;
	Seam.Component.getInstance("chatAction",true).startChat(destinationUserId,startChatCallback);
	
}
function startChatCallback(result){
	if(result!=""){
		//alert(result);
		var args = result.split(";");
		window.open('http://' + args[0] + '?chatParams=' + args[1],'chat_' + args[2],'width=350,height=350');
	}
}
function onLoadProcedure(){
	dwr.engine.setActiveReverseAjax(true);
	dwrAction.setScriptSession(null);
}
function onUnloadProcedure(){
	//alert("unload");
	dwrAction.unloadScriptSession(null);
}
function updateOnlineSupportImg(imgString){
	var img = document.getElementById("onlineSupportImg");
	if(img!=null){
		img.setAttribute("src", imgString);
	}
}
function requestOnlineSupport(){
	Seam.Component.getInstance("chatAction").requestOnlineSupport(requestOnlineSupportCallback);
}
function requestOnlineSupportCallback(result){
	//alert("callback");
	if(result!=""){
		//alert("online request success");
		var args = result.split(";");
		window.open('http://' + args[0] + '?chatParams=' + args[1],'support_' + args[2],'width=350,height=350');
	}
}