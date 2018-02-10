package org.javers.core.diff.changetype.container;

import org.javers.common.string.ToStringBuilder;

/**
 * element added to collection
 *
 * @author bartosz walacik
 */
public class ValueAdded extends ValueAddOrRemove {

    public ValueAdded(int index, Object value) {
        super(index, value);
    }

    public ValueAdded(Object value) {
        super(value);
    }

    public Object getAddedValue() {
        return value.unwrap();
    }

    @Override
    public String toString() {
        return (getIndex() == null ? "" : getIndex()) + ". " +
                ToStringBuilder.format(getAddedValue()) + " added";
    }
}
