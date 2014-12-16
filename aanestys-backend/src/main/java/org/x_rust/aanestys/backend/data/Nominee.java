package org.x_rust.aanestys.backend.data;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Nominee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	private long id;

	@Column(unique = true)
	@NotNull
	@Size(min = 2, message = "Nominee name must have at least two characters")
	private String nomineeName = "";

	@ManyToMany(mappedBy = "nominees")
	private List<Category> categories = new LinkedList<Category>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public List<Category> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "Nominee [nomineeName=" + nomineeName + ", categories="
				+ categories + "]";
	}
}
