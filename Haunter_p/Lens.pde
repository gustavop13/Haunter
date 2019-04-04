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
  boolean update(char next) {
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
