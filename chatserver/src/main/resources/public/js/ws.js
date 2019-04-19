let server = "ws://openjdk-app-chatserver1.7e14.starter-us-west-2.openshiftapps.com:80/chat";
socket = new WebSocket(server);
socket.onopen = () => {};
socket.onmessage = function(event){
    let div = document.getElementById('rooms1');
    if (event.data === '[]')
        div.innerHTML = 'доступных комнат нет';
    else
        div.innerHTML = event.data;
};

socket.onerror = function(error) {
    alert("Ошибка " + error.message+"\n Дальнейшее выполнение програмы не гарантируется.");
};
socket.onclose = (event) => console.log("close");