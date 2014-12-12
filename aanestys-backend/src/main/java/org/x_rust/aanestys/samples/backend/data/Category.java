package org.x_rust.aanestys.samples.backend.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(unique=true)
    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(
        name="CATEGORY_NOMINEE",
        joinColumns={@JoinColumn(name="CATEGORY_ID")},
        inverseJoinColumns={@JoinColumn(name="NOMINEE_ID")})
    private List<Nominee> nominees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    public List<Nominee> getNominees() {
    	return nominees;
    }
    
    public void addNominee(Nominee nominee) {
    	nominees.add(nominee);
    	if (!nominee.getGategories().contains(this)) {
    		nominee.getGategories().add(this);
    	}
    }
}
