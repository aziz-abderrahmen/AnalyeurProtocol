package model.protocols;

import java.util.List;

public class HTTP extends Protocol {
    private String name;

    public HTTP(List<String> data, String name) {
        super(data, 0, 0);
        this.name = name;
    }
}
