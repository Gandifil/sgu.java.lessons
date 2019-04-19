
function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

class Actor extends Phaser.GameObjects.Sprite {
    constructor(scene, x, y, name) {
        super(scene, x, y, "dude");

        //this.scene = scene;
        scene.add.existing(this);
        scene.physics.world.enableBody(this, 0);
        this.body.setBounce(0.1);
        this.body.setCollideWorldBounds(true);
        this.body.setGravityY(30);

        // подпись
        this.v_text = scene.add.existing(new Phaser.GameObjects.Text(scene, x, y,
            name, { font: "12px Verdana", fill: getRandomColor() }));
        this.v_text.setOrigin(0.5, - 1.2);
        this.setData("name", name);
    }

    update(){
        this.v_text.x = this.x;
        this.v_text.y = this.y;

        if (this.body.velocity.x < 0)
            this.anims.play('left', true);
        else if (this.body.velocity.x > 0)
            this.anims.play('right', true);
        else
            this.anims.play('turn');
    }

    updateData(data){
        this.setPosition(data.x, data.y);
        this.body.setVelocityX(data.dx);
        this.body.setVelocityY(data.dy);

        console.log("Положения обновлены!");

        this.showMessage(data.message);
    }

    showMessage(message){
        if (message !== null && message !== ""){
            let div = document.getElementById('chatbox');
            div.innerHTML += `<div class='msgln'><b>${this.getData("name")}:</b> ${message}<br></div>`;
        }

    }
}