class Character extends Prop {
  PImage spritesheet;
  PImage t_box = loadImage("t_box.png");
  int count;
  int index;
  int current;
  int size = 32*5;
  float deadzone = .2;
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
  
  @Override
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
  
  boolean say(String line) {
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
}
