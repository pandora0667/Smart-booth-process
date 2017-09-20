package wisoft.smart.booth.process;

public class MedianSort {
  private int[] array;

  public void setArray(final int[] array) {
    this.array = array;
    selection();
  }

  private void selection() {
    int min;
    for (int i=0; i<array.length -1; i++) {
      min = i;
      for (int j=i+1; j<array.length; j++ ) {
        if (array[j] < array[min]) {
          min = j;
        }
      }
      swap(array, min, i);
    }
  }

  private void swap(final int[] array, final int idx1, final int idx2) {
    int temp = array[idx1];
    array[idx1] = array[idx2];
    array[idx2] = temp;
  }

  public int[] getArray() {
    return array;
  }

}
