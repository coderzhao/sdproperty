
function CameraViewer (url ,mac, img){
	var socket,viewer={};
	var count = 0;
	function connect () {
	    socket = new WebSocket(url);
	    socket.binaryType = "arraybuffer";
	    socket.onopen = onopen;
	    socket.onclose = onclose;
	    socket.onmessage = receiveMessage;
	};
	//StartLiveStream,StopLiveStram
	function disconnect(){
		var command = {"mac":mac,"cmd":"StopLiveStream"};
		socket.send(JSON.stringify(command));
		socket.close();
	}
	
	
	function onopen(){
		var command = {"mac":mac,"cmd":"StartLiveStream"};
		socket.send(JSON.stringify(command));
	}
	
	function onclose(){
		
	}
	
	function receiveMessage(event){
		img.src = "" + "data:image/jpg;base64," + event.data;
		count++;
		console.log('rec from ' + mac + ", ttl=" + count);
	}
	
	viewer.start = function(){
		connect();
	}
	
	viewer.stop = function(){
		disconnect();
	}
	
	viewer.mac = mac;
	
	return viewer;
}