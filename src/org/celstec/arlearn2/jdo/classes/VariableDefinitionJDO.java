package org.celstec.arlearn2.jdo.classes;

import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class VariableDefinitionJDO extends GameClass {

	
	@Persistent
	private String name;

    @Persistent
    private Long minValue;

    @Persistent
    private Long maxValue;

    @Persistent
    private Long startValue;

    @Persistent
    private Integer scope;

	public void setUniqueId() {
		this.id = KeyFactory.createKey(VariableDefinitionJDO.class.getSimpleName(), getGameId()+":"+getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Long getStartValue() {
        return startValue;
    }

    public void setStartValue(Long startValue) {
        this.startValue = startValue;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }
}
