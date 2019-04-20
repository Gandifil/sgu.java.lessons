class MainScene extends Phaser.Scene {
    constructor() {
        super({ key: "MainScene" });
        this.platforms = null;
        this.player = null;
        this.cursors = null;
        this.friends = [];
        this.msg_key = null;

        this.startdata = {
            name: global.name,
            roomId: global.room,
            init: {
                x: 100,
                y: 100,
                dx: 0,
                dy: 0
            },
        };

        socket.onmessage = (event) => this.updateData(event);
        this.openConnection();
        // this.socket.onerror = function(error) {
        //     console.log("Ошибка " + error.message);
        // };
        // this.socket.onclose = (event) => console.log("close");
        this.pred_p = {
            x: this.startdata.init.x,
            y: this.startdata.init.y,
            dx: 0,
            dy: 0,
        }
    }

    preload()
    {
        this.load.image('sky', 'assets/sky.png');
        this.load.image('ground', 'assets/platform.png');
        this.load.image('star', 'assets/star.png');
        this.load.image('bomb', 'assets/bomb.png');
        this.load.spritesheet('dude',
            'assets/dude.png',
            { frameWidth: 32, frameHeight: 48 }
        );
    }

    create() {
        this.add.image(400, 300, 'sky');
        this.platforms = this.physics.add.staticGroup();
        this.platforms.create(400, 568, 'ground').setScale(2).refreshBody();
        this.platforms.create(600, 400, 'ground');
        this.platforms.create(50, 250, 'ground');
        this.platforms.create(750, 220, 'ground');

        this.player = new Actor(
            this,
            this.startdata.init.x,
            this.startdata.init.y,
            this.startdata.name
        );

        this.anims.create({
            key: 'left',
            frames: this.anims.generateFrameNumbers('dude', { start: 0, end: 3 }),
            frameRate: 10,
            repeat: -1
        });

        this.anims.create({
            key: 'turn',
            frames: [ { key: 'dude', frame: 4 } ],
            frameRate: 20
        });

        this.anims.create({
            key: 'right',
            frames: this.anims.generateFrameNumbers('dude', { start: 5, end: 8 }),
            frameRate: 10,
            repeat: -1
        });

        this.physics.add.collider(this.player, this.platforms);

        this.cursors = this.input.keyboard.createCursorKeys();

        this.physics.add.collider(this.friends, this.platforms);

        this.msg_key = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.ENTER);
        //this.scene.input.keyboard.on('keyup-' + 'ENTER', x => this.sendMessage());
    }

    update ()
    {
        if (this.cursors.left.isDown)
            this.player.body.setVelocityX(-160);
        else if (this.cursors.right.isDown)
            this.player.body.setVelocityX(160);
        else
            this.player.body.setVelocityX(0);

        if (this.cursors.up.isDown && this.player.body.touching.down)
        {
            this.player.body.setVelocityY(-330);
        }
        if (this.msg_key.isDown)
            this.sendMessage();

        if (this.pred_p.x !== this.player.x || this.pred_p.y !== this.player.y ||
            this.pred_p.dx !== this.player.body.velocity.x || this.pred_p.dy !== this.player.body.velocity.y){
            this.pred_p = {
                x: this.player.x,
                y: this.player.y,
                dx: this.player.body.velocity.x,
                dy: this.player.body.velocity.y,
            };
            socket.send(JSON.stringify(this.pred_p));
        }
        this.player.update();
        this.friends.forEach(x => x.update());
    }

    openConnection(){
        socket.send(JSON.stringify(this.startdata));
        console.log("Соединение открыто!");
    }

    updateData(event){
        let data = JSON.parse(event.data);
        let friend = this.friendByName(data.name);
        if (friend)
        {
            if (data.message == "\nclose\n")
            {
                this.friends = this.friends.filter( el => friend.getData("name") !== friend.getData("name") );
                console.log("Пользователь удален")
                friend.clear();
            }
            else
                friend.updateData(data);
        }
        else
            this.friends.push(new Actor(
                this,
                data.x,
                data.y,
                data.name
            ));
    }

    friendByName(name) {
        for (let i = 0; i < this.friends.length; i++) {
            let friend = this.friends[i];
            if (friend.getData("name") === name)
                return friend
        }
        return false
    }

    sendText(){
        let text = document.getElementById("usermsg").value;
        let temp = this.pred_p;
        temp.message = text;
        socket.send(JSON.stringify(temp));
        this.addToChat(this.startdata.name, text);
        return false;
    }

    addToChat(author, line){
        let chat = document.getElementById("chatbox").value;
        var div = document.createElement("div");
        div.className = "msgln";
        div.appendChild(document.createTextNode(a.innerHTML));
        div.innerHTML = author + " : " + line;
        chat.appendChild(div);
    }

    sendMessage(){
        let input = document.getElementById('usermsg');
        let text = input.value;
        if (text !== ""){
            input.value = "";
            let temp = this.pred_p;
            temp.message = text;
            socket.send(JSON.stringify(temp));
            this.player.showMessage(text);
        }
    }
}