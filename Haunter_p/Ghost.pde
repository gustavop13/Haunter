class Ghost extends Character {
  
  Ghost(PImage new_s, int new_x, int new_y, int indx) {
    super(new_s, new_x, new_y, indx);
    count = 4;
  }
  
  void update() {
    current = frameCount/8%count + 2;
    sprite = spritesheet.get(current*size, index*size, size, size);
  }
}
