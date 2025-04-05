package org.pt.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdateInfoDto {
    private Integer id;
    private Optional<String> bio=Optional.empty();
    private Optional<String> school=Optional.empty();
    private Optional<String> college=Optional.empty();
    private Optional<Integer> age=Optional.empty();
}
