package hmoa.hmoaserver.admin.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeByHome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_home_id")
    private Long id;
    private String title;
    @OneToMany(mappedBy = "perfumeByHome", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Perfume> perfumeList =new ArrayList<>();

    @Builder
    public PerfumeByHome(String title){
        this.title = title;
    }
}
