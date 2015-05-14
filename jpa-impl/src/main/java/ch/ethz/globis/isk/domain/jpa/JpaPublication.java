package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import org.hibernate.annotations.*;
import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Publication")
@Table(name = "publication")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication_type")
@DiscriminatorOptions(force = true)
public class JpaPublication implements Publication {

    @Id
    private String id;

    @Column(length = 1000)
    @Index(name = "index_title")
    private String title;

    @Column(length = 500)
    private String electronicEdition;

    @Index(name = "index_year")
    private int year;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = JpaPerson.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = "publication_author", joinColumns = @JoinColumn(name = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "person_id", nullable = false))
    @ForeignKey(name = "fk_authors")
    @OrderBy("name")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Person> authors;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = JpaPerson.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = "publication_editor", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    @ForeignKey(name = "fk_editors")
    private Set<Person> editors;

    public JpaPublication() {
        editors = new HashSet<>();
        authors = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Person> getAuthors() {
        return authors;
    }

    @Override
    public void setAuthors(Set<Person> authors) {
        this.authors = authors;
    }

    public Set<Person> getEditors() {
        return editors;
    }

    @Override
    public void setEditors(Set<Person> editors) {
        this.editors = editors;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getElectronicEdition() {
        return electronicEdition;
    }

    public void setElectronicEdition(String electronicEdition) {
        this.electronicEdition = electronicEdition;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Publication{");
        sb.append("key='").append(getId()).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", year=").append(year);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Publication))
            return false;
        Publication that = (Publication) o;
        if (!getId().equals(that.getId()))
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        return result;
    }
}
