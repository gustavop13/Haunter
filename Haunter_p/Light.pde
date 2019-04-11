class Light {
  float x;
  float y;
  float r;
  color c;
  int life = 2000;
  float alpha;
  float speed = 2;

  Light() {
    x = random(0, width*1.5);
    y = random(0, height);
    r = random(50, 200);
    alpha = random(0,200);
    colorMode(HSB);
    c = color(random(0,360),255,255);
  }

  boolean update() {
    if(alpha >= 200) speed *= -1;
    alpha += speed;
    x -= r*.01;
    if(alpha <= 0) return true;
    else return false;
  }

  void show() {
    stroke(c, alpha);
    fill(c, alpha);
    circle(x,y,r);
  }


}
