package org.javers.core.metamodel.object;

import org.javers.common.collections.Defaults;
import org.javers.common.collections.Optional;
import org.javers.common.validation.Validate;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.metamodel.property.Property;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Captured state of client's domain object.
 * Values and primitives are stored 'by value',
 * Referenced Entities and ValueObjects are stored 'by reference' using {@link GlobalCdoId}
 *
 * @author bartosz walacik
 */
public final class CdoSnapshot extends Cdo {
    private CommitMetadata commitMetadata;
    private final Map<Property, Object> state;

    /**
     * should be assembled by {@link CdoSnapshotBuilder}
     */
    CdoSnapshot(GlobalCdoId globalId, CommitMetadata commitMetadata, Map<Property, Object> state) {
        super(globalId);
        Validate.argumentsAreNotNull(state, commitMetadata);
        this.state = state;
        this.commitMetadata = commitMetadata;
    }

    /**
     * @return {@link Optional#EMPTY}
     */
    @Override
    public Optional<Object> getWrappedCdo() {
        return Optional.empty();
    }

    public int size() {
        return state.size();
    }

    @Override
    public Object getPropertyValue(Property property) {
        Validate.argumentIsNotNull(property);
        Object val = state.get(property);
        if (val == null){
            return Defaults.defaultValue(property.getType());
        }
        return val;
    }

    @Override
    public Object getPropertyValue(String withName) {
        Property property = this.getGlobalId().getCdoClass().getProperty(withName);
        return getPropertyValue(property);
    }

    @Override
    public boolean isNull(Property property) {
        Validate.argumentIsNotNull(property);
        return !state.containsKey(property);
    }

    public CommitId getCommitId() {
        return commitMetadata.getId();
    }

    public CommitMetadata getCommitMetadata() {
        return commitMetadata;
    }

    public boolean stateEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CdoSnapshot other = (CdoSnapshot) o;
        return this.state.equals(other.state);
    }

    /**
     * returns non null properties
     */
    public Set<Property> getProperties() {
        return Collections.unmodifiableSet(state.keySet());
    }
}
