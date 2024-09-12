package hmoa.hmoaserver.homemenu.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeHomeMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_home_menu_id")
    private Long id;

    @Column(name = "perfume_id")
    private Long perfumeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_menu_id")
    private HomeMenu homeMenu;

    @Builder
    public PerfumeHomeMenu(Long perfumeId, HomeMenu homeMenu) {
        this.perfumeId = perfumeId;
        this.homeMenu = homeMenu;
    }
}
