package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void addSection2(Station upStation, Station downStation, int distance) {
        // Exception(saveLine이 아닌 addSection만 타야 함..)
        getStations().stream()
                .filter(station -> {
                    return station.equals(upStation) || station.equals(downStation);
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("신규 구간의 역과 일치하는 역이 존재하지 않습니다."));

        List<Station> stationsToAdd = List.of(upStation, downStation);
        stationsToAdd.stream()
                .filter(stationToAdd -> getStations().stream().anyMatch(Predicate.isEqual(stationToAdd)))
                .findFirst()
                .ifPresent(station -> new IllegalArgumentException("신규 구간의 역이 이미 존재합니다."));

        getSections().stream()
                .filter(section -> {
                    return (section.getUpStation().equals(upStation) && section.getDistance() <= distance)
                            || (section.getDownStation().equals(downStation) && section.getDistance() <= distance);
                })
                .findFirst()
                .ifPresent(section -> new IllegalArgumentException("구간 거리가 같거나 커 역 중간에 등록이 불가합니다."));

        // Logic
        getSections().stream()
                .filter(section -> section.getUpStation().equals(upStation) && section.getDistance() > distance)
                .findFirst();

        if (getFirstStation().equals(upStation)) {

        }

        if (getFirstStation().equals(downStation)) {

        }

        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void removeSection(Long stationId) {
        if (!getLastStation().compare(stationId)) {
            throw new IllegalArgumentException();
        }

        sections.removeSection(stationId);
    }

    public Station getFirstStation() {
        return sections.getFirstStation();
    }

    public Station getLastStation() {
        return sections.getLastStation();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
