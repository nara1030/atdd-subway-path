package nextstep.subway.domain;

import nextstep.subway.exception.AllStationsOfSectionExistException;
import nextstep.subway.exception.InvalidDistanceOfSectionException;
import nextstep.subway.exception.NonStationOfSectionExistsException;

import javax.persistence.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public void addSection2(Station upStation, Station downStation, int distance) {
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        // 라인 최초 생성 시 Exception 처리 불필요
        if (sections.getSections().size() == 0) {
            sections.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        // Exception
        getStations().stream()
                .filter(station -> station.equals(upStation) || station.equals(downStation))
                .findFirst()
                .orElseThrow(() -> new NonStationOfSectionExistsException("신규 구간의 역과 일치하는 역이 존재하지 않습니다."));

        List<Station> stationsToAdd = List.of(upStation, downStation);
        List<Station> stationsMatched = stationsToAdd.stream()
                .filter(stationToAdd -> getStations().stream().anyMatch(Predicate.isEqual(stationToAdd)))
                .collect(Collectors.toList());
        if (stationsToAdd.equals(stationsMatched)) {
            throw new AllStationsOfSectionExistException("신규 구간의 역이 이미 존재합니다.");
        }

        getSections().stream()
                .filter(section -> {
                    return (section.getUpStation().equals(upStation) && section.getDistance() <= distance)
                            || (section.getDownStation().equals(downStation) && section.getDistance() <= distance);
                })
                .findFirst()
                .ifPresent(section -> {
                    throw new InvalidDistanceOfSectionException("구간 거리가 같거나 커 역 중간에 등록이 불가합니다.");
                });

        // Business Logic
        if (getFirstStation().equals(downStation)) {
            sections.addSection(0, new Section(this, upStation, downStation, distance));
        }

        if (getLastStation().equals(upStation)) {
            sections.addSection(new Section(this, upStation, downStation, distance));
        }

//        getSections().stream()
//                .filter(section -> section.getUpStation().equals(upStation) && section.getDistance() > distance)
//                .findFirst();
//
//        sections.addSection(new Section(this, upStation, downStation, distance));
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
