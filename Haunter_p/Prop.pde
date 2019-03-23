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
  
  void move(float new_x, float new_y) {
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
  
  @Override
  int compareTo(Prop other) {
    if(this.y + this.h/2 > other.y + other.h/2)
        return 1;
    else if (this.y + this.h/2 == other.y + other.h/2)
        return 0 ;
    return -1 ;
  }
  
  void update(float ud, float lr) {
  }
  
  void show() {
    image(sprite, x, y);
  }
}
