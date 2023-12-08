package hmoa.hmoaserver.homemenu.domain;

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
public class HomeMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_menu_id")
    private Long id;
    private String title;
    @OneToMany(mappedBy = "homeMenu", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Perfume> perfumeList =new ArrayList<>();

    @Builder
    public HomeMenu(String title){
        this.title = title;
    }
}
