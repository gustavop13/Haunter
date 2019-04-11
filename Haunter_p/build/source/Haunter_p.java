import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import java.util.Map; 
import java.util.Collections; 
import org.gamecontrolplus.gui.*; 
import org.gamecontrolplus.*; 
import net.java.games.input.*; 
import processing.video.*; 
import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Haunter_p extends PApplet {

// Gustavo Placencia Carranza

// Arduino

Serial port;
String val = "N/A";
boolean first = true;

// Java



// Gamepad




ControlIO control;
Configuration config;
ControlDevice gpad;

// Video

Movie dancin;

// Sound

SoundFile song1;
SoundFile song2;
SoundFile song3;
SoundFile song4;

// Game info
Character doug;
Character mcgee;
Character bates;
Ghost ghost1;
Ghost ghost2;
Ghost ghost3;
Character player;

int lv = 7;
int script = 82;
int fade = 0;
int timer;

String phone = "";

PImage skull;
PImage c_spritesheet;
PImage t_box;
PImage basement;
PImage kitchen;
PImage living_room;
PImage corazon;
PImage ouija;
PImage prop_sheet;
Prop table;
Prop chair1;
Prop rail;
Prop lamp;
Prop sofa;
Prop coffee_table;

PImage mascara;
PImage magnified, normal;

Lens lens;
int current;
String msg = "HELLO";

ArrayList<Prop> props_basement = new ArrayList<Prop>();
ArrayList<Prop> props_kitchen = new ArrayList<Prop>();
ArrayList<Prop> props_living_room = new ArrayList<Prop>();

Light[] lights = new Light[10];
int hue;
int glare;

public void setup() {
  
  background(0);
  noCursor();
  rectMode(CENTER);
  imageMode(CENTER);
  textAlign(CENTER);
  basement = loadImage("basement.png");
  kitchen = loadImage("kitchen.png");
  living_room = loadImage("living_room.png");
  corazon = loadImage("corazon.png");
  skull = loadImage("skull.png");
  c_spritesheet = loadImage("characters.png");
  ouija = loadImage("ouija.png");
  t_box = loadImage("t_box.png");
  lens = new Lens();
  current = 0;

  String port_name = Serial.list()[2];
  println(port_name);
  port = new Serial(this, port_name, 115200);
  port.bufferUntil('\n');

  song1 = new SoundFile(this, "pawrtl.wav");
  song2 = new SoundFile(this, "bound.wav");
  song3 = new SoundFile(this, "idfk.wav");
  song4 = new SoundFile(this, "about_schroder.wav");
  song1.loop();

  control = ControlIO.getInstance(this);
  gpad = control.getMatchedDevice("360 config");
  if (gpad == null) {
    println("No suitable device configured");
    System.exit(-1);
  }

  PFont font;
  font = loadFont("ATW_SB.vlw");
  textFont(font, 32);
  doug = new Character(c_spritesheet, 0, 0, 4);
  bates = new Character(c_spritesheet, 0, 0, 3);
  mcgee = new Character(c_spritesheet, 0, 0, 2);
  ghost1 = new Ghost(c_spritesheet, width/2, height/2, 6);
  ghost2 = new Ghost(c_spritesheet, width/2, height/2, 5);
  ghost3 = new Ghost(c_spritesheet, width/2, height/2, 7);
  //370, 400
  player = bates;
  doug.x = 500;
  doug.y = 450;
  bates.x = 650;
  bates.y = 450;
  mcgee.x = 1480;
  mcgee.y = 0;

  normal = loadImage("ouija.png");
  magnified = loadImage("ouija2.png");
  prop_sheet = loadImage("props.png");
  table = new Prop(prop_sheet.get(0, 40, 400, 240), 380, 770);
  chair1 = new Prop(prop_sheet.get(300, 360, 80, 40), 380, 630);
  rail = new Prop(prop_sheet.get(400, 0, 220, 170), 1330, 85);
  props_basement.add(table);
  props_basement.add(chair1);
  props_basement.add(rail);
  props_basement.add(bates);
  props_basement.add(mcgee);
  props_basement.add(doug);

  props_kitchen.add(doug);

  lamp = new Prop(prop_sheet.get(0, 370, 130, 360), 460, 560);
  sofa = new Prop(prop_sheet.get(630, 0, 510, 250), 750, 580);
  coffee_table = new Prop(prop_sheet.get(670, 250, 400, 150), 740, 750);
  props_living_room.add(lamp);
  props_living_room.add(sofa);
  props_living_room.add(coffee_table);
  props_living_room.add(doug);
  props_living_room.add(bates);
  props_living_room.add(mcgee);
  props_living_room.add(ghost3);

  for(int i = 0; i < 10; i++) lights[i] = new Light();
  hue = PApplet.parseInt(random(0, 255));
  glare = 255;

  dancin = new Movie(this, "dancin.mp4");
}

public void draw() {
  if(gpad.getButton("XBOX").pressed()) {
    while(gpad.getButton("XBOX").pressed());
    lv = 8;
  }
  switch(lv) {
    case 0:
      if(!song1.isPlaying()) song1.loop();
      titlescreen();
      break;
    case 1:
      song1.stop();
      if(!song2.isPlaying()) song2.loop();
      lv1();
      break;
    case 2:
      song1.stop();
      song3.stop();
      if(!song2.isPlaying()) song2.loop();
      lv2();
      break;
    case 3:
      lv3();
      break;
    case 4:
      break;
    case 5:
      board();
      break;
    case 6:
      song2.stop();
      if(!song3.isPlaying()) song3.loop();
      lv5();
      break;
    case 7:
      song1.stop();
      song2.stop();
      song3.stop();
      if(!song4.isPlaying()) song4.loop();
      lv6();
      break;
    case 8:
      credits();
      break;
  }
}

public void titlescreen() {
  background(0);
  fill(255);
  textSize(72);
  image(skull, width/2, 300);
  text("Haunter's Call", width/2, 600);
  textSize(20);
  text("Enter phone number", 720, 650);
  text(phone, 720, 700);
  if(phone.length() == 10 && gpad.getButton("Start").pressed()) {
    lv = 1;
    while(gpad.getButton("Start").pressed());
  }
}

public void lv1() {
  background(basement);
  textSize(20);
  Collections.sort(props_basement);
  for(Prop prop : props_basement) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    }
    if(prop == ghost3) {
      ghost3.update();
    }
    for(Prop prop2 : props_basement) {
      if(prop2 != player && (prop2.x+prop2.w/2 > player.x && prop2.x-prop2.w/2 < player.x) && player.x>prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, prop2.x+prop2.w/2, width);
      }
      if(prop2 != player && (prop2.x-prop2.w/2 < player.x && prop2.x+prop2.w/2 > player.x) && player.x<prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, 0, prop2.x-prop2.w/2);
      }
    }
    prop.show();
  }
  switch(script) {
    case 0:
      if(doug.say("And that's why the soles of my shoes \nare all uneven.")) script++;
      break;
    case 1:
      if(bates.say("I always thought you walked like that \nbecause you were trying to look cool.")) script++;
      break;
    case 2:
      if(doug.say("What do you mean, trying?")) script++;
      break;
    case 3:
      if(bates.say("...Hey! I think I hear McGee coming!")) script++;
      break;
    case 4:
      mcgee.restrained = false;
      if(mcgee.x > 1130) {
        mcgee.update(-.7f, 0);
        mcgee.update(0, .25f);
      } else if(mcgee.y < 450){
        mcgee.update(0, .7f);
      } else if(mcgee.x > 800) {
        mcgee.update(-.7f, 0);
      } else {
        mcgee.update(0,0);
        script = 5;
      }
      break;
    case 5:
      if(mcgee.say("Ey yooo, smells like updog in here.")) script++;
      break;
    case 6:
      if(doug.say("Shut the fuck up McGee.")) script++;
      break;
    case 7:
      if(mcgee.say("Haha, love you too Doug. Have you guys \nsettled on what we're playing today?")) script++;
      break;
    case 8:
      if(bates.say("No. We were just figuring that out. \nMind if I take a look in your bookshelf, \nDoug?")) script++;
      break;
    case 9:
      if(doug.say("Nah, knock yourself out.")) script++;
      break;
    case 10:
      if(player.x < 200 && player.x > 0 && player.y < 500 && player.y > 445) {
        fill(0);
        stroke(255);
        strokeWeight(3);
        ellipse(100, 360, 50, 50);
        fill(255);
        text("A", 103, 365);
        if(gpad.getButton("A").pressed()) {
          script++;
        }
      }
      break;
    case 11:
      if(bates.say("You guys wanna play Tokaido? Or we could \ngo for another round of Pandemic.")) script++;
      break;
    case 12:
      if(mcgee.say("We played that last week. You got anything\nelse Doug?")) script++;
      break;
    case 13:
      if(doug.say("I don't fucking know man. Take a look at \nthe top shelf, Bates.")) script++;
      break;
    case 14:
      if(player.x < 200 && player.x > 0 && player.y < 500 && player.y > 445) {
        fill(0);
        stroke(255);
        strokeWeight(3);
        ellipse(100, 360, 50, 50);
        fill(255);
        text("A", 103, 365);
        if(gpad.getButton("A").pressed()) {
          script++;
        }
      }
      break;
    case 15:
      if(bates.say("What about this one? The box is all dusty \nthough.")) script++;
      break;
    case 16:
      if(doug.say("Fuck if I know. I think it's that old ouija\nboard from Ireland my old man got me for \nmy birthday last year.")) script++;
      break;
    case 17:
      if(mcgee.say("Does it conjure up Irish ghosts or sober \nones?")) script++;
      break;
    case 18:
      if(doug.say("It doesn't 'conjure up' anything, you dingus. \nIt's just for talking with ghosts.")) script++;
      break;
    case 19:
      if(doug.say("If we wanted to summon stuff then we \ncould just play Jumanji or some shit.")) script++;
      break;
    case 20:
      if(bates.say("How does it even work, though? Is it like a \nphone call that any ghost can pick up?")) script++;
      break;
    case 21:
      if(bates.say("What's to stop Chinese ghosts from picking\nup? I don't see any Chinese characters on\nthis.")) script++;
      break;
    case 22:
      if(mcgee.say("Well let's give it a try. Maybe we can get a\ncelebrity ghost. Set it on the table, Bates.")) script++;
      break;
    case 23:
      if(player.x < 225 && player.x > 175 && player.y < 725 && player.y > 675) {
        fill(0);
        stroke(255);
        strokeWeight(3);
        ellipse(320, 700, 50, 50);
        fill(255);
        text("A", 323, 705);
        if(gpad.getButton("A").pressed()) {
          script++;
        }
      }
      break;
    case 24:
      if(bates.say("Here.")) script++;
      break;
    case 25:
      mcgee.restrained = false;
      doug.restrained = false;
      if(mcgee.x > 600) {
        mcgee.update(-.7f, 0);
      } else if(mcgee.y < 720){
        mcgee.update(0, .7f);
      } else {
        mcgee.update(0, 0);
        mcgee.restrained = true;
      }
      if(doug.x > 370) {
        doug.update(-.7f, 0);
      } else if(doug.y < 630){
        doug.update(0, .7f);
      } else {
        doug.update(0, 0);
        doug.restrained = true;
      }
      if(mcgee.restrained && doug.restrained) {
          script++;
      }
      break;
    case 26:
      if(mcgee.say("Oh, great spirit of the beyond. If you are\nnearby and speak in a language that uses \nthe English alphabet, send us a message...")) {
        port.write("s" + phone + "Your presence has been requested in the mortal realm. If you wish to respond, send a message here.");
        delay(10000);
        port.write("r");
        delay(4000);
        script++;
      }
      break;
    case 27:
      if(val.charAt(0) == 'O' && val.charAt(1) == 'K') {
        port.write("r");
        delay(8000);
      } else {
        msg = val.substring(0, val.length()-2).toUpperCase();
        lv = 5;
        port.write("d");
        script++;
      }
      break;
    case 28:
      if(bates.say("The spirit says '" + msg + "'.")) script++;
      break;
    case 29:
      if(doug.say("Sounds like a retarded fucking ghost if \nyou ask me.")) script++;
      break;
    case 30:
      if(doug.say("I'm getting thirsty anyway. I'm gonna go \nupstairs to make myself an Arnold Palmer.")) script++;
      break;
    case 31:
      if(doug.say("You guys want anything?")) script++;
      break;
    case 32:
      if(bates.say("I'll have one too. Thank you Doug.")) script++;
      break;
    case 33:
      if(doug.say("How about you, Mcgee?")) script++;
      break;
    case 34:
      if(mcgee.say("Do you put sugar in your iced tea?")) script++;
      break;
    case 35:
      if(doug.say("No, I just put a shit ton of sugar in the \nlemonade so it balances out.")) script++;
      break;
    case 36:
      if(mcgee.say("I'll take a lemonade then.")) script++;
      break;
    case 37:
      if(doug.say("Alright when I come back we can give it \nanother go but Mcgee is not allowed to touch\nit.")) script++;
      break;
    case 38:
      if(doug.say("I don't want him moving it and spelling\nout any more dumb shit. I'll be right back.")) script++;
      break;
    case 39:
      doug.restrained = false;
      bates.restrained = false;
      if(doug.x < 1120) {
        doug.update(1, 0);
      } else if(doug.y > 100){
        doug.update(0, -1);
      } else if(doug.x < 1450) {
        doug.update(.7f, -.25f);
      } else {
        script++;
      }
      break;
    case 40:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        lv=6;
      }
      break;
    case 52:
      if(ghost3.say("So I beat the shit out of him for giving me \nthe finger. Turns out he was just saying \n'thank you' in sign language.")) script++;
      break;
    case 53:
      if(doug.say("What in the world is going on here?")) script++;
      break;
    case 54:
      if(bates.say("Oh hey Doug. This is our new friend, Iverson.")) script++;
      break;
    case 55:
      if(ghost3.say("Hullo, are those Arnold Palmers:?")) script++;
      break;
    case 56:
      if(mcgee.say("We can finally play 4-player games!")) script++;
      break;
    case 57:
      if(doug.say(".....")) {
        script++;
      }
      break;
    case 58:
      if(doug.say("Does he know how to play pandemic?")) script++;
      break;
    case 59:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
        doug.restrained = false;
      }
      break;
    case 60:
      fill(0, fade);
      stroke(0);
      rect(width/2,height/2,width,height);
      if(doug.x > 370) {
        doug.update(-.7f, 0);
      } else if(doug.y < 630){
        doug.update(0, .7f);
      } else {
        doug.update(0, 0);
        doug.restrained = true;
        script++;
      }
      break;
    case 61:
      if(fade > 0) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade -= 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
      }
      break;
    case 62:
      if(mcgee.say("..And that makes three wins!")) script++;
      break;
    case 63:
      if(mcgee.say("I don't think we've ever won this many times\nin a row before.")) script++;
      break;
    case 64:
      if(bates.say("I'm kinda getting tired of this though. Can\nwe play Aleanation now?")) script++;
      break;
    case 65:
      if(doug.say("No way in hell we're playing that. It's gonna\ntake forever to teach the new guy.")) script++;
      break;
    case 66:
      if(ghost3.say("Alienation?")) script++;
      break;
    case 67:
      if(doug.say("AlEAnation. It's a game Bates made but he al\nways wins because he's the only one that can\nsolve a rubik's cube in under 3 minutes.")) script++;
      break;
    case 68:
      if(ghost3.say("Well that sounds interesting but I'm afraid I \nhave to go. I have some business to \nattend to.")) script++;
      break;
    case 69:
      if(bates.say("Will you come back once you're done?")) script++;
      break;
    case 70:
      if(ghost3.say("The board resides here so I'll usually be\naround until I've done my time.")) script++;
      break;
    case 71:
      if(ghost3.say("I hope I can make it in time for the next\nsession of 'Aleanation'.")) {
        script++;
        ghost3.x = width/2;
        ghost3.y = height/2;
      }
      break;
    case 72:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
        lv = 7;
      }
      break;
  }

  //Freeroam
  if(player.x < 1220 && player.x > 1000 && player.y < 450 && player.y > 400 && !(script >= 39)) {
    fill(0);
    stroke(255);
    strokeWeight(3);
    ellipse(1110, 310, 50, 50);
    fill(255);
    text("A", 1113, 315);
  }
  //    port.write("c" + phone);
  if(gpad.getButton("Select").pressed()) {
    while(gpad.getButton("Select").pressed());
    lv = 3;
  }
}

public void lv2() {
  background(kitchen);
  textSize(20);
  Collections.sort(props_kitchen);
  for(Prop prop : props_kitchen) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    }
    if(prop == ghost1) {
      ghost1.update();
    }
    for(Prop prop2 : props_kitchen) {
      if(prop2 != player && (prop2.x+prop2.w/2 > player.x && prop2.x-prop2.w/2 < player.x) && player.x>prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, prop2.x+prop2.w/2, width);
      }
      if(prop2 != player && (prop2.x-prop2.w/2 < player.x && prop2.x+prop2.w/2 > player.x) && player.x<prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, 0, prop2.x-prop2.w/2);
      }
    }
    prop.show();
  }
  // if(player.x < 1400 && player.x > 1300 && player.y < 600 && player.y > 550) {
  //  fill(0);
  //  stroke(255);
  //  strokeWeight(3);
  //  ellipse(1380, 500, 50, 50);
  //  fill(255);
  //  text("A", 1383, 505);
  //  if(gpad.getButton("A").pressed()) {
  //    while(gpad.getButton("A").pressed());
  //    player.x = 100;
  //    player.y = 450;
  //    lv = 3;
  //  }
  // }
  if(player.x < 1250 && player.x > 1150 && player.y < 430 && player.y > 400) {
    fill(0);
    stroke(255);
    strokeWeight(3);
    ellipse(1210, 300, 50, 50);
    fill(255);
    text("A", 1213, 305);
    if(gpad.getButton("A").pressed()) {
      while(gpad.getButton("A").pressed());
      player.x = 1100;
      player.y = 425;
      lv = 1;
    }
  }
  switch(script) {
    case 48:
      if(fade > 0) {
          fill(0,fade);
          stroke(0);
          rect(width/2,height/2,width,height);
          fade -= 2;
        } else {
          script++;
        }
        break;
    case 49:
      if(doug.say("High and dryyyyyy..Out of the raaiiiin...")) script++;
      break;
    case 50:
      if(doug.say("Mcgee's gonna need an insulin chaser\nfor this lemonade. Better head downstairs\nbefore they start arguing.")) {
        script++;
        props_basement.add(ghost3);
        ghost3.x = 370;
        ghost3.y = 400;
      }
      break;
    case 51:
      script++;
      break;
    case 86:
      if(fade > 180) {
        fill(0,fade);
        stroke(0, fade);
        rect(width/2,height/2,width,height);
        fade -= 2;
      } else {
        fill(0,fade);
        stroke(0, fade);
        rect(width/2,height/2,width,height);
        timer = frameCount;
        script++;
      }
      break;
    case 87:
      fill(0,fade);
      stroke(0, fade);
      rect(width/2,height/2,width,height);
      if(frameCount-timer == 200) {
        script++;
        fade = 0;
        doug.x = 1350;
      }
      break;
    case 88:
      if(doug.say("What are you doing in the dark?")) script++;
      break;
    case 89:
      if(ghost1.say("Just got back. Didn't feel like going\nanywhere else.")) script++;
      break;
    case 90:
      if(doug.say("Wanna come watch Annie Hall with us?\nI made fish tacos.")) script++;
      break;
    case 91:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
        lv = 3;
        doug.x = 850;
        doug.y = 550;
        bates.x = 750;
        bates.y = 550;
        mcgee.x = 650;
        mcgee.y = 550;
        ghost3.y = 300;
      }
      break;
  }
}

public void lv3() {
  background(living_room);
  textSize(20);
  bates.sit();
  mcgee.sit();
  doug.sit();
  for(Prop prop : props_living_room) {
    // if(prop == player) {
    //   player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    // }

    if(prop == ghost3) ghost3.update();
    prop.show();
  }
  if(player.x < 100 && player.x > -1 && player.y < 550 && player.y > 500) {
    fill(0);
    stroke(255);
    strokeWeight(3);
    ellipse(50, 530, 50, 50);
    fill(255);
    text("A", 53, 535);
    if(gpad.getButton("A").pressed()) {
      while(gpad.getButton("A").pressed());
      bates.x = 1300;
      bates.y = 575;
      lv = 2;
    }
  }
  tv_glare();
  if(fade > 0) {
    fill(0,fade);
    stroke(0, fade);
    rect(width/2,height/2,width,height);
    fade -= 2;
  }
}

public void lv5() {
  background(0);
  if(script < 48 && fade < 254) {
    ghost1.update();
    ghost1.show();
  } else if(fade < 254) {
    ghost2.update();
    ghost2.show();
  }
  switch(script) {
    case 40:
      if(fade > 0) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade -= 2;
      } else {
        script++;
      }
    case 41:
      if(ghost1.say("She was...")) script++;
      break;
    case 42:
      if(ghost1.say("Pulchritude incarnate...She was...")) script++;
      break;
    case 43:
      if(ghost1.say("As perfect a human being as anyone could \never aspire to be.")) script++;
      break;
    case 44:
      if(ghost1.say("The way she looked at me while she talked...\nThe words she spoke and the way she \nphrased them. ")) script++;
      break;
    case 45:
      if(ghost1.say("I was all too aware that we belonged to\na different species altogether...She...")) script++;
      break;
    case 46:
      if(ghost1.say("Belonged to a world made for noone and I\nwas made to belong nowhere.")) script++;
      break;
    case 47:
      if(ghost1.say("Perhaps this is where I belong...At least \nuntil I'm called somewhere else... But until \nthen..")) script++;
      break;
    case 48:
      if(ghost2.say("\"Long live the underground.\"")) {
        script++;
        fade = 0;
      }
      break;
    case 49:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        player = doug;
        player.x = width/2;
        player.y = height/2;
        lv=2;
      }
  }
}

public void lv6() {
  background(corazon);
  ghost3.update();
  ghost3.show();
  lightshow();
  if(frameCount%40 == 0) hue = PApplet.parseInt(random(0,255));
  colorMode(HSB);
  int c = color(hue, 255, 255);
  stroke(c, 20);
  fill(c, 20);
  rect(width/2, height/2, width, height);
  switch(script) {
    case 73:
      if(fade > 0) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade -= 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
      }
      break;
    case 74:
      if(ghost3.say("This place hasn't changed a bit.")) script++;
      break;
    case 75:
      if(ghost3.say("I probably wasn't gone for too long.")) script++;
      break;
    case 76:
      if(ghost3.say("She's usually sitting at the bar at this time.")) script++;
      break;
    case 77:
      if(ghost3.say("What would I even say to her...")) script++;
      break;
    case 78:
      if(ghost3.say("In all the time I spent standing right across\nfrom her I never once was sincere.")) script++;
      break;
    case 79:
      if(ghost3.say("Perhaps I was merely using the 'it could never\nwork between us' schtick as a copout.")) script++;
      break;
    case 80:
      if(ghost3.say("It always felt like God had put her on Earth\njust to taunt me.")) script++;
      break;
    case 81:
      if(ghost3.say("Now it feels like he's sent me back down here\nfor the same reason.")) script++;
      break;
    case 82:
      if(ghost3.say("No point in staying here any longer.")) script++;
      break;
    case 83:
      if(ghost3.say("This is a game I refuse to play.")) script++;
      break;
    case 84:
      script++;
      props_kitchen.add(ghost1);
      break;
    case 85:
      if(fade < 255) {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        fade += 2;
      } else {
        fill(0,fade);
        stroke(0);
        rect(width/2,height/2,width,height);
        script++;
        lv = 2;
        song4.stop();
        doug.x = 1500;
        doug.y = 600;
      }
      break;
  }
}
public void credits() {
  if(song1.isPlaying()) song1.stop();
  if(song2.isPlaying()) song2.stop();
  if(song3.isPlaying()) song3.stop();
  if(song4.isPlaying()) song4.stop();
  dancin.play();
  background(0);
  image(dancin, 980, 410);
  stroke(255);
  fill(255);
  textSize(56);
  text("Special thanks to:", 720, 100);
  text("Drew Castalia \nPeter Jansen \nLaura Owen \nEvren Bozgeyikli \nGlenn Weyant", 480, 300);
  text("Without whom I wouldn't have been able to do this.", 720, 800);
  textSize(20);
  if(gpad.getButton("XBOX").pressed()) {
    while(gpad.getButton("XBOX").pressed());
    dancin.stop();
    lv = 1;
  }
}

public void movieEvent(Movie m) {
  m.read();
}

public void board() {
  background(255);
  fill(0);
  ellipse(lens.x, lens.y, 175, 175);
  mascara=get();
  normal.mask(mascara);
  image(magnified, (width/2) - (lens.x-width/2)/1.9f, (height/2) - (lens.y-height/2)/1.9f);
  image(normal, width/2, height/2);
  strokeWeight(5);
  noFill();
  stroke(150, 0, 0);
  ellipse(lens.x, lens.y, 175, 175);
  if(current < msg.length() && lens.update(msg.charAt(current))){
    lens.x_ready = false;
    lens.y_ready = false;
    current++;
  }
  if(current == msg.length()) {
    delay(2000);
    lv = 1;
  }
  if(gpad.getButton("Select").pressed()) {
    while(gpad.getButton("Select").pressed());
    lv = 1;
  }
}

public void lightshow() {
  for(int i = 0; i < lights.length; i++) {
    lights[i].show();
    if(lights[i].update()) lights[i] = new Light();
  }
}

public void tv_glare() {
  colorMode(HSB);
  if(frameCount%30 == 0) {
    glare = color(PApplet.parseInt(random(0,255)), 100, 100);
  } else if(frameCount%40 == 0) {
    glare = color(PApplet.parseInt(random(0,255)), 100, 100);
  } else if(frameCount%50 == 0) {
    glare = color(PApplet.parseInt(random(0,255)), 100, 100);
  } if(frameCount%60 == 0) {
    glare = color(0,0,100);
  }
  stroke(glare, 20);
  fill(glare, 20);
  rect(width/2, height/2, width/2, height/2);
  stroke(0, 20);
  fill(0, 80);
  rect(width/2, height/2, width, height);
}

public void keyPressed() {
  if(lv == 0) {
    if(key == ENTER && phone.length() == 10) {
      lv = 1;
    } else if(key == BACKSPACE && phone.length() > 0) {
      phone = phone.substring(0, phone.length()-1);
    } else if(phone.length() < 10) {
      phone += key;
    }
  }
}

public void serialEvent(Serial port) {
  val = port.readStringUntil('\n');
}
class Character extends Prop {
  PImage spritesheet;
  PImage t_box = loadImage("t_box.png");
  int count;
  int index;
  int current;
  int size = 32*5;
  float deadzone = .2f;
  boolean restrained;
  Dialog line_queue;

  Character(PImage new_s, int new_x, int new_y, int indx) {
    super(new_s, new_x, new_y);
    imageMode(CENTER);
    spritesheet = new_s;
    index = indx;
    sprite = spritesheet.get(0, index*size, size, size);
    count = 2;
    w = size;
    h = size;
    restrained = true;
  }

  public @Override
  void update(float lr, float ud) {
    current = frameCount/12%count;
    if(lr > deadzone || lr < -deadzone || ud > deadzone || ud < -deadzone) {
      if(lr > deadzone) {
        x += lr * speed;
        sprite = spritesheet.get(current*size + 6*size, index*size, size, size);
        if(restrained) {
          x = constrain(x, 0, 1440);
          y = constrain(y, 420, 900);
        }
      }
      if(lr < -deadzone) {
        x += lr * speed;
        sprite = spritesheet.get(current*size + 4*size, index*size, size, size);
        if(restrained) {
          x = constrain(x, 0, 1440);
          y = constrain(y, 420, 900);
        }
      }
      if(ud > deadzone) {
        y +=ud * speed;
        sprite = spritesheet.get(current*size + 2*size, index*size, size, size);
        if(restrained) {
          x = constrain(x, 0, 1440);
          y = constrain(y, 420, 900);
        }
      }
      if (ud < -deadzone) {
        y += ud * speed;
        sprite = spritesheet.get(current*size + 9*size, index*size, size, size);
        if(restrained) {
          x = constrain(x, 0, 1440);
          y = constrain(y, 420, 900);
        }
      }
    } else {
      sprite = spritesheet.get(0, index*size, size, size);
    }
  }

  public boolean say(String line) {
    if(line_queue == null) {
      line_queue = new Dialog(line, t_box, this.index, this.spritesheet);
    }
    line_queue.update();
    if(line_queue.count/2 > line_queue.text.length()) {
      if(gpad.getButton("A").pressed()) {
        while(gpad.getButton("A").pressed());
        line_queue = null;
        return true;
      }
    }
    return false;
  }

  public void sit() {
    sprite = spritesheet.get(11*size, index*size, size, size);
  }
}
class Dialog {
  String text;
  PImage t_box;
  PImage spritesheet;
  int size;
  int index;
  int count;
  int time;
  boolean skipped;
  
  Dialog(String txt, PImage textbox, int idx, PImage ss) {
    t_box = textbox;
    spritesheet = ss;
    size = 32*5;
    text = txt;
    index = idx;
    count = 1;
    time = frameCount;
    skipped = false;
  }
  
  public void update() {
    count = frameCount - time;
    stroke(255);
    strokeWeight(3);
    fill(0);
    rect(width/2, 100, 700, 100, 10);
    fill(255);
    rect(width/2 + 290, 100, 117, 100, 0, 10, 10, 0);
    if(count/2 < text.length()) {
      image(spritesheet.get((count/5%2)*size, index*size, size, 100), width/2 +290, 97);
    } else {
      image(spritesheet.get(0, index*size, size, 100), width/2 + 290, 97);
    }
    textAlign(LEFT);
    textSize(26);
    text(text.substring(0, min(count/2, text.length())), 380, 80);
    textSize(20);
    textAlign(CENTER);
  }
}
class Ghost extends Character {
  
  Ghost(PImage new_s, int new_x, int new_y, int indx) {
    super(new_s, new_x, new_y, indx);
    count = 4;
  }
  
  public void update() {
    current = frameCount/8%count + 2;
    sprite = spritesheet.get(current*size, index*size, size, size);
  }
}
class Lens {
  int size;
  int speed;
  int buffer;
  float x;
  float y;
  boolean x_ready;
  boolean y_ready;
  HashMap<String, Integer[]> coords;
  
  Lens() {
    x = width/2;
    y = height/2;
    x_ready = false;
    x_ready = false;
    speed = 20;
    buffer = 10;
    coords = new HashMap<String,Integer[]>(); 
    coords.put("A", new Integer[]{157, 346});
    coords.put("B", new Integer[]{218, 324});
    coords.put("C", new Integer[]{280, 305});
    coords.put("D", new Integer[]{338, 277});
    coords.put("E", new Integer[]{409, 246});
    coords.put("F", new Integer[]{469, 222});
    coords.put("G", new Integer[]{529, 207});
    coords.put("H", new Integer[]{917, 212});
    coords.put("I", new Integer[]{964, 218});
    coords.put("J", new Integer[]{1024, 235});
    coords.put("K", new Integer[]{1082, 260});
    coords.put("L", new Integer[]{1150, 295});
    coords.put("M", new Integer[]{1219, 323});
    coords.put("N", new Integer[]{1295, 351});
    coords.put("O", new Integer[]{197, 552});
    coords.put("P", new Integer[]{259, 535});
    coords.put("Q", new Integer[]{332, 507});
    coords.put("R", new Integer[]{407, 477});
    coords.put("S", new Integer[]{473, 437});
    coords.put("T", new Integer[]{540, 417});
    coords.put("U", new Integer[]{926, 414});
    coords.put("V", new Integer[]{999, 427});
    coords.put("W", new Integer[]{1072, 460});
    coords.put("X", new Integer[]{1150, 499});
    coords.put("Y", new Integer[]{1215, 523});
    coords.put("Z", new Integer[]{1281, 544});
    coords.put("1", new Integer[]{418, 688});
    coords.put("2", new Integer[]{471, 688});
    coords.put("3", new Integer[]{529, 688});
    coords.put("4", new Integer[]{604, 688});
    coords.put("5", new Integer[]{666, 688});
    coords.put("6", new Integer[]{730, 688});
    coords.put("7", new Integer[]{794, 688});
    coords.put("8", new Integer[]{862, 688});
    coords.put("9", new Integer[]{928, 688});
    coords.put("0", new Integer[]{991, 688});
  }
  public boolean update(char next) {
    if(!coords.containsKey(str(next))) return true;
    if(x < coords.get(str(next))[0]-buffer) {
      x += speed;
    } else if(lens.x > coords.get(str(next))[0]+buffer) {
      x -= speed;
    } else {
      x_ready = true;
    }
    if(y < coords.get(str(next))[1]-buffer) {
      y += speed;
    } else if(y > coords.get(str(next))[1]+buffer) {
      y -= speed;
    } else {
      y_ready = true;
    }
    return (x_ready && y_ready);
  }
}
class Light {
  float x;
  float y;
  float r;
  int c;
  int life = 2000;
  float alpha;
  float speed = 2;

  Light() {
    x = random(0, width*1.5f);
    y = random(0, height);
    r = random(50, 200);
    alpha = random(0,200);
    colorMode(HSB);
    c = color(random(0,360),255,255);
  }

  public boolean update() {
    if(alpha >= 200) speed *= -1;
    alpha += speed;
    x -= r*.01f;
    if(alpha <= 0) return true;
    else return false;
  }

  public void show() {
    stroke(c, alpha);
    fill(c, alpha);
    circle(x,y,r);
  }


}
class Prop implements Comparable<Prop>{
  int x;
  int y;
  int w;
  int h;
  float speed = 5;
  PImage sprite;
  
  Prop(PImage new_s, int new_x, int new_y) {
    x = new_x;
    y = new_y;
    sprite = new_s;
    w = sprite.width;
    h = sprite.height;
  }
  
  public void move(float new_x, float new_y) {
    if(x < new_x) {
      x += speed;
    } else if(x > new_x) {
      x -= speed;
    }
    if(y < new_y) {
      y += speed;
    } else if(y > new_y) {
      y -= speed;
    }
  }
  
  public @Override
  int compareTo(Prop other) {
    if(this.y + this.h/2 > other.y + other.h/2)
        return 1;
    else if (this.y + this.h/2 == other.y + other.h/2)
        return 0 ;
    return -1 ;
  }
  
  public void update(float ud, float lr) {
  }
  
  public void show() {
    image(sprite, x, y);
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Haunter_p" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
