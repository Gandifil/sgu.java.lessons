let global = {
    name: '',
    room: 0,
};

function startGame(){
    //проверка данных
    let login_input = document.getElementById('userlogin');
    let room_input = document.getElementById('userroom');

    global.name = login_input.value;
    global.room = parseInt(room_input.value);

    if (global.name === '') {
        alert('введите имя пользователя');
        return;
    }
    if (isNaN(global.room)){
        alert('введите правилько номер комнаты (разрешается в формате "12пываыв", что равно 12)');
        return;
    }

    // процесс старта игры
    //document.open();
    config = {
        type: Phaser.AUTO,
        width: 800,
        height: 600,
        physics: {
            default: 'arcade',
            arcade: {
                gravity: { y: 100 }
            }
        },
        scene: [MainScene]
    };

    var game = new Phaser.Game(config);

    document.getElementById('wrapper').innerHTML =`
    <h2>Движение - стрелочки. Отправка сообщения - enter. </h2>
    <div id="chatbox"></div>

    <input name="usermsg" type="text" id="usermsg" size="63" />`;
    document.getElementById('usermsg').focus();
}