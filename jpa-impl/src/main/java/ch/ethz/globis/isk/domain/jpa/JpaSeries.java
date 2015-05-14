package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.Series;
import org.hibernate.annotations.Index;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Series")
@Table(name = "series")
public class JpaSeries implements Series {

    @Id
    private String id;

    @Index(name = "index_series_name")
    private String name;

    @OneToMany(targetEntity = JpaPublication.class)
    @JoinTable(name = "series_publication", joinColumns = { @JoinColumn(name = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "series_id", nullable = false) })
    private Set<Publication> publications;

    public JpaSeries() {
        publications = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<Publication> getPublications() {
        return publications;
    }

    @Override
    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Series{");
        sb.append(", id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
