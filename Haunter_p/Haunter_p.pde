// Gustavo Placencia Carranza

// Arduino
import processing.serial.*;
Serial port;
String val = "N/A";
boolean first = true;

// Java
import java.util.Map;
import java.util.Collections;

// Gamepad
import org.gamecontrolplus.gui.*;
import org.gamecontrolplus.*;
import net.java.games.input.*;

ControlIO control;
Configuration config;
ControlDevice gpad;

// Video
import processing.video.*;
Movie dancin;

// Sound
import processing.sound.*;
SoundFile song1;
SoundFile song2;
SoundFile song3;

// Game info
Character doug;
Character mcgee;
Character bates;
Ghost ghost1;
Ghost ghost2;
Character player;

int lv = 1;
int script = 38;
int fade = 0;

String phone = "";

PImage skull;
PImage c_spritesheet;
PImage t_box;
PImage basement;
PImage kitchen;
PImage living_room;
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

void setup() {
  fullScreen();
  background(0);
  noCursor();
  rectMode(CENTER);
  imageMode(CENTER);
  textAlign(CENTER);
  basement = loadImage("basement.png");
  kitchen = loadImage("kitchen.png");
  living_room = loadImage("living_room.png");
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
  props_living_room.add(bates);
  props_living_room.add(lamp);
  props_living_room.add(sofa);
  props_living_room.add(coffee_table);
  
  dancin = new Movie(this, "dancin.mp4");
}      

void draw() {
  if(gpad.getButton("XBOX").pressed()) {
    while(gpad.getButton("XBOX").pressed());
    lv = 6;
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
      lv2();
      break;
    case 3:
      lv3();
      break;
    case 4:
      lv4();
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
      credits();
      break;
  }
}

void titlescreen() {
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

void lv1() {
  background(basement);
  textSize(20);
  Collections.sort(props_basement);
  for(Prop prop : props_basement) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    }
    if(prop == ghost2) {
      ghost2.update();
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
        mcgee.update(-.7, 0);
        mcgee.update(0, .25);
      } else if(mcgee.y < 450){
        mcgee.update(0, .7);
      } else if(mcgee.x > 800) {
        mcgee.update(-.7, 0);
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
        mcgee.update(-.7, 0);
      } else if(mcgee.y < 720){
        mcgee.update(0, .7);
      } else {
        mcgee.update(0, 0);
        mcgee.restrained = true;
      }
      if(doug.x > 370) {
        doug.update(-.7, 0);
      } else if(doug.y < 630){
        doug.update(0, .7);
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
        doug.update(.7, -.25);
      } else {
        script++;
      }
      break;
    case 40:
      //Move doug and fade to black
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
  }   
  
  //Freeroam
  if(player.x < 1220 && player.x > 1000 && player.y < 450 && player.y > 400) {
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

void lv2() {
  background(kitchen);
  textSize(20);
  Collections.sort(props_kitchen);
  for(Prop prop : props_kitchen) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
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
  if(player.x < 1400 && player.x > 1300 && player.y < 600 && player.y > 550) {
    fill(0);
    stroke(255);
    strokeWeight(3);
    ellipse(1380, 500, 50, 50);
    fill(255);
    text("A", 1383, 505);
    if(gpad.getButton("A").pressed()) {
      while(gpad.getButton("A").pressed());
      bates.x = 100;
      bates.y = 450;
      lv = 3;
    }
  }
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
      if(doug.say("Mcgee's gonna need an insulin chaser\nfor this lemonade. Better head downstairs\n")) script++;
      break;
  }
}

void lv3() {
  background(living_room);
  textSize(20);
  Collections.sort(props_living_room);
  for(Prop prop : props_living_room) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    }
    for(Prop prop2 : props_living_room) {
      if(prop2 != player && (prop2.x+prop2.w/2 > player.x && prop2.x-prop2.w/2 < player.x) && player.x>prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-30 < player.y) {
        player.x = constrain(player.x, prop2.x+prop2.w/2, width);
      }
      if(prop2 != player && (prop2.x-prop2.w/2 < player.x && prop2.x+prop2.w/2 > player.x) && player.x<prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-30 < player.y) {
        player.x = constrain(player.x, 0, prop2.x-prop2.w/2);
      }
    }
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
}

void lv4() {
  background(living_room);
  textSize(20);
  Collections.sort(props_living_room);
  for(Prop prop : props_living_room) {
    if(prop == player) {
      player.update(gpad.getSlider("LX").getValue(), gpad.getSlider("LY").getValue());
    }
    for(Prop prop2 : props_living_room) {
      if(prop2 != player && (prop2.x+prop2.w/2 > player.x && prop2.x-prop2.w/2 < player.x) && player.x>prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, prop2.x+prop2.w/2, width);
      }
      if(prop2 != player && (prop2.x-prop2.w/2 < player.x && prop2.x+prop2.w/2 > player.x) && player.x<prop2.x && prop2.y+prop2.h/2 >= player.y+player.h/2 && prop2.y-prop2.h/2 < player.y) {
        player.x = constrain(player.x, 0, prop2.x-prop2.w/2);
      }
    }
    prop.show();
  }
}

void lv5() {
  background(0);
  if(script < 48 && fade < 254) {
    ghost1.update();
    ghost1.show();
  } else if(fade < 254){
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

void credits() {
  if(song2.isPlaying()) song2.stop();
  dancin.play();
  background(0);
  image(dancin, 980, 410);
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

void movieEvent(Movie m) {
  m.read();
}

void board() {
  background(255);
  fill(0);
  ellipse(lens.x, lens.y, 175, 175);
  mascara=get();
  normal.mask(mascara);
  image(magnified, (width/2) - (lens.x-width/2)/1.9, (height/2) - (lens.y-height/2)/1.9); 
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


void keyPressed() {
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

void serialEvent(Serial port) {
  val = port.readStringUntil('\n');
}
