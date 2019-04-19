let server = "ws://localhost:1234/chat";
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