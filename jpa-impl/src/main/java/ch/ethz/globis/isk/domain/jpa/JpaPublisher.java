package ch.ethz.globis.isk.domain.jpa;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.Publisher;
import org.hibernate.annotations.Index;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Publisher")
@Table(name = "publisher")
public class JpaPublisher implements Publisher {

    @Id
    private String id;

    @Index(name = "index_publisher_name")
    private String name;

    @OneToMany(targetEntity = JpaPublication.class)
    @JoinTable(name = "publisher_publication", joinColumns = { @JoinColumn(name = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "publisher_id", nullable = false) })
    private Set<Publication> publications;

    public JpaPublisher() {
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
        final StringBuffer sb = new StringBuffer("Publisher{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
