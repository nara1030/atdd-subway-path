package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addSection(int index, Section section) {
        sections.add(index, section);
    }

    public List<Station> getStations() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Station getFirstStation() {
        return sections.get(0).getUpStation();
    }

    public Station getLastStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void removeSection(Long stationId) {
        sections.removeIf(section -> section.getDownStation().compare(stationId));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
