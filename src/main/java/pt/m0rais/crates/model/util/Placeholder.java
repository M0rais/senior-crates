package pt.m0rais.crates.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Placeholder {

    private final String key, value;

    public String replace(String s) {
        return s.replace(key, value);
    }

}