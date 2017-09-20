package wisoft.smart.booth.process;

import java.util.ArrayList;

public class Median {
  private int median;
  private final int size;
  private int count;
  private final ArrayList<Integer> queue;
  private final MedianSort medianSort;

  public Median(final int size) {
    this.median = 0;
    this.count = 0;
    this.size = size;
    this.queue = new ArrayList<>(size + 1);
    this.medianSort = new MedianSort();
  }

  public void setValue(String value) {
    push(Integer.parseInt(value));
    if (count >= size) {
     medianSort.setArray(peek());
     medianCalculation(medianSort.getArray());
    }
    count++;
  }

  public String getMedian() {
    return String.valueOf(median);
  }

  private void medianCalculation(final int [] array) {
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
