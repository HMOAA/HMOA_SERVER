package hmoa.hmoaserver.perfume.review.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "PerfumeReview")
@AllArgsConstructor
public class PerfumeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_review_id")
    private Long id;
    private int spring;
    private int summer;
    private int autumn;
    private int winter;
    private int ten;
    private int twenty;
    private int thirty;
    private int fourty;
    private int fifty;
    private int man;
    private int woman;

    private int neuter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    public void increaseSpring(){
        this.spring+=1;
    }
    public void increaseSummer(){
        this.summer+=1;
    }
    public void increaseAutumn(){
        this.autumn+=1;
    }
    public void increaseWinter(){
        this.winter-=1;
    }
    public void decreaseSpring(){
        this.spring-=1;
    }
    public void decreaseSummer(){
        this.summer-=1;
    }
    public void decreaseAutumn(){
        this.autumn-=1;
    }
    public void decreaseWinter(){
        this.winter-=1;
    }
    public void increaseMan(){this.man+=1;}
    public void increaseWoman(){this.woman+=1;}
    public void increaseNeuter(){this.neuter+=1;}
    public void decreaseMan(){this.man-=1;}
    public void decreaseWoman(){this.woman-=1;}
    public void decreaseNeuter(){this.neuter-=1;}

    public void increaseTen(){this.ten +=1;}
    public void increaseTwenty(){this.twenty +=1;}
    public void increaseThirty(){this.thirty +=1;}
    public void increaseFourty(){this.fourty +=1;}
    public void increaseFifty(){this.fifty +=1;}

    public void decreaseTen(){this.ten -=1;}
    public void decreaseTwenty(){this.twenty -=1;}
    public void decreaseThirty(){this.thirty -=1;}
    public void decreaseFourty(){this.fourty -=1;}
    public void decreaseFifty(){this.fifty -=1;}
}
