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
  
  void update() {
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
