package teamproject.bloom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "wines")
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id=?")
@SQLRestriction("is_delete=false")
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer alcohol;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false)
    private Integer vintage;
    private String img;
    private String grape;
    private String region;
    private String description;
    @Column(nullable = false)
    private Boolean isDeleted = false;

    public enum Type {
        RED,
        WHITE,
        ROSE,
        ORANGE,
        NATURAL,
        PET_NAT,
        SPARKLING
    }
}
