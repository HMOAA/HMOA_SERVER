package hmoa.hmoaserver.homemenu.domain;

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
    private List<PerfumeHomeMenu> perfumeHomeMenus =new ArrayList<>();

    @Builder
    public HomeMenu(String title){
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
