package co.mscp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;




public class PartialList<T >  {

    private int offset;
    private int total;
    private List<T> items;


    public PartialList() {
        offset = 0;
        total = 0;
        items = Collections.emptyList();
    }


    public PartialList(final int offset, final int total, final List<T> items) {
        this.offset = offset;
        this.total = total;
        this.items = Collections.unmodifiableList(items);
    }


    public PartialList(final List<T> items) {
        this(0, items.size(), items);
    }


    public PartialList(final T[] items) {
        this(0, items.length, Arrays.asList(items));
    }

    @JsonIgnore
    public int getSize() {
        return items.size();
    }


    public List<T> getItems() {
        return items;
    }


    public int getOffset() {
        return offset;
    }


    public int getTotal() {
        return total;
    }

}
