package teamproject.bloom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "wines")
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@NoArgsConstructor
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Float alcohol;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Variety variety;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Value value;
    @Column(nullable = false)
    private Integer vintage;
    @ManyToOne
    @JoinColumn(name = "grape_id")
    private Grape grape;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    private String description;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public Wine(Long id) {
        this.id = id;
    }

    public enum Variety {
        RED,
        WHITE,
        ROSE,
        ORANGE,
        NATURAL,
        PET_NAT,
        SPARKLING,
        PROSECCO
    }

    public enum Value {
        ORGANIC,
        NON_ORGANIC,
        NATURAL,
        VEGAN
    }
}
