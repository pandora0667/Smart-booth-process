package wisoft.smart.booth.process;

import java.util.ArrayList;

public class Median {
  private int median;
  private int size;
  private int count;
  private boolean isMedian;
  private ArrayList<Integer> queue;
  private MedianSort medianSort;

  public Median(int size) {
    this.median = 0;
    this.count = 0;
    this.size = size;
    this.isMedian = false;
    this.queue = new ArrayList<>(size + 1);
    this.medianSort = new MedianSort();
  }

  public void setValue(int value) {
    push(value);
    if (count >= size) {
     medianSort.setArray(peek());
     isMedian = true;
     medianCalculation(medianSort.getArray());
    }
    count++;
  }

  public int getMedian() {
    return median;
  }

  public boolean isMedian() {
    return isMedian;
  }

  private void medianCalculation(int [] array) {
    median = array[((size+1)/2)-1];
  }

  private void push(int value) {
    queue.add(value);
    if (queue.size() > size)
      queue.remove(0);
  }

  private int[] peek() {
    int[] convert = new int[queue.size()];
    for (int i=0; i<queue.size(); i++) {
      convert[i] = queue.get(i);
    }
    return convert;
  }
}
